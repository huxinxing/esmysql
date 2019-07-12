package com.zhifan.monitor_manager.shiro;

import com.zhifan.monitor_manager.domain.entity.TMSystemAccountInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {


    /**
     * 执行登录认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        return executeLogin(request, response);

    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        try {
            log.info("接收到的参数为：");
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String token = httpServletRequest.getHeader("Authorization");
            log.info("获取的token为：" + token);
            if (StringUtils.isEmpty(token)) {
                setHeader((HttpServletRequest) request,(HttpServletResponse) response);
                log.error("========无请求头，请登录==========");
                String str = "{\"url\":\"login.html\",\"message\":\"请登录\"}";
                returnJson(response, str);
                return false;
            } else {
                JwtToken jwtToken = new JwtToken(token);
                // 提交给realm进行登入，如果错误他会抛出异常并被捕获
                getSubject(request, response).login(jwtToken);
                //getSubject(request,response).checkRole("admisn");
                // 如果没有抛出异常则代表登入成功，返回true
                TMSystemAccountInfo tmSystemAccountInfo = (TMSystemAccountInfo) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
                if (ObjectUtils.isEmpty(tmSystemAccountInfo)) {
                    log.error("===========用户不存在=============");
                    String str = "{\"url\":\"login.html\",\"message\":\"用户不存在,请登录\"}";
                    returnJson(response, str);
                    return false;
                }


                /**
                 * 包括用户登录的超时操作皆在此进行过滤，可以采用redis记录用户登录信息
                 */

            }
        } catch (Exception e) {
            log.error("========登录异常==========");
            log.error("登录失败", e);
            String str = "{\"url\":\"login.html\",\"message\":\"请登录\"}";
            returnJson(response, str);
            return false;
        }
        return true;
    }


    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            setHeader(httpRequest,httpResponse);
            return true;
        }
        return super.preHandle(request, response);
    }


    private void returnJson(ServletResponse response, String json) {

        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        try {
            writer = response.getWriter();
            writer.write(json);
        } catch (IOException e) {
            log.error("response error");
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    private void setHeader(HttpServletRequest request, HttpServletResponse response){
        //跨域的header设置
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //防止乱码，适用于传输JSON数据
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
    }


}

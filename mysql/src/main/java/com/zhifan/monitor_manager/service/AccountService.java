package com.zhifan.monitor_manager.service;

import com.github.pagehelper.PageInfo;
import com.zhifan.monitor_manager.domain.dto.parmary.AccountAddMsg;
import com.zhifan.monitor_manager.domain.dto.resultDto.AccountMsg;
import com.zhifan.monitor_manager.domain.entity.TMSystemAccountInfo;
import com.zhifan.monitor_manager.domain.repository.TMSSystemAccountInfoRepository;
import com.zhifan.monitor_manager.shiro.JwtToken;
import com.zhifan.monitor_manager.util.GoogleAuthenticatorUtil;
import com.zhifan.monitor_manager.util.JwtUtil;
import com.zhifan.monitor_manager.util.PasswordUtil;
import com.zhifan.monitor_manager.util.RandomUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AccountService {


    @Autowired
    TMSSystemAccountInfoRepository tmsSystemAccountInfoRepository;

    /**
     * 获取google验证码私钥
     */
    public Map<String, Object> googleGain() {
        Map<String, Object> resultMap = new HashMap<>();
        String googleKey = GoogleAuthenticatorUtil.generateSecretKey();
        String url = "otpauth://totp/zhifan?secret=" + googleKey;
        resultMap.put("googlekey", googleKey);
        return resultMap;
    }

    /**
     * 验证google验证吗
     */
    public Map<String, Object> googleVerify(Long googleCode, String googleKey, Integer userId) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        TMSystemAccountInfo tmSystemAccountInfo = tmsSystemAccountInfoRepository.findByUserId(userId);

        if (ObjectUtils.isEmpty(tmSystemAccountInfo)) {
            throw new Exception("用户不存在");
        }

        if (!StringUtils.isEmpty(googleKey) && !StringUtils.isEmpty(tmSystemAccountInfo.getGoogleCode())) {
            throw new Exception("用户已绑定私钥");
        }

        if (StringUtils.isEmpty(googleKey) && StringUtils.isEmpty(tmSystemAccountInfo.getGoogleCode())) {
            throw new Exception("用户未绑定私钥");
        }

        if (!StringUtils.isEmpty(googleKey) && StringUtils.isEmpty(tmSystemAccountInfo.getGoogleCode())) {
            tmSystemAccountInfo.setGoogleCode(googleKey);
        }

        if (!GoogleAuthenticatorUtil.check_code(googleCode, System.currentTimeMillis(), tmSystemAccountInfo.getGoogleCode())) {
            throw new Exception("验证码验证失败");
        }

        if (!StringUtils.isEmpty(googleKey)) {
            tmsSystemAccountInfoRepository.save(tmSystemAccountInfo);
        }

        tmSystemAccountInfo.setIsLogin(TMSystemAccountInfo.LOGIN_ONE);
        tmsSystemAccountInfoRepository.save(tmSystemAccountInfo);

        String token = JwtUtil.sign(tmSystemAccountInfo.getDisplayName(), tmSystemAccountInfo.getSecurityPassword());
        SecurityUtils.getSubject().login(new JwtToken(token));
        resultMap.put("token", token);
        return resultMap;
    }


    /**
     * 登录接口
     */
    public Map<String, Object> login(String loginName, String loginPass) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        TMSystemAccountInfo tmSystemAccountInfo = tmsSystemAccountInfoRepository.findByEmail(loginName);
        if (ObjectUtils.isEmpty(tmSystemAccountInfo)) {
            throw new Exception("用户不存在，请先注册");
        }

        if (tmSystemAccountInfo.getStatu() == TMSystemAccountInfo.STATUS_ZERO) {
            throw new Exception("用户已停用");
        }

        if (tmSystemAccountInfo.getSecurityPassword().equals(PasswordUtil.MD5Salt(loginPass, tmSystemAccountInfo.getSalt()))) {
            resultMap.put("dictate", tmSystemAccountInfo.isBindGoogleCode());
            resultMap.put("userid", tmSystemAccountInfo.getUserId());
            return resultMap;
        } else {
            throw new Exception("密码错误");
        }

    }

    /**
     * 用户退出接口
     */
    public void loginOut() throws Exception {
        Subject subject = SecurityUtils.getSubject();
        TMSystemAccountInfo tmSystemAccountInfo = (TMSystemAccountInfo) subject.getPrincipal();
        tmSystemAccountInfo.setIsLogin(TMSystemAccountInfo.LOGIN_ZERO);
        tmsSystemAccountInfoRepository.save(tmSystemAccountInfo);
        subject.logout();
    }


    /**
     * 获取用户信息列表
     */
    public PageInfo accountList(String msg, Integer pageNum, Integer pageSize) {
        PageInfo pageInfo = new PageInfo();

        Pageable pageable = PageRequest.of(pageNum , pageSize, new Sort(Sort.Direction.DESC, "userId"));
        Specification<TMSystemAccountInfo> specification = new Specification<TMSystemAccountInfo>() {
            @Override
            public Predicate toPredicate(Root<TMSystemAccountInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.isEmpty(msg)) {
                   return null;
                }

                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicate = criteriaBuilder.like(root.get("displayName").as(String.class),"%" + msg + "%");
                    predicates.add(predicate);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("email").as(String.class),"%" + msg + "%");
                    predicates.add(predicateUser);
                }
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };

        Page<TMSystemAccountInfo> page = tmsSystemAccountInfoRepository.findAll(specification, pageable);
        if (ObjectUtils.isEmpty(page)) {
            return pageInfo;
        }

        List<AccountMsg> accountMsgList = new ArrayList<>();

        for (TMSystemAccountInfo tmSystemAccountInfo : page.getContent()) {
            AccountMsg accountMsg = new AccountMsg();
            accountMsg.setUserId(tmSystemAccountInfo.getUserId());
            accountMsg.setEmail(tmSystemAccountInfo.getEmail());
            accountMsg.setName(tmSystemAccountInfo.getDisplayName());
            accountMsg.setStatus(tmSystemAccountInfo.getStatu());
            accountMsgList.add(accountMsg);
        }
        pageInfo.setList(accountMsgList);
        pageInfo.setTotal(page.getTotalElements());
        return pageInfo;
    }

    /**
     * 编辑或者添加用户
     * 角色编辑暂时不写
     */
    public void accountAddOrEidt(AccountAddMsg accountAddMsg) throws Exception {
        TMSystemAccountInfo tmSystemAccountInfo = null;
        if (ObjectUtils.isEmpty(accountAddMsg.getUserId())) {
            //添加用户
            tmSystemAccountInfo = new TMSystemAccountInfo();
            //tmSystemAccountInfo.setGoogleCode("BJU76BK25JVVDXMY");
            tmSystemAccountInfo.setIsLogin(TMSystemAccountInfo.LOGIN_ZERO);
            tmSystemAccountInfo.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            tmSystemAccountInfo.setSalt(RandomUnit.generateRandom(6, RandomUnit.RANDOM_TWO));
            tmSystemAccountInfo.setStatu(TMSystemAccountInfo.STATUS_ONE);
            tmSystemAccountInfo.setSecurityPassword(PasswordUtil.MD5Salt(accountAddMsg.getPassword(), tmSystemAccountInfo.getSalt()));
        } else {
            //编辑用户
            tmSystemAccountInfo = tmsSystemAccountInfoRepository.findByUserId(accountAddMsg.getUserId());
            if (ObjectUtils.isEmpty(tmSystemAccountInfo)) {
                throw new Exception("编辑用户不存在");
            }
            if (!StringUtils.isEmpty(accountAddMsg.getPassword())) {
                tmSystemAccountInfo.setSalt(RandomUnit.generateRandom(6, RandomUnit.RANDOM_TWO));
                tmSystemAccountInfo.setSecurityPassword(PasswordUtil.MD5Salt(accountAddMsg.getPassword(), tmSystemAccountInfo.getSalt()));
            }
        }

        tmSystemAccountInfo.setEmail(accountAddMsg.getEmail());
        tmSystemAccountInfo.setDisplayName(accountAddMsg.getName());
        tmSystemAccountInfo.setModifyName(Timestamp.valueOf(LocalDateTime.now()));
        tmsSystemAccountInfoRepository.save(tmSystemAccountInfo);
    }

    /**
     * 停用用户
     */
    public void accountCease(Integer userId) throws Exception {
        TMSystemAccountInfo tmSystemAccountInfo = tmsSystemAccountInfoRepository.findByUserId(userId);
        if (ObjectUtils.isEmpty(tmSystemAccountInfo)) {
            throw new Exception("用户信息不存在");
        }

        if (tmSystemAccountInfo.getStatu() == TMSystemAccountInfo.STATUS_ZERO) {
            throw new Exception("用户已停用");
        }

        tmSystemAccountInfo.setStatu(TMSystemAccountInfo.STATUS_ZERO);
        tmsSystemAccountInfoRepository.save(tmSystemAccountInfo);
    }

    /**
     * 启用用户
     */
    public void accountOpen(Integer userId) throws Exception {
        TMSystemAccountInfo tmSystemAccountInfo = tmsSystemAccountInfoRepository.findByUserId(userId);
        if (ObjectUtils.isEmpty(tmSystemAccountInfo)) {
            throw new Exception("用户信息不存在");
        }

        if (tmSystemAccountInfo.getStatu() == TMSystemAccountInfo.STATUS_ONE) {
            throw new Exception("用户已启用");
        }

        tmSystemAccountInfo.setStatu(TMSystemAccountInfo.STATUS_ONE);
        tmsSystemAccountInfoRepository.save(tmSystemAccountInfo);
    }


    /**
     * 解除Google验证码
     */
    public void googleRemove(Integer userId) throws Exception {
        TMSystemAccountInfo tmSystemAccountInfo = tmsSystemAccountInfoRepository.findByUserId(userId);
        if (ObjectUtils.isEmpty(tmSystemAccountInfo)) {
            throw new Exception("用户信息不存在");
        }
        tmSystemAccountInfo.setGoogleCode(null);
        tmsSystemAccountInfoRepository.save(tmSystemAccountInfo);
    }

}

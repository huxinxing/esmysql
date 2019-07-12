package com.zhifan.monitor_manager.controller;

import com.zhifan.monitor_manager.domain.dto.parmary.AccountAddMsg;
import com.zhifan.monitor_manager.domain.dto.resultDto.ResponseResult;
import com.zhifan.monitor_manager.domain.enums.ResponseStatus;
import com.zhifan.monitor_manager.service.AccountService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequestMapping("/monitor/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @ApiOperation("获取Google验证码")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/gainGoogle",method = GET)
    public ResponseResult gainGoogle(
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),accountService.googleGain());
        }catch (Exception e){
            log.error("获取GoogleCode异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("验证Google验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "googleCode", value = "google验证吗", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "googleKey", value = "googleKey", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/verifyGoogle",method = GET)
    public ResponseResult verifyGoogle(
            @RequestParam(name = "googleCode") Long googleCode,
            @RequestParam(name = "googleKey",required = false) String googleKey,
            @RequestParam(name = "userId") Integer userId
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),accountService.googleVerify(googleCode,googleKey,userId));
        }catch (Exception e){
            log.error("验证GoogleCode异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginPass", value = "密码", required = true, dataType = "String", paramType = "query")

    })
    @RequestMapping(value = "/login",method = GET)
    public ResponseResult login(
            @RequestParam(name = "loginName") String loginName,
            @RequestParam(name = "loginPass") String loginPass
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),accountService.login(loginName,loginPass));
        }catch (Exception e){
            log.error("登录异常",e);
            e.printStackTrace();
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("用户退出")
    @RequestMapping(value = "/loginOut",method = GET)
    public ResponseResult loginOut(
    ){
        try{
            accountService.loginOut();
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),"注销成功");
        }catch (Exception e){
            log.error("退出失败",e);
            e.printStackTrace();
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMsgs", value = "查询条件", required = true, dataType = "string" , paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "string" , paramType = "query"),
            @ApiImplicitParam(name = "PageSize", value = "每页数量", required = true, dataType = "string" , paramType = "query")
    })
    @RequestMapping(value = "/accountList",method = GET)
    public ResponseResult accountList(
            @RequestParam(name = "searchMsgs",required = false) String searchMsgs,
            @RequestParam(name = "pageNum") Integer pageNum,
            @RequestParam(name = "pageSize") Integer pageSize
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),accountService.accountList(searchMsgs,pageNum,pageSize));
        }catch (Exception e){
            log.error("用户列表异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }


    @ApiOperation("添加或编辑用户")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/accountAddEdit",method = POST)
    public ResponseResult accountAdd(
            @RequestBody AccountAddMsg accountAddMsg
    ){
        try{
            if (ObjectUtils.isEmpty(accountAddMsg.getEmail())){
                throw new Exception("邮箱不能为空");
            }

            if (ObjectUtils.isEmpty(accountAddMsg.getName())){
                throw new Exception("名称不能为空");
            }

            if (ObjectUtils.isEmpty(accountAddMsg.getUserId()) && ObjectUtils.isEmpty(accountAddMsg.getPassword())){
                throw new Exception("添加用户密码不能为空");
            }
            accountService.accountAddOrEidt(accountAddMsg);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("添加用户异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("停止用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户名", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/accountCease",method = GET)
    public ResponseResult accountCease(
            @RequestParam(name = "userId") Integer userId
    ){
        try{
            accountService.accountCease(userId);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("停止用户异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("启用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户名", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/accountOpen",method = GET)
    public ResponseResult accountOpen(
            @RequestParam(name = "userId") Integer userId
    ){
        try{
            accountService.accountOpen(userId);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("启用用户异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("重置用户GA")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/resettingGA",method = GET)
    public ResponseResult resettingGA(
            @RequestParam(name = "userId") Integer userId
    ){
        try{
            accountService.googleRemove(userId);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("重置用户GA异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }


}

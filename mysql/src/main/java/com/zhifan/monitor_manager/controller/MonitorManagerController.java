package com.zhifan.monitor_manager.controller;

import com.zhifan.monitor_manager.domain.dto.parmary.RuleAddMsg;
import com.zhifan.monitor_manager.domain.dto.parmary.UserMonitorAddMsg;
import com.zhifan.monitor_manager.domain.dto.resultDto.ResponseResult;
import com.zhifan.monitor_manager.domain.enums.ResponseStatus;
import com.zhifan.monitor_manager.service.MonitorManagerService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequestMapping("/monitor/mmc")
public class MonitorManagerController {

    @Autowired
    MonitorManagerService monitorManagerService;

    @ApiOperation("监控系统，用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMsgs", value = "查询条件", required = true, dataType = "string" , paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "string" , paramType = "query"),
            @ApiImplicitParam(name = "PageSize", value = "每页数量", required = true, dataType = "string" , paramType = "query")
    })
    @RequestMapping(value = "/userList",method = GET)
    public ResponseResult userList(
            @RequestParam(name = "searchMsgs",required = false) String searchMsgs,
            @RequestParam(name = "pageNum") Integer pageNum,
            @RequestParam(name = "pageSize") Integer pageSize
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),monitorManagerService.userList(searchMsgs,pageNum,pageSize));
        }catch (Exception e){
            log.error("监控系统，用户列表异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("监控系统，用户添加或编辑")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/userAddEdit",method = POST)
    public ResponseResult userAddEdit(
            @RequestBody UserMonitorAddMsg userMonitorAddMsg
    ){
        try{
            if (StringUtils.isEmpty(userMonitorAddMsg.getEmail()) && StringUtils.isEmpty(userMonitorAddMsg.getMobile())){
                throw new Exception("参数错误");
            }
            monitorManagerService.userAddEdit(userMonitorAddMsg);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("监控系统，用户添加异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }


    @ApiOperation("监控系统，停止用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户名", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/userCease",method = GET)
    public ResponseResult userCease(
            @RequestParam(name = "id") Integer userId
    ){
        try{
            monitorManagerService.userCease(userId);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("监控系统，停止用户",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("监控系统，启用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户名", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/userOpen",method = GET)
    public ResponseResult userOpen(
            @RequestParam(name = "id") Integer userId
    ){
        try{
            monitorManagerService.userOpen(userId);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("监控系统，启用用户异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("监控系统，用户发送记录历史信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户名", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/record",method = GET)
    public ResponseResult record(
            @RequestParam(name = "id") Integer userId,
            @RequestParam(name = "pageNum") Integer pageNum,
            @RequestParam(name = "pageSize") Integer pageSize
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),monitorManagerService.recordList(userId,pageNum,pageSize));
        }catch (Exception e){
            log.error("监控系统，用户发送记录历史信息异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("监控系统，规则列表")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/ruleList",method = GET)
    public ResponseResult ruleList(
            @RequestParam(name = "searchMsgs",required = false) String searchMsgs,
            @RequestParam(name = "pageNum") Integer pageNum,
            @RequestParam(name = "pageSize") Integer pageSize
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),monitorManagerService.ruleList(searchMsgs,pageNum,pageSize));
        }catch (Exception e){
            log.error("监控系统，规则列表异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("监控系统，规则添加或编辑")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/ruleAddEdit",method = POST)
    public ResponseResult ruleAddEdit(
            @RequestBody RuleAddMsg ruleAddMsg
    ){
        try{
            monitorManagerService.ruleAddEdit(ruleAddMsg);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("监控系统，规则添加或编辑异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("监控系统，规则删除")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/ruledelete",method = GET)
    public ResponseResult ruledelete(
            @RequestParam(name = "id") Integer ruleId
    ){
        try{
            monitorManagerService.ruleDelete(ruleId);
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue());
        }catch (Exception e){
            log.error("监控系统，规则删除异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }


    @ApiOperation("监控系统，监控类型")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/monitorType",method = GET)
    public ResponseResult monitorType(
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),monitorManagerService.monitorType());
        }catch (Exception e){
            log.error("监控系统，监控类型异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("监控系统，币种阈值单位")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/monitorThresholdUnit",method = GET)
    public ResponseResult thresholdUnit(
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),monitorManagerService.monitorCoinType(2));
        }catch (Exception e){
            log.error("监控系统，币种阈值单位异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

    @ApiOperation("监控系统，币种编号")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/monitorCoinHash",method = GET)
    public ResponseResult monitorCoinHash(
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),monitorManagerService.monitorCoinType(1));
        }catch (Exception e){
            log.error("监控系统，币种异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }


    @ApiOperation("监控系统，监控通知方式")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/monitorNotifyType",method = GET)
    public ResponseResult monitorNotifyType(
    ){
        try{
            return new ResponseResult(ResponseStatus.SUCCESS.getKey(),ResponseStatus.SUCCESS.getValue(),monitorManagerService.monitorNotify());
        }catch (Exception e){
            log.error("监控系统，监控通知方式异常",e);
            return new ResponseResult(ResponseStatus.Default.getKey(),e.getMessage());
        }
    }

}

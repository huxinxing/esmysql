package com.zhifan.monitor_manager.service;

import com.github.pagehelper.PageInfo;
import com.zhifan.monitor_manager.domain.dto.parmary.RuleAddMsg;
import com.zhifan.monitor_manager.domain.dto.parmary.UserMonitorAddMsg;
import com.zhifan.monitor_manager.domain.dto.resultDto.RecordMsg;
import com.zhifan.monitor_manager.domain.dto.resultDto.RuleMsg;
import com.zhifan.monitor_manager.domain.dto.resultDto.UserMonitorMsg;
import com.zhifan.monitor_manager.domain.entity.TMCoinUnit;
import com.zhifan.monitor_manager.domain.entity.TMNotifyHistory;
import com.zhifan.monitor_manager.domain.entity.TMUserInfo;
import com.zhifan.monitor_manager.domain.entity.TMUserRule;
import com.zhifan.monitor_manager.domain.repository.TMCoinUnitRepository;
import com.zhifan.monitor_manager.domain.repository.TMNotifyHistoryRepository;
import com.zhifan.monitor_manager.domain.repository.TMUserInfoRepository;
import com.zhifan.monitor_manager.domain.repository.TMUserRuleRepository;
import com.zhifan.monitor_manager.util.DataUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonitorManagerService {

    @Autowired
    TMUserInfoRepository tmUserInfoRepository;

    @Autowired
    TMNotifyHistoryRepository tmNotifyHistoryRepository;

    @Autowired
    TMUserRuleRepository tmUserRuleRepository;

    @Autowired
    TMCoinUnitRepository tmCoinUnitRepository;


    /**
     * 获取监控系统用户信息列表
     * @param msg       搜索用户信息  id、手机号码、电子邮件、用户名 支持模糊查询
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return
     */
    public PageInfo userList(String msg, Integer pageNum, Integer pageSize) {
        PageInfo pageInfo = new PageInfo();

        Pageable pageable = PageRequest.of(pageNum, pageSize, new Sort(Sort.Direction.DESC, "createTime"));
        Specification<TMUserInfo> specification = new Specification<TMUserInfo>() {
            @Override
            public Predicate toPredicate(Root<TMUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isEmpty(msg)) {
                   return null;
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicate = criteriaBuilder.like(root.get("id").as(String.class), "%" + msg + "%");
                    predicates.add(predicate);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("mobile").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("email").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("userName").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        Page<TMUserInfo> page = tmUserInfoRepository.findAll(specification, pageable);
        if (ObjectUtils.isEmpty(page)) {
            return pageInfo;
        }
        List<UserMonitorMsg> userMonitorMsgList = new ArrayList<>();
        for (TMUserInfo tmUserInfo : page.getContent()) {
            UserMonitorMsg userMonitorMsg = new UserMonitorMsg();
            BeanUtils.copyProperties(tmUserInfo,userMonitorMsg);
            if (!ObjectUtils.isEmpty(userMonitorMsg.getCreateTime())){
                userMonitorMsg.setCreateTime(DataUtil.timeChangeType(String.valueOf(tmUserInfo.getCreateTime().getTime()),DataUtil.defautFormat,DataUtil.CHANGE_TYPE_TWO));
            }
            if (!ObjectUtils.isEmpty(userMonitorMsg.getModifyTime())){
                userMonitorMsg.setModifyTime(DataUtil.timeChangeType(String.valueOf(tmUserInfo.getModifyTime().getTime()),DataUtil.defautFormat,DataUtil.CHANGE_TYPE_TWO));
            }
            userMonitorMsgList.add(userMonitorMsg);
        }
        pageInfo.setList(userMonitorMsgList);
        pageInfo.setTotal(page.getTotalElements());
        return pageInfo;
    }


    /**
     * 编辑或者添加用户  若usderId存在则为修改，若不存在则为添加
     * @param userMonitorAddMsg   用户基本信息对象
     * @throws Exception
     */
    public void userAddEdit(UserMonitorAddMsg userMonitorAddMsg) throws Exception {
        TMUserInfo tmUserInfo = null;
        if (ObjectUtils.isEmpty(userMonitorAddMsg.getId())){
            tmUserInfo = new TMUserInfo();
            tmUserInfo.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            tmUserInfo.setIsEnable(TMUserInfo.IS_ENABLE_ONE);
        }else {
            tmUserInfo = tmUserInfoRepository.findById(userMonitorAddMsg.getId()).get();
            if (ObjectUtils.isEmpty(tmUserInfo)){
                throw new Exception("用户不存在");
            }
        }
        if (!StringUtils.isEmpty(userMonitorAddMsg.getUserName())){
            tmUserInfo.setUserName(userMonitorAddMsg.getUserName());
        }
        if (!StringUtils.isEmpty(userMonitorAddMsg.getMobile())){
            tmUserInfo.setMobile(userMonitorAddMsg.getMobile());
        }
        if (!StringUtils.isEmpty(userMonitorAddMsg.getEmail())){
            tmUserInfo.setEmail(userMonitorAddMsg.getEmail());
        }
        tmUserInfo.setModifyTime(Timestamp.valueOf(LocalDateTime.now()));
        tmUserInfoRepository.save(tmUserInfo);
    }

    /**
     * 启动用户
     * @param userId  用户Id
     * @throws Exception
     */
    public void userCease(Integer userId) throws Exception {
        TMUserInfo tmUserInfo =  tmUserInfoRepository.findById(userId).get();
        if (ObjectUtils.isEmpty(tmUserInfo)){
            throw new Exception("用户不存在");
        }
        tmUserInfo.setIsEnable(TMUserInfo.IS_ENABLE_ZERO);
        tmUserInfoRepository.save(tmUserInfo);
    }

    /**
     * 停用用户
     * @param userId   用户Id
     * @throws Exception
     */
    public void userOpen(Integer userId) throws Exception {
        TMUserInfo tmUserInfo =  tmUserInfoRepository.findById(userId).get();
        if (ObjectUtils.isEmpty(tmUserInfo)){
            throw new Exception("用户不存在");
        }
        tmUserInfo.setIsEnable(TMUserInfo.IS_ENABLE_ONE);
        tmUserInfoRepository.save(tmUserInfo);
    }


    /**
     * 获取监控系统用户信息列表
     * @param userId    用户Id
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return
     */
    public PageInfo recordList(Integer userId, Integer pageNum, Integer pageSize) {
        PageInfo pageInfo = new PageInfo();
        PageRequest pageRequest = PageRequest.of(pageNum,pageSize,new Sort(Sort.Direction.DESC,"userId"));
        Specification<TMNotifyHistory> specification = new Specification<TMNotifyHistory>() {
            @Override
            public Predicate toPredicate(Root<TMNotifyHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isEmpty(userId)) {
                    return null;
                }
                if (!StringUtils.isEmpty(userId)) {
                    Predicate predicate = criteriaBuilder.equal(root.get("userId").as(Integer.class), userId );
                    predicates.add(predicate);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
        Page<TMNotifyHistory> tmNotifyHistoryPage = tmNotifyHistoryRepository.findAll(specification,pageRequest);
        List<RecordMsg> recordMsgList = new ArrayList<>();
        for (TMNotifyHistory tmNotifyHistory : tmNotifyHistoryPage.getContent()){
            RecordMsg recordMsg = new RecordMsg();
            BeanUtils.copyProperties(tmNotifyHistory,recordMsg);
            if (!ObjectUtils.isEmpty(tmNotifyHistory.getModifyTime())){
                recordMsg.setModifyTime(DataUtil.timeChangeType(String.valueOf(tmNotifyHistory.getModifyTime().getTime()),DataUtil.defautFormat,DataUtil.CHANGE_TYPE_TWO));
            }
            if (!ObjectUtils.isEmpty(tmNotifyHistory.getCreateTime())){
                recordMsg.setCreateTime(DataUtil.timeChangeType(String.valueOf(tmNotifyHistory.getCreateTime().getTime()),DataUtil.defautFormat,DataUtil.CHANGE_TYPE_TWO));
            }
            recordMsgList.add(recordMsg);
        }
        pageInfo.setList(recordMsgList);
        pageInfo.setTotal(tmNotifyHistoryPage.getTotalElements());
        return pageInfo;
    }

    /**
     * 获取监控系统用户信息列表，支持规则名称、id、用户Id、币种Hash、阈值、阈值单位、地址标注模糊查找
     * @param msg      搜索条件
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return
     */
    public PageInfo ruleList(String msg, Integer pageNum, Integer pageSize) {
        PageInfo pageInfo = new PageInfo();

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, new Sort(Sort.Direction.DESC, "createTime"));
        Specification<TMUserRule> specification = new Specification<TMUserRule>() {
            @Override
            public Predicate toPredicate(Root<TMUserRule> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.isEmpty(msg)) {
                    return null;
                }

                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("ruleName").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicate = criteriaBuilder.like(root.get("id").as(String.class), "%" + msg + "%");
                    predicates.add(predicate);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("userId").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("coinHash").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("thresholdValue").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("thresholdUnit").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                if (!StringUtils.isEmpty(msg)) {
                    Predicate predicateUser = criteriaBuilder.like(root.get("addressTag").as(String.class), "%" + msg + "%");
                    predicates.add(predicateUser);
                }
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        Page<TMUserRule> page = tmUserRuleRepository.findAll(specification, pageRequest);
        if (ObjectUtils.isEmpty(page)) {
            return pageInfo;
        }
        List<RuleMsg> ruleMsgList = new ArrayList<>();
        for (TMUserRule tmUserRule : page.getContent()) {
            RuleMsg ruleMsg = new RuleMsg();
            BeanUtils.copyProperties(tmUserRule,ruleMsg);
            if (!ObjectUtils.isEmpty(ruleMsg.getCreateTime())){
                ruleMsg.setModifyTime(DataUtil.timeChangeType(String.valueOf(tmUserRule.getModifyTime().getTime()),DataUtil.defautFormat,DataUtil.CHANGE_TYPE_TWO));
            }
            if (!ObjectUtils.isEmpty(tmUserRule.getModifyTime())){
                ruleMsg.setCreateTime(DataUtil.timeChangeType(String.valueOf(tmUserRule.getCreateTime().getTime()),DataUtil.defautFormat,DataUtil.CHANGE_TYPE_TWO));
            }
            ruleMsgList.add(ruleMsg);
        }
        pageInfo.setList(ruleMsgList);
        pageInfo.setTotal(page.getTotalElements());
        return pageInfo;
    }

    /**
     * 用户规则添加或修改 id不存在时为添加  存在时为修改
     * @param ruleAddMsg
     * @throws Exception
     */
    public void ruleAddEdit(RuleAddMsg ruleAddMsg) throws Exception {
        TMUserRule tmUserRule = null;
        if (ObjectUtils.isEmpty(ruleAddMsg.getId())){
            tmUserRule = new TMUserRule();
            tmUserRule.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        }else {
            tmUserRule = tmUserRuleRepository.findById(ruleAddMsg.getId()).get();
            if (ObjectUtils.isEmpty(tmUserRule)){
                throw new Exception("规则不存在");
            }
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getAddressTag())){
            tmUserRule.setAddressTag(ruleAddMsg.getAddressTag());
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getCoinHash())){
            tmUserRule.setCoinHash(ruleAddMsg.getCoinHash());
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getNotifyType())){
            tmUserRule.setNotifyType(ruleAddMsg.getNotifyType());
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getOperationAddress())){
            tmUserRule.setOperationAddress(ruleAddMsg.getOperationAddress());
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getOperationType())){
            tmUserRule.setOperationType(ruleAddMsg.getOperationType());
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getRuleName())){
            tmUserRule.setRuleName(ruleAddMsg.getRuleName());
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getThresholdUnit())){
            tmUserRule.setThresholdUnit(ruleAddMsg.getThresholdUnit());
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getThresholdValue())){
            tmUserRule.setThresholdValue(ruleAddMsg.getThresholdValue());
        }
        if (!StringUtils.isEmpty(ruleAddMsg.getUserId())){
            tmUserRule.setUserId(ruleAddMsg.getUserId());
        }
        tmUserRule.setModifyTime(Timestamp.valueOf(LocalDateTime.now()));
        tmUserRuleRepository.save(tmUserRule);
    }

    public void ruleDelete(Integer ruleId) throws Exception {
        TMUserRule tmUserRule = tmUserRuleRepository.findById(ruleId).get();
        if (ObjectUtils.isEmpty(tmUserRule)){
            throw new Exception("规则不存在");
        }
        tmUserRuleRepository.delete(tmUserRule);
    }

    /**
     * 监控类型，key value值。1：大额交易监控  0：地址监控  （实际效果不确定，须找业务确定）
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> monitorType() throws Exception {
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 0; i < 2; i++){
            Map<String,String> map = new HashMap<>();
            map.put("key",String.valueOf(i));
            if (i == 1){
                map.put("value","大额交易监控");
            }else if (i == 0){
                map.put("value","地址监控");
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 币种编号和阈值单位的key、value的映射
     * @param type  类型：1、币种编号  2、阈值单位
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> monitorCoinType(Integer type) throws Exception {

        List<TMCoinUnit> tmCoinUnits = tmCoinUnitRepository.findAll();
        if (CollectionUtils.isEmpty(tmCoinUnits)){
            return null;
        }
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 0 ;i < tmCoinUnits.size(); i++){
            Map<String,String> map = new HashMap<>();
            if (type == 1) {  //币种编号
                map.put("value",tmCoinUnits.get(i).getMinUnit());
                map.put("key",tmCoinUnits.get(i).getCoinHash());
            }else if (type == 2){  //币种阈值单位
                map.put("key",tmCoinUnits.get(i).getMinUnit());
                map.put("value",tmCoinUnits.get(i).getMinUnit());
            }
            mapList.add(map);
        }

        return mapList;
    }

    /**
     * 监控通知类型 1、邮件 2、短信 3、邮件，短信。
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> monitorNotify() throws Exception {
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 1; i < 4; i++){
            Map<String,String> map = new HashMap<>();
            map.put("key",String.valueOf(i));
            if (i == 2){
                map.put("value","短信");
            }else if (i == 1){
                map.put("value","邮件");
            }else if (i == 3){
                map.put("value","邮件、短信");
            }
            mapList.add(map);
        }
        return mapList;
    }
}

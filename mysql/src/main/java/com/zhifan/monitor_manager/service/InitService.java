package com.zhifan.monitor_manager.service;

import com.zhifan.monitor_manager.domain.dto.parmary.AccountAddMsg;
import com.zhifan.monitor_manager.domain.entity.TMNotifyHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class InitService {



    @Autowired
    AccountService accountService;

    public void initData() {
        //initAccount();
    }

    public void initAccount(){

        for (int i = 0; i < 1; i++){
            AccountAddMsg accountAddMsg = new AccountAddMsg();
            accountAddMsg.setEmail("admin");
            accountAddMsg.setName("admin");
            accountAddMsg.setPassword("admin");
            try {
                accountService.accountAddOrEidt(accountAddMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void initHistoryRecod(){
        for (int i = 0; i < 10; i++){
            TMNotifyHistory tmNotifyHistory = new TMNotifyHistory();
            tmNotifyHistory.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            tmNotifyHistory.setModifyTime(Timestamp.valueOf(LocalDateTime.now()));
            tmNotifyHistory.setNotifyContent("");
        }
    }




}

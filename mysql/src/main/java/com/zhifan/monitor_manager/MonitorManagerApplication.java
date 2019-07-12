package com.zhifan.monitor_manager;

import com.zhifan.monitor_manager.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonitorManagerApplication implements CommandLineRunner {

    @Autowired
    InitService initService;

    public static void main(String[] args) {
        SpringApplication.run(MonitorManagerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initService.initData();
    }
}

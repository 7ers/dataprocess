package com.hsmy.dataprocess.controller;

import com.hsmy.dataprocess.service.SendLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Statistics {
    @Resource
    SendLogService sendLogService;

    @GetMapping("/querybyday")
    public Object querybyday() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", "0");
        result.put("data", sendLogService.selectByDay());

        return result;
    }

    @GetMapping("/querybyhour")
    public Object querybyhour() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", "0");
        result.put("data", sendLogService.selectByHour());

        return result;
    }
}

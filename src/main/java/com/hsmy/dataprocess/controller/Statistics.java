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

    @GetMapping("/querybyhour")
    public Object querybyhour(int page) {
        Map<String, Object> result = new HashMap<>();

        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("logs", sendLogService.selectPageByHour(page));
        dataMap.put("pages", sendLogService.countPagesByHour());

        result.put("code", "0");
        result.put("data", dataMap);

        return result;
    }

    @GetMapping("/querybyday")
    public Object querybyday(int page) {
        Map<String, Object> result = new HashMap<>();

        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("logs", sendLogService.selectPageByDay(page));
        dataMap.put("pages", sendLogService.countPagesByDay());

        result.put("code", "0");
        result.put("data", dataMap);

        return result;
    }
}

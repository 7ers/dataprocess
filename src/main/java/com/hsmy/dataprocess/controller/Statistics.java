package com.hsmy.dataprocess.controller;

import com.hsmy.dataprocess.pojo.ReceiveStat;
import com.hsmy.dataprocess.pojo.SendLog;
import com.hsmy.dataprocess.service.ReceiveStatService;
import com.hsmy.dataprocess.service.SendLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class Statistics {
    @Resource
    SendLogService sendLogService;

    @Resource
    ReceiveStatService receiveStatService;

    @GetMapping("querybyhour")
    public Object querybyhour(int page) {
        Map<String, Object> result = new HashMap<>();

        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("logs", sendLogService.selectPageByHour(page));
        dataMap.put("pages", sendLogService.countPagesByHour());

        result.put("code", "0");
        result.put("data", dataMap);

        return result;
    }

    @GetMapping("querybyday")
    public Object querybyday(int page) {
        Map<String, Object> result = new HashMap<>();

        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("logs", sendLogService.selectPageByDay(page));
        dataMap.put("pages", sendLogService.countPagesByDay());

        result.put("code", "0");
        result.put("data", dataMap);

        return result;
    }

    @GetMapping("querybyweek")
    public Object querybyweek() {
        Map<String, Object> result = new HashMap<>();

        List<SendLog> sendLogs = sendLogService.selectByWeek();
        Collections.reverse(sendLogs);
        List<Integer> totalCount = new ArrayList<>();
        for (SendLog sendLog : sendLogs) {
            totalCount.add(sendLog.getScount());
        }

        List<ReceiveStat> receiveStats = receiveStatService.selectByWeek();
        Collections.reverse(receiveStats);
        List<Integer> ipCount = new ArrayList<>();
        List<Integer> idfaCount = new ArrayList<>();
        for (ReceiveStat receiveStat : receiveStats) {
            ipCount.add(receiveStat.getStatCount().intValue());
            idfaCount.add(receiveStat.getIdfaCount().intValue());
        }

        String[] weekArray = {"周日","周一","周二","周三","周四","周五","周六"};
        Calendar cal = Calendar.getInstance();
        int weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 2;
        if(weekIndex < 0){
            weekIndex = 6;
        }
        List<String> weeks = new ArrayList<>();

        for (int i = weekIndex + 1; i <= 6; i++)
            weeks.add(weekArray[i]);

        for (int i = 0; i <= weekIndex; i++)
            weeks.add(weekArray[i]);

        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("totalCount", totalCount);
        dataMap.put("ipCount", ipCount);
        dataMap.put("idfaCount", idfaCount);
        dataMap.put("weeks", weeks);

        result.put("code", "0");
        result.put("data", dataMap);

        return result;
    }

    @GetMapping("queryip")
    public Object queryip(int page) {
        Map<String, Object> result = new HashMap<>();

        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("stats", receiveStatService.selectPageByDay(page));
        dataMap.put("pages", receiveStatService.countPagesByDay());

        result.put("code", "0");
        result.put("data", dataMap);

        return result;
    }
}

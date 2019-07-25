package com.hsmy.dataprocess.service;

import com.hsmy.dataprocess.pojo.SendLog;

import java.util.List;

public interface SendLogService {
    void record(SendLog sendLog);

    boolean isDuplicate(String filename);

    int countPagesByHour();

    List<SendLog> selectPageByHour(int page);

    int countPagesByDay();

    List<SendLog> selectPageByDay(int page);
}

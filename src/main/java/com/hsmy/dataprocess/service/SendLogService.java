package com.hsmy.dataprocess.service;

import com.hsmy.dataprocess.pojo.SendLog;

import java.util.List;

public interface SendLogService {
    void record(int sCount, int fCount, String filename, int readbyte, int totalbyte);

    boolean isDuplicate(String filename);

    int countPagesByHour();

    List<SendLog> selectPageByHour(int page);

    int countPagesByDay();

    List<SendLog> selectPageByDay(int page);
}

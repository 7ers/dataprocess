package com.hsmy.dataprocess.service;

import com.hsmy.dataprocess.pojo.SendLog;

import java.util.List;

public interface SendLogService {
    void record(int sCount, int fCount, String filename);

    boolean isDuplicate(String filename);

    List<SendLog> selectByDay();

    List<SendLog> selectByHour();
}

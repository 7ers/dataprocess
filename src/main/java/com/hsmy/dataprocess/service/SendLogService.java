package com.hsmy.dataprocess.service;

import com.hsmy.dataprocess.pojo.SendLog;

import java.util.List;

public interface SendLogService {
    public void record(int sCount, int fCount);
    public List<SendLog> selectAll();
}

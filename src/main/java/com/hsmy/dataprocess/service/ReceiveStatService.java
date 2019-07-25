package com.hsmy.dataprocess.service;

import com.hsmy.dataprocess.pojo.ReceiveStat;

import java.util.List;

public interface ReceiveStatService {
    void record(ReceiveStat receiveStat);

    int countPagesByDay();

    List<ReceiveStat> selectPageByDay(int page);
}

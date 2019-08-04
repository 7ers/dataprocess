package com.hsmy.dataprocess.service;

import com.hsmy.dataprocess.pojo.ReceiveStat;

import java.util.List;

public interface ReceiveStatService {
    int countPagesByDay();

    List<ReceiveStat> selectPageByDay(int page);

    List<ReceiveStat> selectByWeek();
}

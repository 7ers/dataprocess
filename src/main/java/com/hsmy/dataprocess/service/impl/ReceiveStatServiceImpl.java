package com.hsmy.dataprocess.service.impl;

import com.hsmy.dataprocess.dao.ReceiveStatMapper;
import com.hsmy.dataprocess.pojo.ReceiveStat;
import com.hsmy.dataprocess.service.ReceiveStatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReceiveStatServiceImpl implements ReceiveStatService {
    @Resource
    ReceiveStatMapper receiveStatMapper;

    @Override
    public int countPagesByDay() {
        return receiveStatMapper.countPagesByDay();
    }

    @Override
    public List<ReceiveStat> selectPageByDay(int page) {
        return receiveStatMapper.selectPageByDay(page);
    }

    @Override
    public List<ReceiveStat> selectByWeek() {
        return receiveStatMapper.selectByWeek();
    }
}

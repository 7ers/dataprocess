package com.hsmy.dataprocess.dao;

import com.hsmy.dataprocess.pojo.ReceiveStat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReceiveStatMapper {
    int countPagesByDay();

    List<ReceiveStat> selectPageByDay(int page);
}
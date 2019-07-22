package com.hsmy.dataprocess.dao;

import com.hsmy.dataprocess.pojo.SendLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SendLogMapper {
    int insertSelective(SendLog record);

    int countByFileName(String filename);

    int countPagesByHour();

    List<SendLog> selectPageByHour(int page);

    int countPagesByDay();

    List<SendLog> selectPageByDay(int page);
}
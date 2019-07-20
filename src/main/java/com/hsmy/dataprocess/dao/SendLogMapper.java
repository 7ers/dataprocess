package com.hsmy.dataprocess.dao;

import com.hsmy.dataprocess.pojo.SendLog;

import java.util.List;

public interface SendLogMapper {
    int insertSelective(SendLog record);

    List<SendLog> selectByDay();

    List<SendLog> selectByHour();

    int countByFileName(String filename);
}
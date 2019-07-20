package com.hsmy.dataprocess.dao;

import com.hsmy.dataprocess.pojo.SendLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SendLogMapper {
    int insertSelective(SendLog record);

    List<SendLog> selectByDay();

    List<SendLog> selectByHour();

    int countByFileName(String filename);
}
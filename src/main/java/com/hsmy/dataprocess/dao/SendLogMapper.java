package com.hsmy.dataprocess.dao;

import com.hsmy.dataprocess.pojo.SendLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SendLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SendLog record);

    int insertSelective(SendLog record);

    SendLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SendLog record);

    int updateByPrimaryKey(SendLog record);

    List<SendLog> selectAll();
}
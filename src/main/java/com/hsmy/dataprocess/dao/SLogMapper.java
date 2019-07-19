package com.hsmy.dataprocess.dao;

import com.hsmy.dataprocess.pojo.SLog;

public interface SLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SLog record);

    int insertSelective(SLog record);

    SLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SLog record);

    int updateByPrimaryKey(SLog record);
}
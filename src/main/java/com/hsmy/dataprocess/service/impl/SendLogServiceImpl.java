package com.hsmy.dataprocess.service.impl;

import com.hsmy.dataprocess.dao.SendLogMapper;
import com.hsmy.dataprocess.pojo.SendLog;
import com.hsmy.dataprocess.service.SendLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SendLogServiceImpl implements SendLogService {
    @Resource
    private SendLogMapper sendLogMapper;

    @Override
    public void record(int sCount, int fCount, String filename) {
        SendLog sendLog = new SendLog();
        sendLog.setScount(sCount);
        sendLog.setFcount(fCount);
        sendLog.setFilename(filename);
        sendLogMapper.insertSelective(sendLog);
    }

    @Override
    public boolean isDuplicate(String filename) {
        return sendLogMapper.countByFileName(filename) > 0;
    }

    @Override
    public List<SendLog> selectByDay() {
        return sendLogMapper.selectByDay();
    }

    @Override
    public List<SendLog> selectByHour() {
        return sendLogMapper.selectByHour();
    }
}

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
    public void record(int sCount, int fCount) {
        SendLog sendLog = new SendLog();
        sendLog.setScount(sCount);
        sendLog.setFcount(fCount);
        sendLogMapper.insertSelective(sendLog);
    }

    @Override
    public List<SendLog> selectAll() {
        return sendLogMapper.selectAll();
    }
}

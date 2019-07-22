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
    public void record(int sCount, int fCount, String filename, int readbyte, int totalbyte) {
        SendLog sendLog = new SendLog();
        sendLog.setScount(sCount);
        sendLog.setFcount(fCount);
        sendLog.setFilename(filename);
        sendLog.setReadbyte(readbyte);
        sendLog.setTotalbyte(totalbyte);
        sendLogMapper.insertSelective(sendLog);
    }

    @Override
    public boolean isDuplicate(String filename) {
        return sendLogMapper.countByFileName(filename) > 0;
    }

    @Override
    public int countPagesByHour() {
        return sendLogMapper.countPagesByHour();
    }

    @Override
    public List<SendLog> selectPageByHour(int page) {
        return sendLogMapper.selectPageByHour(page);
    }

    @Override
    public int countPagesByDay() {
        return sendLogMapper.countPagesByDay();
    }

    @Override
    public List<SendLog> selectPageByDay(int page) {
        return sendLogMapper.selectPageByDay(page);
    }
}

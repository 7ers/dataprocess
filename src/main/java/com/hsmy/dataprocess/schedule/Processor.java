package com.hsmy.dataprocess.schedule;

import com.hsmy.dataprocess.pojo.ReceiveStat;
import com.hsmy.dataprocess.pojo.SendLog;
import com.hsmy.dataprocess.service.ReceiveStatService;
import com.hsmy.dataprocess.service.SendLogService;
import com.hsmy.dataprocess.service.TransferDataService;
import com.hsmy.dataprocess.tools.BufferedRandomAccessFile;
import com.hsmy.dataprocess.tools.DesECBUti;
import com.hsmy.dataprocess.tools.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Configuration
@EnableScheduling
public class Processor {
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    @Value("${sending.url}")
    private String url;
    @Value("${sending.group_count}")
    private int group_count;
    @Value("${sending.encoding}")
    private String encoding;
    @Value("${sending.timeout}")
    private int timeout;
    @Value("${sending.key}")
    private String key;

    @Value("${saving.src_path}")
    private String src_path;
    @Value("${saving.his_path}")
    private String his_path;

    private final String FORMAT = "{\"ip\":\"%s\",\"ua\":\"%s\"},";

    @Resource
    TransferDataService transferDataService;

    @Resource
    SendLogService sendLogService;

    @Resource
    ReceiveStatService receiveStatService;

    @Scheduled(fixedDelay=10000)
    private void process() {
        long startTime = System.currentTimeMillis();

        int sCount = 0, fCount = 0, vcount = 0, ivcount = 0;
        int totalbyte = 0;

        File folder = new File(src_path);
        if (folder.exists()) {
            String[] fileArray = folder.list();
            if (fileArray == null || fileArray.length == 0)
                return;

            List fileList = Arrays.asList(fileArray);
            Collections.sort(fileList, new Comparator<String>() {
                @Override
                public int compare(String f1, String f2) {
                    return f1.compareTo(f2);
                }
            });

            String filename = fileList.get(fileList.size()-1).toString();
            if (filename.charAt(0) == '.')
                return;

//            if (new File(src_path + filename).isDirectory())
//                return;

            BufferedRandomAccessFile reader = null;
            try {
                reader = new BufferedRandomAccessFile(src_path + filename, "r");
                reader.seek(0);
                totalbyte = (int)reader.length();

                int count = 0;
                boolean didReadEOF = false;
                StringBuilder records = new StringBuilder();
                records.append("[");

                while (true) {
                    String line = reader.readLine();
                    if (StringUtils.isEmpty(line)) {
                        didReadEOF = true;
                    } else {
                        String[] record = line.split("\\|");
                        if (!StringUtils.isValidRecord(record)) {
                            ++ivcount;
                            continue;
                        }

                        records.append(String.format(FORMAT, record[0], record[2]));
                        ++count;
                        ++vcount;
                    }

                    if(count == group_count || (didReadEOF && records.length() > 0)){
                        records.deleteCharAt(records.length() - 1);
                        records.append("]");
                        String sendContent = DesECBUti.encryptDES(records.toString(), key);

                        int ret = transferDataService.sendPostDataByJson(url, sendContent,encoding);
                        if (ret == HttpStatus.SC_OK) {
                            sCount += count;
                        } else {
                            fCount += count;
                        }

                        count = 0;
                        records.setLength(0);
                        records.append("[");
                    }

                    if (didReadEOF || System.currentTimeMillis() - startTime >= timeout) {
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                SendLog sendLog = new SendLog();
                sendLog.setScount(sCount);
                sendLog.setFcount(fCount);
                sendLog.setFilename(filename);
                sendLog.setReadbyte((int)reader.getFilePointer());
                sendLog.setTotalbyte(totalbyte);
                sendLog.setVcount(vcount);
                sendLog.setIvcount(ivcount);
                sendLog.setStarttime(new Date(new Timestamp(startTime).getTime()));
                sendLogService.record(sendLog);

                IOUtils.closeQuietly(reader);

                String hisFolderPath = his_path + filename.substring(0, 8) + "/";
                File hisFoler = new File(hisFolderPath);
                if (!hisFoler.exists()) {
                    hisFoler.mkdirs();
                }
                File currentFile = new File(src_path + filename);
                if(!currentFile.renameTo(new File(hisFolderPath + filename))){
                    logger.error("backup failed.");
                }
            }
        }
    }


    @Scheduled(cron = "0 35 5 * * ?")
    private void statistics() {
        long startTime = System.currentTimeMillis();

        long count = 0;
        byte[][][][] ips = new byte[256][256][256][256];

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);

        String folderPath = his_path + new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()) + "/";
        File folder = new File(folderPath);
        String[] fileArray = folder.list();
        if (fileArray == null || fileArray.length == 0)
            return;

        for (int i = 0; i < fileArray.length; i++) {
            String filename = fileArray[i];
            BufferedRandomAccessFile reader = null;
            try {
                reader = new BufferedRandomAccessFile(folderPath + filename, "r");
                reader.seek(0);

                while (true) {
                    String line = reader.readLine();
                    if (StringUtils.isEmpty(line)) {
                        break;
                    } else {
                        String[] record = line.split("\\|");
                        if (!StringUtils.isValidRecord(record)) {
                            continue;
                        }

                        String[] ip = record[0].split("\\.");
                        int a = Integer.parseInt(ip[0]);
                        int b = Integer.parseInt(ip[1]);
                        int c = Integer.parseInt(ip[2]);
                        int d = Integer.parseInt(ip[3]);
                        if (ips[a][b][c][d] == 0) {
                            ips[a][b][c][d] = 1;
                            ++count;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }

        ReceiveStat receiveStat = new ReceiveStat();
        receiveStat.setStatCount(count);
        receiveStat.setDuration(System.currentTimeMillis() - startTime);
        receiveStat.setStatDate(calendar.getTime());
        receiveStatService.record(receiveStat);
    }
}

package com.hsmy.dataprocess.schedule;

import com.hsmy.dataprocess.service.SendLogService;
import com.hsmy.dataprocess.service.TransferDataService;
import com.hsmy.dataprocess.tools.BufferedRandomAccessFile;
import com.hsmy.dataprocess.tools.DesECBUti;
import org.apache.http.HttpStatus;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    @Scheduled(fixedDelay=10000)
    private void process() {
        long startTime = System.currentTimeMillis();
        int sCount = 0, fCount = 0;
        int totalbyte = 0;

        File folder = new File(src_path);
        if (!folder.exists()) {
            folder.mkdirs();
        } else {
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
            BufferedRandomAccessFile reader = null;
            try {
                if (sendLogService.isDuplicate(filename))
                    return;

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
                        records.append(String.format(FORMAT, record[0], record[2]));

                        ++count;
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
                IOUtils.closeQuietly(reader);

                File currentFile = new File(src_path + filename);
                if(!currentFile.renameTo(new File(his_path + filename))){
                    logger.error("backup failed.");
                }

                if(sCount > 0 || fCount > 0)
                    sendLogService.record(sCount, fCount, filename, (int)reader.getFilePointer(), totalbyte);
            }
        }
    }
}

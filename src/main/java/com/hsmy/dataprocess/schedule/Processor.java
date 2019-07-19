package com.hsmy.dataprocess.schedule;

import com.hsmy.dataprocess.service.SendLogService;
import com.hsmy.dataprocess.service.TransferDataService;
import com.hsmy.dataprocess.tools.BufferedRandomAccessFile;
import com.hsmy.dataprocess.tools.DesECBUti;
import org.apache.http.HttpStatus;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@Configuration
@EnableScheduling
public class Processor {
    private final String URL = "http://www.798joy.com:1080/recv";

    private final String FOLDER_PATH = "/Users/lahm/Desktop/data/";
    private final String HISTORY_PATH = "/Users/lahm/Desktop/history/";

    private final String FORMAT = "{\"ip\":\"%s\",\"ua\":\"%s\"},";
    private final String KEY = "3fa6f09b";

    private final int GROUP_COUNT = 10000;

    @Resource
    TransferDataService transferDataService;

    @Resource
    SendLogService sendLogService;

    @Scheduled(fixedDelay=1000000)
    private void process() {
        int sCount = 0, fCount = 0;
        File folder = new File(FOLDER_PATH);
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
                reader = new BufferedRandomAccessFile(FOLDER_PATH + filename, "r");
                reader.seek(0);
                int count = 0;
                boolean didReadEnd = false;
                StringBuilder records = new StringBuilder();
                records.append("[");
                while (true) {
                    String line = reader.readLine();
                    if (StringUtils.isEmpty(line)) {
                        didReadEnd = true;
                    } else {
                        String[] record = line.split("\\|");
                        records.append(String.format(FORMAT, record[0], record[2]));

                        ++count;
                    }

                    if(count == GROUP_COUNT || (didReadEnd && records.length() > 0)){
                        records.deleteCharAt(records.length() - 1);
                        records.append("]");
                        String sendContent = DesECBUti.encryptDES(records.toString(), KEY);

                        int ret = transferDataService.sendPostDataByJson(URL, sendContent,"utf-8");
                        if (ret == HttpStatus.SC_OK) {
                            sCount += count;
                        } else {
                            fCount += count;
                        }

                        count = 0;
                        records.setLength(0);
                        records.append("[");
                    }

                    if (didReadEnd) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(reader);

                File currentFile = new File(FOLDER_PATH + filename);
                currentFile.renameTo(new File(HISTORY_PATH + filename));

                sendLogService.record(sCount, fCount);
            }
        }
    }
}

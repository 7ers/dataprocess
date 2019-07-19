package com.hsmy.dataprocess.schedule;

import com.hsmy.dataprocess.tools.BufferedRandomAccessFile;
import com.hsmy.dataprocess.tools.DesECBUti;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Configuration
@EnableScheduling
public class Processor {
    private String folderPath = "/Users/lahm/Desktop/data/";

    private final String FORMAT = "{\"ip\":\"%s\",\"ua\":\"%s\"},";
    private final String KEY = "3fa6f09b";

    @Scheduled(fixedDelay=10000)
    private void process() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        } else {
            String[] fileList = folder.list();
            if (fileList != null && fileList.length > 0) {
                String filePath = fileList[fileList.length-1];
                BufferedRandomAccessFile reader = null;
                try {
                    reader = new BufferedRandomAccessFile(folderPath + filePath, "r");
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

                        if((count == 10000 || didReadEnd) && records.length() > 0){
                            records.deleteCharAt(records.length() - 1);
                            records.append("]");

                            String sendContent = DesECBUti.encryptDES(records.toString(), KEY);
                            String ret = sendPostDataByJson("http://www.798joy.com:1080/recv", sendContent,"utf-8");

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
                }
            }
        }
    }

    public String sendPostDataByJson(String url, String json, String encoding) throws ClientProtocolException, IOException {
        String result = "";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        stringEntity.setContentEncoding("utf-8");
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpPost);
        }catch (ClientProtocolException cpe){
            cpe.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        int statusCode = HttpStatus.SC_REQUEST_TIMEOUT;
        if (response != null) {
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            response.close();
        }

        return String.valueOf(statusCode);
    }
}

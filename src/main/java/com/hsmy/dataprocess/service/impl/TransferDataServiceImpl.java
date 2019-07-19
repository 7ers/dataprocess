package com.hsmy.dataprocess.service.impl;

import com.hsmy.dataprocess.service.TransferDataService;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TransferDataServiceImpl implements TransferDataService {
    @Override
    public int sendPostDataByJson(String url, String json, String encoding) throws ClientProtocolException, IOException {
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
            response.close();
        }

        return statusCode;
    }
}

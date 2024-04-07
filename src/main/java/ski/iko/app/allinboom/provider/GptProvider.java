package ski.iko.app.allinboom.provider;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.alibaba.fastjson2.JSON;

import ski.iko.app.allinboom.controller.GptRequest;
import ski.iko.app.allinboom.controller.Message;

@Service(GptProvider.PROVIDER_TYPE)
@Scope("prototype")
public class GptProvider extends AbstractProvider {
    public static final String PROVIDER_TYPE = "GptProvider";
    private static final String DEFAULT_API_URL = "https://api.openai.com/v1/chat/completions";
    @Autowired
    private HttpClientBuilder httpClientBuilder;

    @Override
    public String defaultApiUrl() {
        return DEFAULT_API_URL;
    }

    @Override
    public SseEmitter stream(List<Message> messages, String model) {
        SseEmitter sseEmitter = new SseEmitter();
        new Thread(() -> {
            try {
                CloseableHttpClient httpclient = httpClientBuilder.build();
                HttpPost httpPost = buildHttpPost(model, messages, true);
                try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                    InputStream inputStream = response.getEntity().getContent();
                    byte[] bytes = new byte[1024];
                    String readString = new String(bytes, StandardCharsets.UTF_8);
                    while (inputStream.read(bytes) > 0) {
                        sseEmitter.send(bytes);
                    }
                    sseEmitter.complete();
                }
            } catch (Exception e) {
                throw new RuntimeException("请求[" + getName() + "]异常", e);
            }
        }).start();
        return sseEmitter;
    }

    @Override
    public String normal(List<Message> messages, String model) {
        try {
            CloseableHttpClient httpclient = httpClientBuilder.build();
            HttpPost httpPost = buildHttpPost(model, messages, false);
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                if (response.getCode() != 200) {
                    throw new RuntimeException("请求[" + getName() + "]响应状态码异常,状态码为[" + response.getCode() + "]");
                }
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "UTF-8").trim();
            }
        } catch (Exception e) {
            throw new RuntimeException("请求[" + getName() + "]异常", e);
        }
    }

    private HttpPost buildHttpPost(String model,List<Message> messages,boolean stream) throws Exception{
        HttpPost httpPost = new HttpPost(new URI(requestUrl()));
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + getKey());
        GptRequest gptRequest = new GptRequest();
        gptRequest.setModel(model);
        gptRequest.setMessages(messages);
        gptRequest.setStream(stream);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(gptRequest), StandardCharsets.UTF_8));
        return httpPost;
    }
}

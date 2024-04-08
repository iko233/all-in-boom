package ski.iko.app.allinboom.provider;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import ski.iko.app.allinboom.controller.Message;
import ski.iko.app.allinboom.util.GptResponseBuilderUtil;

@Service(GeminiProvider.PROVIDER_TYPE)
@Scope("prototype")
public class GeminiProvider extends AbstractProvider {
    public static final String PROVIDER_TYPE = "GeminiProvider";
    private static final String DEFAULT_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    @Autowired
    private HttpClientBuilder httpClientBuilder;


    @Override
    String defaultApiUrl() {
        return DEFAULT_API_URL;
    }

    @Override
    public SseEmitter stream(List<Message> messages, String model) {
        SseEmitter sseEmitter = new SseEmitter();
        try {
            String text = requestGemini(messages);
            sseEmitter.send(GptResponseBuilderUtil.stream(text));
            sseEmitter.send(GptResponseBuilderUtil.streamEnd());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        sseEmitter.complete();
        return sseEmitter;
    }

    @Override
    public String normal(List<Message> messages, String model) {
        try {
            return GptResponseBuilderUtil.normal(requestGemini(messages));
        } catch (Exception e) {
            throw new RuntimeException("请求[" + getName() + "]异常", e);
        }
    }

    private String requestGemini(List<Message> messages) throws URISyntaxException, IOException, ParseException {
        CloseableHttpClient httpclient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(new URI(requestUrl() + "?key=" + getKey()));
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        GeminiRequest geminiRequest = new GeminiRequest();
        geminiRequest.setContents(new ArrayList<>());
        if (!messages.isEmpty() && !messages.get(0).getRole().equals("user")) {
            GeminiContent geminiContent = new GeminiContent();
            geminiContent.setRole("user");
            geminiContent.setParts(new GeminiPart());
            geminiContent.getParts().setText("");
            geminiRequest.getContents().add(geminiContent);
        }
        for (Message message : messages) {
            GeminiContent geminiContent = new GeminiContent();
            geminiContent.setRole("user".equals(message.getRole())?message.getRole():"model");
            geminiContent.setParts(new GeminiPart());
            geminiContent.getParts().setText(message.getContent());
            geminiRequest.getContents().add(geminiContent);
        }
        httpPost.setEntity(new StringEntity(JSON.toJSONString(geminiRequest), StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            String jsonString = EntityUtils.toString(entity, "UTF-8");
            if (response.getCode() != 200) {
                throw new RuntimeException(
                    "请求[" + getName() + "]响应状态码异常,状态码为[" + response.getCode() + "],返回信息如下:"+jsonString);
            }
            JSONObject jsonObject = JSON.parseObject(jsonString);
            String responseDate = jsonObject.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
            return responseDate;
        }
    }
}

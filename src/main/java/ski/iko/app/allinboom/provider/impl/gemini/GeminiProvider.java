package ski.iko.app.allinboom.provider.impl.gemini;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import ski.iko.app.allinboom.controller.Message;
import ski.iko.app.allinboom.provider.HttpAbstractProvider;
import ski.iko.app.allinboom.util.GptResponseBuilderUtil;

@Service(GeminiProvider.PROVIDER_TYPE)
@Scope("prototype")
public class GeminiProvider extends HttpAbstractProvider {
    public static final String PROVIDER_TYPE = "GeminiProvider";

    @Override
    public String defaultApiUrl() {
        return "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    }

    @Override
    public SseEmitter stream(List<Message> messages, String model) {
        SseEmitter sseEmitter = new SseEmitter();
        try {
            String text = requestGemini(messages);
            sseEmitter.send(GptResponseBuilderUtil.stream(text));
            sseEmitter.send(GptResponseBuilderUtil.streamEnd());
            sseEmitter.complete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private HttpPost buildHttpPost(List<Message> messages) {
        HttpPost httpPost;
        try {
            httpPost = new HttpPost(new URI(this.requestUrl() + "?key=" + getKey()));
        } catch (URISyntaxException e) {
            throw new RuntimeException("解析URL[" + requestUrl() + "]错误", e);
        }
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        GeminiRequest geminiRequest = new GeminiRequest();
        geminiRequest.setContents(new ArrayList<>());
        if (!messages.isEmpty() && !messages.getFirst().getRole().equals("user")) {
            GeminiContent geminiContent = new GeminiContent();
            geminiContent.setRole("user");
            geminiContent.setParts(new GeminiPart());
            geminiContent.getParts().setText("");
            geminiRequest.getContents().add(geminiContent);
        }
        for (Message message : messages) {
            GeminiContent geminiContent = new GeminiContent();
            geminiContent.setRole("user".equals(message.getRole()) ? message.getRole() : "model");
            geminiContent.setParts(new GeminiPart());
            geminiContent.getParts().setText(message.getContent());
            geminiRequest.getContents().add(geminiContent);
        }
        httpPost.setEntity(new StringEntity(JSON.toJSONString(geminiRequest), StandardCharsets.UTF_8));
        return httpPost;
    }

    private String requestGemini(List<Message> messages) throws URISyntaxException, IOException {
        HttpPost httpPost = buildHttpPost(messages);
        String jsonString = httpRequest(httpPost);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        return jsonObject.getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text");
    }
}




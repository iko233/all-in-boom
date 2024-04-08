package ski.iko.app.allinboom.provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.alibaba.fastjson2.JSON;

import ski.iko.app.allinboom.controller.GptRequest;
import ski.iko.app.allinboom.controller.Message;

@Service(GptProvider.PROVIDER_TYPE)
@Scope("prototype")
public class GptProvider extends HttpAbstractProvider {
    public static final String PROVIDER_TYPE = "GptProvider";

    @Override
    public String defaultApiUrl() {
        return "https://api.openai.com/v1/chat/completions";
    }

    @Override
    public SseEmitter stream(List<Message> messages, String model) {
        SseEmitter sseEmitter = new SseEmitter();
        HttpPost httpPost = buildHttpPost(model, messages, true);
        httpStreamRequest(httpPost, sseEmitter::send, sseEmitter::complete);
        return sseEmitter;
    }

    @Override
    public String normal(List<Message> messages, String model) {
        HttpPost httpPost = buildHttpPost(model, messages, false);
        return httpRequest(httpPost);
    }

    private HttpPost buildHttpPost(String model,List<Message> messages,boolean stream) {
        HttpPost httpPost;
        try {
            httpPost = new HttpPost(new URI(requestUrl()));
        } catch (URISyntaxException e) {
            throw new RuntimeException("解析URL["+requestUrl()+"]错误",e);
        }
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

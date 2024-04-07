package ski.iko.app.allinboom.provider;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ski.iko.app.allinboom.controller.Message;

@Service(GeminiProvider.PROVIDER_TYPE)
@Scope("prototype")
public class GeminiProvider extends AbstractProvider {
    public static final String PROVIDER_TYPE = "GeminiProvider";
    private static final String DEFAULT_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";

    @Override
    String defaultApiUrl() {
        return DEFAULT_API_URL;
    }

    @Override
    public SseEmitter stream(List<Message> messages, String model) {
        SseEmitter sseEmitter = new SseEmitter();
        return sseEmitter;
    }

    @Override
    public String normal(List<Message> messages, String model) {
        return "";
    }
}

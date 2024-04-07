package ski.iko.app.allinboom.provider;

import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ski.iko.app.allinboom.controller.Message;

public interface IProvider {
    SseEmitter stream(List<Message> messages, String model);

    String normal(List<Message> messages, String model);
}

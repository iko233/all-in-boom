package ski.iko.app.allinboom.provider.impl.bedrock;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ski.iko.app.allinboom.controller.Message;
import ski.iko.app.allinboom.provider.AbstractProvider;
import ski.iko.app.allinboom.provider.IProvider;

@Service
public class BedRcokProvider extends AbstractProvider implements IProvider {
    @Override
    public SseEmitter stream(List<Message> messages, String model) {
        return null;
    }

    @Override
    public String normal(List<Message> messages, String model) {
        return "";
    }
}

package ski.iko.app.allinboom.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ski.iko.app.allinboom.provider.IProvider;
import ski.iko.app.allinboom.provider.manager.ProviderManager;

@RestController
@RequestMapping({"/v1/chat"})
public class GptController {
    @Autowired
    private ProviderManager providerManager;

    @RequestMapping({"/completions"})
    public Object completions(@RequestBody GptRequest request) {
        String providerString = null;
        String modelString = null;
        try {
            providerString = request.getModel().substring(0, request.getModel().indexOf("/"));
            modelString = request.getModel().substring(request.getModel().indexOf("/") + 1);
        }catch (Exception e) {
            throw new RuntimeException("无效的模型信息["+request.getModel()+"]",e);
        }
        IProvider provider = this.providerManager.getProvider(providerString);
        if (Objects.isNull(provider)) {
            throw new RuntimeException("未知的提供商["+providerString+"]");
        }
        if (request.getStream()){
           return provider.stream(request.getMessages(), modelString);
        }
        return provider.normal(request.getMessages(), modelString);
    }
}

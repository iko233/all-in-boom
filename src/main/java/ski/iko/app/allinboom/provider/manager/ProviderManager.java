
package ski.iko.app.allinboom.provider.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import ski.iko.app.allinboom.dao.ProviderMapper;
import ski.iko.app.allinboom.dao.po.ProviderDo;
import ski.iko.app.allinboom.provider.AbstractProvider;
import ski.iko.app.allinboom.util.SpringContextUtil;

@Service
public class ProviderManager {
    private Map<String, AbstractProvider> providerModelMap;
    @Autowired
    private ProviderMapper providerMapper;
    @Autowired
    private SpringContextUtil springContextUtil;

    @PostConstruct
    public void $init() {
        this.providerModelMap = new ConcurrentHashMap<>();
        List<ProviderDo> providerDos = this.providerMapper.selectList(null);
        for (ProviderDo providerDo : providerDos) {
            AbstractProvider gptProvider = this.springContextUtil.getBean(providerDo.getProviderType(), AbstractProvider.class);
            gptProvider.setId(providerDo.getId());
            gptProvider.setName(providerDo.getName());
            gptProvider.setCode(providerDo.getCode());
            gptProvider.setProxy(providerDo.getProxy());
            gptProvider.setProxyUrl(providerDo.getProxyUrl());
            gptProvider.setKey(providerDo.getKey());
            this.registerProvider(gptProvider);
        }

    }

    public AbstractProvider getProvider(String provider) {
        return this.providerModelMap.get(provider);
    }

    public void registerProvider(AbstractProvider provider) {
        this.providerModelMap.put(provider.getCode(), provider);
    }

    public void deleteProvider(String providerName) {
        this.providerModelMap.remove(providerName);
    }

    public void editProvider(AbstractProvider provider) {
        this.providerModelMap.put(provider.getCode(), provider);
    }
}

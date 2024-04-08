package ski.iko.app.allinboom.provider;

import lombok.Data;

@Data
public abstract class AbstractProvider implements IProvider {
    private String id;
    private String name;
    private String code;
    private String proxy;
    private String proxyUrl;
    private String key;
}

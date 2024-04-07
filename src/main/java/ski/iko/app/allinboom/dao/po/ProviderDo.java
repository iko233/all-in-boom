package ski.iko.app.allinboom.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("boom_provider")
public class ProviderDo {
    private String id;
    private String name;
    private String code;
    private String providerType;
    private String proxy;
    private String proxyUrl;
    private String models;
    private String key;
}

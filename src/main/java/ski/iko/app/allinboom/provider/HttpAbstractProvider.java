package ski.iko.app.allinboom.provider;

import java.io.InputStream;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import ski.iko.app.allinboom.util.StreamProcessUtil;

public abstract class HttpAbstractProvider extends AbstractProvider {
    @Autowired
    private HttpClientBuilder httpClientBuilder;

    @Async
    public void httpStreamRequest(HttpPost httpPost, StreamProcessUtil.Processor processor, completeAction completeAction) {
        try {
            CloseableHttpClient httpclient = httpClientBuilder.build();
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                InputStream inputStream = response.getEntity()
                    .getContent();
                StreamProcessUtil.parse(inputStream, processor);
                if (completeAction != null) {
                    completeAction.complete();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("请求[" + getName() + "]异常", e);
        }
    }

    public void httpStreamRequest(HttpPost httpPost, StreamProcessUtil.Processor processor){
        httpStreamRequest(httpPost,processor,null);
    }

    public String httpRequest(HttpPost httpPost) {
        try {
            CloseableHttpClient httpclient = httpClientBuilder.build();
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                if (response.getCode() != 200) {
                    throw new RuntimeException("请求[" + getName() + "]响应状态码异常,状态码为[" + response.getCode() + "]");
                }
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "UTF-8").trim();
            }
        } catch (Exception e) {
            throw new RuntimeException("请求[" + getName() + "]异常", e);
        }
    }


    public interface completeAction{
        void complete();
    }

    abstract public String defaultApiUrl();

    public String requestUrl() {
        return this.getProxyUrl() != null && !this.getProxyUrl()
            .isBlank() ? this.getProxyUrl() : this.defaultApiUrl();
    }
}

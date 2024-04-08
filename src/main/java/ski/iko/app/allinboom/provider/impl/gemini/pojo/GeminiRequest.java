package ski.iko.app.allinboom.provider.impl.gemini.pojo;

import java.util.List;

import lombok.Data;

@Data
public class GeminiRequest {
    private List<GeminiContent> contents;
}

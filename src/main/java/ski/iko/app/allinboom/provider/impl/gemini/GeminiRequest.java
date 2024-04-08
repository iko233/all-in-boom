package ski.iko.app.allinboom.provider.impl.gemini;

import java.util.List;

import lombok.Data;

@Data
public class GeminiRequest {
    private List<GeminiContent> contents;
}

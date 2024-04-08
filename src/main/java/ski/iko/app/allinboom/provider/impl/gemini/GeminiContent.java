package ski.iko.app.allinboom.provider.impl.gemini;

import lombok.Data;

@Data
public class GeminiContent {
    private String role;
    private GeminiPart parts;
}

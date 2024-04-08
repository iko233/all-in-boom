package ski.iko.app.allinboom.controller;

import lombok.Data;
@Data
public class GptStreamResponse {
    private String id = "chatcmpl-N7LXDtJikVq3gdK5fceM6q2hbnkj";
    private String model = "gpt-3.5-turbo";
    private String object = "chat.completion.chunk";
    private Long created = System.currentTimeMillis();
    private StreamChoices choices;
}

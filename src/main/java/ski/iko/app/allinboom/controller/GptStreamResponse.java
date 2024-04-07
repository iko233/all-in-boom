package ski.iko.app.allinboom.controller;

import lombok.Data;

@Data
public class GptStreamResponse {
    private String id;
    private String model;
    private String object;
    private Long created;
    private StreamChoices choices;
    private String finish_reason;
}

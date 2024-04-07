package ski.iko.app.allinboom.controller;

import lombok.Data;

@Data
public class GptResponse {
    private String id;
    private String model;
    private String object;
    private Long created;
    private Choices choices;
    private String finish_reason;
}

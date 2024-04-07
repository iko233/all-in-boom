package ski.iko.app.allinboom.controller;

import lombok.Data;

@Data
public class NormalChoices {
    private String finish_reason = "stop";
    private Integer index = 0;
    private Message message;
}

package ski.iko.app.allinboom.controller;

import java.util.List;
import lombok.Data;

@Data
public class GptRequest {
    private String model;
    private List<Message> messages;
    private Boolean stream;
}

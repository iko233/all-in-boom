package ski.iko.app.allinboom.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class GptNormalResponse {
    private String id = "chatcmpl-quqik5Qj1zmGj8rxkelibwX4BMUn";
    private String created = "1712497677016";
    private String model = "gpt-3.5-turbo";
    private String object = "chat.completion";
    private List<NormalChoices> choies = new ArrayList<>();
    private NormalUsage usage = new NormalUsage();

}

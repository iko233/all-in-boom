package ski.iko.app.allinboom.controller;

import lombok.Data;


@Data
public class NormalUsage {
     private Long prompt_tokens = 0L;
     private Long completion_tokens = 0L;
     private Long total_tokens = 0L;

}

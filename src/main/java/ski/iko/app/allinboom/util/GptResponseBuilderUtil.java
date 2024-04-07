package ski.iko.app.allinboom.util;

import com.alibaba.fastjson2.JSON;

import ski.iko.app.allinboom.controller.GptNormalResponse;
import ski.iko.app.allinboom.controller.GptStreamResponse;
import ski.iko.app.allinboom.controller.Message;
import ski.iko.app.allinboom.controller.NormalChoices;

public class GptResponseBuilderUtil {

    public static String normal(String date){
        GptNormalResponse gptNormalResponse = new GptNormalResponse();
        NormalChoices normalChoices = new NormalChoices();
        normalChoices.setMessage(new Message());
        normalChoices.getMessage().setContent(date);
        normalChoices.getMessage().setRole("assistant");
        gptNormalResponse.getChoies().add(normalChoices);
        return JSON.toJSONString(gptNormalResponse);
    }

    public static String stream(String date){
        GptStreamResponse gptStreamResponse = new GptStreamResponse();
        return "";
    }
    public static String streamEnd(){
        return "{\"id\":\"chatcmpl-aeMBe71KJeMYbsC2ejJoLH0UuGIW\",\"created\":1712498547768,\"object\":\"chat.completion.chunk\",\"model\":\"gpt-3.5-turbo\",\"choices\":[{\"delta\":{\"content\":\"\"},\"index\":0,\"finish_reason\":null}]}";
    }
}

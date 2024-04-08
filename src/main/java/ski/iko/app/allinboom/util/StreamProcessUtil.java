package ski.iko.app.allinboom.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StreamProcessUtil {

    public static void parse(InputStream inputStream,Processor processor) throws IOException {
        byte[] buffer = new byte[1048];
        int bytesRead;
        StringBuilder textBuilder = new StringBuilder();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            String chunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            while (true) {
                int index = chunk.indexOf("\n");
                if (index != -1) {
                    String substring = chunk.substring(0, index);
                    if (index + 1 != chunk.length()) {
                        chunk = chunk.substring(index + 1);
                    } else {
                        chunk = "";
                    }
                    textBuilder.append(substring);
                    textBuilder.append("\n");
                    if (!textBuilder.toString().isBlank()) {
                        processor.processStream(textBuilder.toString());
                    }
                    textBuilder.setLength(0);
                }else{
                    textBuilder.append(chunk);
                    break;
                }
            }
        }
        if (!textBuilder.isEmpty()) {
            processor.processStream(textBuilder.toString());
        }
    }

    public interface Processor{
        void processStream(String data) throws IOException;
    }
}

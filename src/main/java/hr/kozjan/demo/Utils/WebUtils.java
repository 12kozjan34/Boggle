package hr.kozjan.demo.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebUtils {
    public static boolean checkForWordTruth(String wordAttempt) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            String question = "Answer only with true or false without any interpunct , is " + wordAttempt + " in english dictionary? DON'T SAY ANTYTHING ELSE THAN TRUE OR FALSE, WITHOUT ANY INTERPUNCTION, only " +
                    "thing you can write as response is True or False, so no True. or False. ONLY TRUE OR FALSE!!";

            // The request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean extractMessageFromJSONResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            String content = rootNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            boolean truth = false;
            if (Objects.equals(content, "True.") || Objects.equals(content, "True")){
                return true;
            }

            return false;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}

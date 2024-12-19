package me.wky.screenmatch_terminal.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ChatGptQuery {
    private static final String CHATGPT_KEY= System.getenv("CHATGPT_KEY");
    public static String getTranslation(String texto) {
        OpenAiService service = new OpenAiService(CHATGPT_KEY);

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var response = service.createCompletion(request);
        return response.getChoices().get(0).getText();
    }
}

package ai.travel.planning.assistant.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class TravelService {

    @Value("classpath:/prompts/system_prompt_template.st")
    private Resource systemPromptResource;

    @Value("classpath:/prompts/user_prompt_template.st")
    private Resource userPromptResource;

    private final ChatClient chatClient;

    public TravelService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateTravelPlan(
            String travelerName,
            String destination,
            String numberOfDays,
            String budget,
            String preferences) {

        return chatClient.prompt()
                .system(systemPromptResource)
                .user(promptTemplate -> promptTemplate
                        .text(userPromptResource)
                        .param("travelerName", travelerName)
                        .param("destination", destination)
                        .param("numberOfDays", numberOfDays)
                        .param("budget", budget)
                        .param("preferences", preferences))
                .call()
                .content();
    }
}

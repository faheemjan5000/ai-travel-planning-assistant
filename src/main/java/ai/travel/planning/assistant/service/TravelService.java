package ai.travel.planning.assistant.service;

import ai.travel.planning.assistant.advisors.AuditTokenUsageAdvisor;
import ai.travel.planning.assistant.dto.TravelRequest;
import ai.travel.planning.assistant.model.TravelPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;



import org.springframework.ai.converter.BeanOutputConverter;

import java.util.List;

@Slf4j
@Service
public class TravelService {

    private final ChatClient chatClient;

    @Value("classpath:prompts/system_prompt_template.st")
    private Resource systemPrompt;

    @Value("classpath:prompts/user_prompt_template.st")
    private Resource userPrompt;

    public TravelService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public TravelPlan generateTravelPlan(
            TravelRequest travelRequest) {
            //log.info("Generating travel plan for traveler: {}, destination: {}, number of days: {}, budget: {}, preferences: {}", travelerName, destination, numberOfDays, budget, preferences);
            BeanOutputConverter<TravelPlan> converter =
                new BeanOutputConverter<>(TravelPlan.class);
            log.info("calling AI.....");

             TravelPlan travelPlan = chatClient.prompt()
                .system(systemPrompt)
                     //.advisors(List.of(new SimpleLoggerAdvisor())) built in advisor
                     .advisors(new AuditTokenUsageAdvisor()) //custom advisor
                .user(promptTemplate -> promptTemplate
                        .text(userPrompt)
                        .param("travelerName", travelRequest.getTravelerName())
                        .param("destination", travelRequest.getDestination())
                        .param("numberOfDays", travelRequest.getNumberOfDays())
                        .param("budget", travelRequest.getBudget())
                        .param("preferences", travelRequest.getPreferences())
                        .param("format", converter.getFormat()))

                .call()

                .entity(converter);
        log.info("Generated travel plan: {}", travelPlan);
        return travelPlan;
    }
}

package ai.travel.planning.assistant.advisors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

@Slf4j
public class AuditTokenUsageAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
       ChatResponse chatResponse = chatClientResponse.chatResponse();
            if(chatResponse!=null){
              Usage usage = chatResponse.getMetadata().getUsage();
              if(usage!=null){
                  log.info("Token usage - Prompt tokens: {}, Completion tokens: {}, Total tokens: {}",
                          usage.getPromptTokens(),
                          usage.getCompletionTokens(),
                          usage.getTotalTokens());
              }
            }

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "AuditTokenUsageAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

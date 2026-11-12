package ai.travel.planning.assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelRequest {

    String travelerName;
    String destination;
    String numberOfDays;
    String budget;
    String preferences;
}

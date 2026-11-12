package ai.travel.planning.assistant.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlan {

    private String travelerName;
    private String destination;
    private int numberOfDays;
    private String budget;
    private String preferences;
    private List<DayPlan> itinerary;
}

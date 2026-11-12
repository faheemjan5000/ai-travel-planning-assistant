package ai.travel.planning.assistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayPlan {

    private int day;
    private Activity morning;
    private Activity afternoon;
    private Activity evening;

}

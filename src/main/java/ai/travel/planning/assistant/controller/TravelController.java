package ai.travel.planning.assistant.controller;



import ai.travel.planning.assistant.dto.TravelRequest;
import ai.travel.planning.assistant.model.TravelPlan;
import ai.travel.planning.assistant.service.TravelService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/travel")
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping("/plan")
    public TravelPlan generateTravelPlan(@RequestBody TravelRequest travelRequest) {

        return travelService.generateTravelPlan(travelRequest);
    }
}

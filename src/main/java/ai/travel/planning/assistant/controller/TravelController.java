package ai.travel.planning.assistant.controller;



import ai.travel.planning.assistant.service.TravelService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/travel")
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/plan")
    public String generateTravelPlan(
            @RequestParam String travelerName,
            @RequestParam String destination,
            @RequestParam String numberOfDays,
            @RequestParam String budget,
            @RequestParam String preferences) {

        return travelService.generateTravelPlan(
                travelerName,
                destination,
                numberOfDays,
                budget,
                preferences
        );
    }
}


const travelForm = document.getElementById("travelForm");

travelForm.addEventListener("submit", async function(event) {

    event.preventDefault();

    // Get user information
    const requestData = {
        travelerName: document.getElementById("travelerName").value,
        destination: document.getElementById("destination").value,
        numberOfDays: document.getElementById("numberOfDays").value,
        budget: document.getElementById("budget").value,
        preferences: document.getElementById("preferences").value
    };

    const button = document.getElementById("submitBtn");
    const loading = document.getElementById("loading");
    const result = document.getElementById("result");

    // Show loading animation
    button.disabled = true;
    button.innerHTML = "✈ Planning your trip...";

    loading.hidden = false;
    result.hidden = true;

    try {

        // Send request to Spring Boot
        const apiResponse = await fetch("/api/travel/plan", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(requestData)

        });

        if (!apiResponse.ok) {
            throw new Error("Failed to generate travel plan.");
        }

        // Get AI response
        const data = await apiResponse.text();

        // Parse JSON response
        const travelPlan = JSON.parse(
            data.replace(/```json|```/g, "").trim()
        );

        // Hide loader
        loading.hidden = true;

        // Display itinerary
        displayTravelPlan(travelPlan, requestData);

    } catch (error) {

        loading.hidden = true;
        result.hidden = false;

        result.innerHTML = "";

        const errorMessage = document.createElement("div");
        errorMessage.className = "error-message";
        errorMessage.textContent =
            "Something went wrong: " + error.message;

        result.appendChild(errorMessage);

    } finally {

        // Reset button
        button.disabled = false;
        button.innerHTML = "✨ Generate Travel Plan";

    }

});


// =====================================
// DISPLAY TRAVEL ITINERARY
// =====================================

function displayTravelPlan(plan, request) {

    const result = document.getElementById("result");

    const header = document.getElementById("tripHeader");

    const itinerary = document.getElementById("itinerary");

    result.hidden = false;

    header.replaceChildren();
    itinerary.replaceChildren();


    // Get trip information
    const details = plan.itinerary || plan;

    const travelerName =
        plan.traveler_name || request.travelerName;

    const destination =
        plan.destination || request.destination;

    const days =
        plan.number_of_days || request.numberOfDays;

    const budget =
        plan.budget ?? request.budget;

    const preferences =
        plan.travel_preferences || request.preferences;


    // =====================================
    // TRIP HEADER
    // =====================================

    const title = document.createElement("h2");

    title.textContent = `✈️ Explore ${destination}`;

    const subtitle = document.createElement("p");

    subtitle.textContent =
        `A personalized adventure for ${travelerName}`;

    const info = document.createElement("div");

    info.className = "trip-info";

    const tripDetails = [
        `📅 ${days} Days`,
        `💰 Budget: ${budget}`,
        `❤️ ${preferences}`
    ];

    tripDetails.forEach(item => {

        const span = document.createElement("span");

        span.textContent = item;

        info.appendChild(span);

    });

    header.append(title, subtitle, info);


    // =====================================
    // GET ITINERARY DAYS
    // =====================================

    let daysList = [];

    if (Array.isArray(details)) {

        daysList = details;

    } else {

        daysList = Object.entries(details)
            .filter(([key, value]) =>
                /^day[_\s-]?\d+/i.test(key) &&
                value &&
                typeof value === "object"
            )
            .map(([key, value], index) => ({

                day: key.match(/\d+/)?.[0] || index + 1,

                ...value

            }));

    }


    // =====================================
    // CREATE DAY CARDS
    // =====================================

    daysList.forEach((day, index) => {

        const dayCard = document.createElement("div");

        dayCard.className = "day-card";


        const dayTitle = document.createElement("h3");

        dayTitle.className = "day-title";

        dayTitle.textContent =
            `📍 Day ${day.day || index + 1}`;

        dayCard.appendChild(dayTitle);


        // Morning, Afternoon, Evening
        const timeSlots = [
            "morning",
            "afternoon",
            "evening"
        ];


        timeSlots.forEach(time => {

            const activity = day[time];

            if (!activity || typeof activity !== "object") {
                return;
            }


            const activityCard = document.createElement("div");

            activityCard.className = "activity-card";


            const timeTitle = document.createElement("div");

            timeTitle.className = "activity-time";

            const timeLabels = {
                morning: "☀️ Morning",
                afternoon: "🌤️ Afternoon",
                evening: "🌙 Evening"
            };

            timeTitle.textContent = timeLabels[time];


            const activityTitle = document.createElement("div");

            activityTitle.className = "activity-title";

            activityTitle.textContent =
                activity.activity || "Travel activity";


            const description = document.createElement("p");

            description.className = "activity-description";

            description.textContent =
                activity.description || "No description available.";


            activityCard.append(
                timeTitle,
                activityTitle,
                description
            );


            dayCard.appendChild(activityCard);

        });


        itinerary.appendChild(dayCard);

    });


    // Handle empty itinerary
    if (daysList.length === 0) {

        const message = document.createElement("p");

        message.textContent =
            "The AI did not return a recognizable itinerary. Please try again.";

        itinerary.appendChild(message);

    }

}

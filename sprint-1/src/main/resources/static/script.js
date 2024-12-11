document.getElementById("scenario-form").addEventListener("submit", async function (event) {
    event.preventDefault();

    const title = document.getElementById("title").value;
    const actors = document.getElementById("actors").value.split(",");
    const systemActor = document.getElementById("systemActor").value;
    const steps = document.getElementById("steps").value.split("\n").map(text => ({ text, subSteps: [] }));

    const scenario = { title, actors, systemActor, steps };

    try {
        const response = await fetch("/api/scenario/analyze", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(scenario)
        });

        if (!response.ok) throw new Error("Failed to analyze scenario");

        const result = await response.text();
        document.getElementById("analysis-output").innerText = result;
        document.getElementById("result").classList.remove("hidden");
    } catch (error) {
        console.error(error);
        alert("Error analyzing the scenario. Please try again.");
    }
});

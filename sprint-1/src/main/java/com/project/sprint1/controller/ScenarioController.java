package com.project.sprint1.controller;

import com.project.sprint1.model.Step;
import com.project.sprint1.model.Scenario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/scenario")
public class ScenarioController {
    private final Map<String, Scenario> scenarios = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @PostMapping("/upload")
    public String uploadScenario(@RequestBody Scenario scenario) {
        String id = String.valueOf(idCounter.incrementAndGet());
        scenarios.put(id, scenario);
        return id;
    }

    @GetMapping("/{id}/count-steps")
    public int countSteps(@PathVariable String id) {
        Scenario scenario = scenarios.get(id);
        return countTotalSteps(scenario);
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeScenario(@RequestBody Scenario scenario) {
        // Prosta analiza: zliczanie kroków i podkroków
        int totalSteps = scenario.getSteps().size();
        int totalSubSteps = scenario.getSteps().stream()
            .mapToInt(step -> step.getSubSteps() == null ? 0 : step.getSubSteps().size())
            .sum();

        // Zwrot danych w JSON
        return ResponseEntity.ok().body(
            String.format("Title: %s, Total Steps: %d, Total SubSteps: %d",
                scenario.getTitle(), totalSteps, totalSubSteps)
        );
    }

    @PostMapping("/count-steps")
    public int countSteps(@RequestBody Scenario scenario) {
        return scenario.countStepsIncludingSubsteps();
    }

    @PostMapping("/count-keyword-steps")
    public int countKeywordSteps(@RequestBody Scenario scenario, @RequestParam List<String> keywords) {
        return scenario.countStepsWithKeywords(keywords);
    }

    @PostMapping("/invalid-steps")
    public List<String> findInvalidSteps(@RequestBody Scenario scenario) {
        return scenario.findStepsWithoutActor();
    }

    @PostMapping("/numbered")
    public String generateNumberedScenario(@RequestBody Scenario scenario) {
        return scenario.generateNumberedScenario();
    }

    @PostMapping("/simplify")
    public List<Step> simplifyScenario(@RequestBody Scenario scenario, @RequestParam int maxDepth) {
        return scenario.simplifyScenario(maxDepth);
    }

    private int countTotalSteps(Scenario scenario) {
        // Rekurencyjne liczenie kroków
        return scenario.getSteps().stream()
                .mapToInt(step -> 1 + countTotalStepsInSubSteps(step))
                .sum();
    }

    private int countTotalStepsInSubSteps(Step step) {
        return step.getSubSteps().stream()
                .mapToInt(subStep -> 1 + countTotalStepsInSubSteps(subStep))
                .sum();
    }

}
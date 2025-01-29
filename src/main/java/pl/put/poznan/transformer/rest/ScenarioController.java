package pl.put.poznan.transformer.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.transformer.logic.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/scenario")
public class ScenarioController {

    private final ConcurrentHashMap<String, Scenario> scenarioStorage = new ConcurrentHashMap<>();
    private int scenarioCounter = 1;

    // Upload scenariusza
    @PostMapping("/upload")
    public ResponseEntity<String> uploadScenario(@RequestBody Scenario scenario) {
        String scenarioId = "scenario-" + scenarioCounter++;
        scenarioStorage.put(scenarioId, scenario);
        return ResponseEntity.ok(scenarioId);
    }
    @GetMapping("/{id}/count-actors")
    public ResponseEntity<String> countActors(@PathVariable String id){
        Scenario scenario = scenarioStorage.get(id);
        if (scenario != null) {
            return ResponseEntity.ok(scenario.countActors());
        }
        return ResponseEntity.notFound().build();
    }
    // Liczenie kroków
    @GetMapping("/{id}/count-steps")
    public ResponseEntity<Integer> countSteps(@PathVariable String id) {
        Scenario scenario = scenarioStorage.get(id);
        if (scenario != null) {
            return ResponseEntity.ok(scenario.countStepsIncludingSubsteps());
        }
        return ResponseEntity.notFound().build();
    }

    // Liczenie kroków z kluczowymi słowami
    @GetMapping("/{id}/count-keyword-steps")
    public ResponseEntity<Integer> countKeywordSteps(@PathVariable String id, @RequestParam List<String> keywords) {
        Scenario scenario = scenarioStorage.get(id);
        if (scenario != null) {
            return ResponseEntity.ok(scenario.countStepsWithKeywords(keywords));
        }
        return ResponseEntity.notFound().build();
    }

    // Znajdowanie błędnych kroków
    @GetMapping("/{id}/invalid-steps")
    public ResponseEntity<List<String>> findInvalidSteps(@PathVariable String id) {
        Scenario scenario = scenarioStorage.get(id);
        if (scenario != null) {
            return ResponseEntity.ok(scenario.findStepsWithoutActor());
        }
        return ResponseEntity.notFound().build();
    }

    // Generowanie scenariusza z numeracją
    @GetMapping("/{id}/numbered")
    public ResponseEntity<String> generateNumberedScenario(@PathVariable String id) {
        Scenario scenario = scenarioStorage.get(id);
        if (scenario != null) {
            return ResponseEntity.ok(scenario.generateNumberedScenario());
        }
        return ResponseEntity.notFound().build();
    }

    // Uproszczenie scenariusza
    @GetMapping("/{id}/simplify")
    public ResponseEntity<List<Step>> simplifyScenario(@PathVariable String id, @RequestParam int maxDepth) {
        Scenario scenario = scenarioStorage.get(id);
        if (scenario != null) {
            return ResponseEntity.ok(scenario.simplifyScenario(maxDepth));
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}/analyze")
    public ResponseEntity<String> analyzeScenario(@PathVariable String id) {
        Scenario scenario = scenarioStorage.get(id);
        if (scenario != null) {
            ScenarioVisitor visitor = new ScenarioPrinterVisitor();
            scenario.accept(visitor);
            return ResponseEntity.ok("Scenario analysis completed. Check terminal for output.");
        }
        return ResponseEntity.notFound().build();
    }
}

import pl.put.poznan.transformer.rest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import pl.put.poznan.transformer.logic.Scenario;
import pl.put.poznan.transformer.logic.Step;
import pl.put.poznan.transformer.logic.ScenarioVisitor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScenarioControllerTest {

    @InjectMocks
    private ScenarioController scenarioController;

    @Mock
    private Scenario scenario;

    private ConcurrentHashMap<String, Scenario> scenarioStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        scenarioStorage = new ConcurrentHashMap<>();
        ReflectionTestUtils.setField(scenarioController, "scenarioStorage", scenarioStorage);
    }

    @Test
    void testUploadScenario() {
        Scenario newScenario = mock(Scenario.class);
        ResponseEntity<String> response = scenarioController.uploadScenario(newScenario);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().startsWith("scenario-"));
        assertTrue(scenarioStorage.containsKey(response.getBody()));  // Sprawdzamy, czy scenariusz jest przechowywany
    }

    @Test
    void testCountActors() {
        scenarioStorage.put("scenario-1", scenario);
        when(scenario.countActors()).thenReturn("3");

        ResponseEntity<String> response = scenarioController.countActors("scenario-1");

        assertEquals("3", response.getBody());
    }

    @Test
    void testCountSteps() {
        scenarioStorage.put("scenario-1", scenario);
        when(scenario.countStepsIncludingSubsteps()).thenReturn(5);

        ResponseEntity<Integer> response = scenarioController.countSteps("scenario-1");

        assertEquals(5, response.getBody());
    }

    @Test
    void testCountKeywordSteps() {
        List<String> keywords = Arrays.asList("keyword1", "keyword2");
        scenarioStorage.put("scenario-1", scenario);
        when(scenario.countStepsWithKeywords(keywords)).thenReturn(2);

        ResponseEntity<Integer> response = scenarioController.countKeywordSteps("scenario-1", keywords);

        assertEquals(2, response.getBody());
    }

    @Test
    void testFindInvalidSteps() {
        List<String> invalidSteps = Arrays.asList("Step 1", "Step 2");
        scenarioStorage.put("scenario-1", scenario);
        when(scenario.findStepsWithoutActor()).thenReturn(invalidSteps);

        ResponseEntity<List<String>> response = scenarioController.findInvalidSteps("scenario-1");

        assertEquals(invalidSteps, response.getBody());
    }

    @Test
    void testGenerateNumberedScenario() {
        scenarioStorage.put("scenario-1", scenario);
        when(scenario.generateNumberedScenario()).thenReturn("1. Step 1\n2. Step 2");

        ResponseEntity<String> response = scenarioController.generateNumberedScenario("scenario-1");

        assertEquals("1. Step 1\n2. Step 2", response.getBody());
    }

    @Test
    void testSimplifyScenario() {
        Step step1 = mock(Step.class);
        Step step2 = mock(Step.class);
        List<Step> simplifiedSteps = Arrays.asList(step1, step2);
        scenarioStorage.put("scenario-1", scenario);
        when(scenario.simplifyScenario(2)).thenReturn(simplifiedSteps);

        ResponseEntity<List<Step>> response = scenarioController.simplifyScenario("scenario-1", 2);

        assertEquals(simplifiedSteps, response.getBody());
    }

    @Test
    void testAnalyzeScenario() {
        scenarioStorage.put("scenario-1", scenario);
        doNothing().when(scenario).accept(any(ScenarioVisitor.class));

        ResponseEntity<String> response = scenarioController.analyzeScenario("scenario-1");

        assertEquals("Scenario analysis completed. Check terminal for output.", response.getBody());
    }
}
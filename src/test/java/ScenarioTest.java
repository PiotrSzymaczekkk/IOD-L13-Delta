import pl.put.poznan.transformer.logic.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScenarioTest {

    private Scenario scenario;
    private Step step1;
    private Step step2;

    @BeforeEach
    void setUp() {
        scenario = new Scenario();
        scenario.setTitle("Test Scenario");
        scenario.setActors(Arrays.asList("User", "Admin"));
        scenario.setSystemActor("System");

        step1 = mock(Step.class);
        step2 = mock(Step.class);
        scenario.setSteps(Arrays.asList(step1, step2));
    }

    @Test
    void testCountStepsIncludingSubsteps() {
        when(step1.countStepsIncludingSubsteps()).thenReturn(3);
        when(step2.countStepsIncludingSubsteps()).thenReturn(2);
        
        assertEquals(5, scenario.countStepsIncludingSubsteps());
    }

    @Test
    void testCountStepsWithKeywords() {
        List<String> keywords = Arrays.asList("login", "error");
        when(step1.containsKeywords(keywords)).thenReturn(true);
        when(step2.containsKeywords(keywords)).thenReturn(false);
        when(step1.countSubstepsWithKeywords(keywords)).thenReturn(2);
        when(step2.countSubstepsWithKeywords(keywords)).thenReturn(1);
        
        assertEquals(4, scenario.countStepsWithKeywords(keywords));
    }

    @Test
    void testFindStepsWithoutActor() {
        when(step1.startsWithActor(anyList())).thenReturn(false);
        when(step2.startsWithActor(anyList())).thenReturn(true);
        when(step1.getDescription()).thenReturn("Step 1 description");
        when(step2.findSubstepsWithoutActor(anyList())).thenReturn(Collections.emptyList());
        when(step1.findSubstepsWithoutActor(anyList())).thenReturn(Collections.singletonList("Substep 1 description"));
        
        List<String> invalidSteps = scenario.findStepsWithoutActor();
        assertEquals(Arrays.asList("Step 1 description", "Substep 1 description"), invalidSteps);
    }

    @Test
    void testGenerateNumberedScenario() {
        when(step1.generateNumberedText(1)).thenReturn("Step 1");
        when(step2.generateNumberedText(1)).thenReturn("Step 2");
        
        String expected = "1. Step 1\n2. Step 2\n";
        assertEquals(expected, scenario.generateNumberedScenario());
    }

    @Test
    void testSimplifyScenario() {
        Step simplifiedStep1 = mock(Step.class);
        Step simplifiedStep2 = mock(Step.class);
        
        when(step1.simplify(2)).thenReturn(simplifiedStep1);
        when(step2.simplify(2)).thenReturn(simplifiedStep2);
        
        List<Step> simplifiedSteps = scenario.simplifyScenario(2);
        
        assertEquals(2, simplifiedSteps.size());
        assertEquals(simplifiedStep1, simplifiedSteps.get(0));
        assertEquals(simplifiedStep2, simplifiedSteps.get(1));
    }

    @Test
    void testAcceptScenarioVisitor() {
        ScenarioVisitor visitor = mock(ScenarioVisitor.class);
        scenario.accept(visitor);
        verify(visitor, times(1)).visitScenario(scenario);
    }

    @Test
    void testSetAndGetSteps() {
        Step mockStep = mock(Step.class);
        scenario.setSteps(Collections.singletonList(mockStep));
        assertEquals(1, scenario.getSteps().size());
        assertEquals(mockStep, scenario.getSteps().get(0));
    }
}

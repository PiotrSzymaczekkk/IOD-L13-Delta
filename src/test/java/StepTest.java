import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.put.poznan.transformer.logic.Step;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StepTest {

    private Step step;
    private Step subStep1;
    private Step subStep2;
    private Step nestedSubStep;

    @BeforeEach
    void setUp() {
        nestedSubStep = new Step("Nested Substep", Collections.emptyList());
        subStep1 = new Step("Substep 1", Arrays.asList(nestedSubStep));
        subStep2 = new Step("Substep 2", Collections.emptyList());
        step = new Step("Main Step", Arrays.asList(subStep1, subStep2));
    }

    @Test
    void testCountStepsIncludingSubsteps() {
        assertEquals(4, step.countStepsIncludingSubsteps());
    }

    @Test
    void testContainsKeywords() {
        List<String> keywords = Arrays.asList("Main");
        assertTrue(step.containsKeywords(keywords));
        assertFalse(step.containsKeywords(Collections.singletonList("Unknown")));
    }

    @Test
    void testCountSubstepsWithKeywords() {
        List<String> keywords = Arrays.asList("Substep", "Nested");
        assertEquals(3, step.countSubstepsWithKeywords(keywords));
    }

    @Test
    void testStartsWithActor() {
        List<String> actors = Arrays.asList("User", "Admin", "Main");
        assertTrue(step.startsWithActor(actors));
        assertFalse(subStep1.startsWithActor(actors));
    }

    @Test
    void testFindSubstepsWithoutActor() {
        List<String> actors = Arrays.asList("Main");
        List<String> invalidSteps = step.findSubstepsWithoutActor(actors);
        assertFalse(invalidSteps.contains("Main"));
        assertTrue(invalidSteps.size() == 3);
    }

    @Test
    void testGenerateNumberedText() {
        String expectedOutput = "  - Main Step\n" +
                "    - - Substep 1\n" +
                "      - - - Nested Substep\n" +
                "    - - Substep 2";
        assertEquals(expectedOutput, step.generateNumberedText(1));
    }

    @Test
    void testSimplify() {
        Step simplifiedStep = step.simplify(1);
        assertEquals(2, simplifiedStep.getSubSteps().size());
        assertTrue(simplifiedStep.getSubSteps().get(0).getSubSteps().isEmpty());
    }
}

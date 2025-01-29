package pl.put.poznan.transformer.logic;

public class ScenarioPrinterVisitor implements ScenarioVisitor {
    @Override
    public void visitStep(Step step, int depth) {
        // Wyświetl krok wraz z poziomem zagnieżdżenia
        System.out.println("  ".repeat(depth) + "- " + step.getDescription());
        if (step.getSubSteps() != null) {
            for (Step subStep : step.getSubSteps()) {
                visitStep(subStep, depth + 1);
            }
        }
    }

    @Override
    public void visitScenario(Scenario scenario) {
        System.out.println("Analyzing Scenario: " + scenario.getTitle());
        if (scenario.getSteps() != null) {
            for (Step step : scenario.getSteps()) {
                visitStep(step, 1);
            }
        }
    }
}

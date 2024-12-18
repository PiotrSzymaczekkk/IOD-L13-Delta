package pl.put.poznan.transformer.logic;

public interface ScenarioVisitor {
    void visitStep(Step step, int depth);
    void visitScenario(Scenario scenario);
}

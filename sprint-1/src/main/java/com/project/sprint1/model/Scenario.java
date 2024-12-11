package com.project.sprint1.model;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
    private String title;
    private List<String> actors;
    private String systemActor;
    private List<Step> steps;

    // Gettery i settery
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getSystemActor() {
        return systemActor;
    }

    public void setSystemActor(String systemActor) {
        this.systemActor = systemActor;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    // Funkcja do liczenia kroków (łącznie z pod-scenariuszami)
    public int countStepsIncludingSubsteps() {
        int count = 0;
        for (Step step : steps) {
            count += step.countStepsIncludingSubsteps();
        }
        return count;
    }

    // Funkcja do liczenia kroków z kluczowymi słowami
    public int countStepsWithKeywords(List<String> keywords) {
        int count = 0;
        for (Step step : steps) {
            if (step.containsKeywords(keywords)) {
                count++;
            }
            count += step.countSubstepsWithKeywords(keywords);
        }
        return count;
    }

    // Funkcja do sprawdzania kroków bez aktora
    public List<String> findStepsWithoutActor() {
        List<String> invalidSteps = new ArrayList<>();
        for (Step step : steps) {
            if (!step.startsWithActor(actors)) {
                invalidSteps.add(step.getText());
            }
            invalidSteps.addAll(step.findSubstepsWithoutActor(actors));
        }
        return invalidSteps;
    }

    // Generowanie scenariusza z numeracją
    public String generateNumberedScenario() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps.size(); i++) {
            sb.append((i + 1)).append(". ").append(steps.get(i).generateNumberedText(1)).append("\n");
        }
        return sb.toString();
    }

    // Generowanie uproszczonego scenariusza (do określonego poziomu pod-scenariuszy)
    public List<Step> simplifyScenario(int maxDepth) {
        List<Step> simplifiedSteps = new ArrayList<>();
        for (Step step : steps) {
            simplifiedSteps.add(step.simplify(maxDepth));
        }
        return simplifiedSteps;
    }

}

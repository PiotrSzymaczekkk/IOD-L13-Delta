package pl.put.poznan.transformer.logic;

import java.util.ArrayList;
import java.util.List;

public class Step {
    private String description;
    private List<Step> subSteps;

    public Step(String description, List<Step> subSteps) {
        this.description = description;
        this.subSteps = subSteps;
    }

    // Gettery i settery
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Step> getSubSteps() {
        return subSteps;
    }

    public void setSubSteps(List<Step> subSteps) {
        this.subSteps = subSteps;
    }

    public int countStepsIncludingSubsteps() {
        int count = 1; // Zlicza siebie
        for (Step subStep : subSteps) {
            count += subStep.countStepsIncludingSubsteps();
        }
        return count;
    }

    public boolean containsKeywords(List<String> keywords) {
        if (description == null || keywords == null) {
            return false;
        }
        return keywords.stream().anyMatch(keyword -> description.contains(keyword));
    }

    public int countSubstepsWithKeywords(List<String> keywords) {
        if (subSteps == null || keywords == null) {
            return 0;
        }
        int count = 0;
        for (Step subStep : subSteps) {
            if (subStep.containsKeywords(keywords)) {
                count++;
            }
            count += subStep.countSubstepsWithKeywords(keywords);
        }
        return count;
    }

    public boolean startsWithActor(List<String> actors) {
        for (String actor : actors) {
            if (description.contains(actor)) {
                return true;
            }
        }
        return false;
    }

    public List<String> findSubstepsWithoutActor(List<String> actors) {
        List<String> invalidSteps = new ArrayList<>();
        for (Step subStep : subSteps) {
            if (!subStep.startsWithActor(actors)) {
                invalidSteps.add(subStep.getDescription());
            }
            invalidSteps.addAll(subStep.findSubstepsWithoutActor(actors));
        }
        return invalidSteps;
    }

    public String generateNumberedText(int level) {
        StringBuilder sb = new StringBuilder(" ".repeat(level * 2)).append("- ".repeat(level)).append(description);
        for (Step subStep : subSteps) {
            sb.append("\n").append(subStep.generateNumberedText(level + 1));
        }
        return sb.toString();
    }

    public Step simplify(int maxDepth) {
        if (maxDepth <= 0) {
            // Zachowanie subkroków, nawet jeśli maxDepth == 0
            return new Step(description, new ArrayList<>());
        }
        List<Step> simplifiedSubSteps = new ArrayList<>();
        for (Step subStep : subSteps) {
            simplifiedSubSteps.add(subStep.simplify(maxDepth - 1));
        }
        return new Step(description, simplifiedSubSteps);
    }


}
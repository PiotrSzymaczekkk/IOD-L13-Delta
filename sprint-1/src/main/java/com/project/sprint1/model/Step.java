package com.project.sprint1.model;

import java.util.ArrayList;
import java.util.List;

public class Step {
    private String text;
    private List<Step> subSteps;

    public Step(String text2, List<Step> simplifiedSubSteps) {
        }
    
        // Gettery i settery
        public String getText() {
            return text;
        }
    
        public void setText(String text) {
            this.text = text;
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
            return keywords.stream().anyMatch(text::contains);
        }
    
        public int countSubstepsWithKeywords(List<String> keywords) {
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
                if (text.startsWith(actor)) {
                    return true;
                }
            }
            return false;
        }
    
        public List<String> findSubstepsWithoutActor(List<String> actors) {
            List<String> invalidSteps = new ArrayList<>();
            for (Step subStep : subSteps) {
                if (!subStep.startsWithActor(actors)) {
                    invalidSteps.add(subStep.getText());
                }
                invalidSteps.addAll(subStep.findSubstepsWithoutActor(actors));
            }
            return invalidSteps;
        }
    
        public String generateNumberedText(int level) {
            StringBuilder sb = new StringBuilder(" ".repeat(level * 2)).append("- ").append(text);
            for (Step subStep : subSteps) {
                sb.append("\n").append(subStep.generateNumberedText(level + 1));
            }
            return sb.toString();
        }
    
        public Step simplify(int maxDepth) {
            if (maxDepth <= 0) {
                return new Step(text, new ArrayList<>());
            }
            List<Step> simplifiedSubSteps = new ArrayList<>();
            for (Step subStep : subSteps) {
                simplifiedSubSteps.add(subStep.simplify(maxDepth - 1));
            }
            return new Step(text, simplifiedSubSteps);
    }
}

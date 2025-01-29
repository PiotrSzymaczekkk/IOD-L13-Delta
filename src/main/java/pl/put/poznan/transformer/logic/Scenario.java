package pl.put.poznan.transformer.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje scenariusz zawierający kroki, aktorów oraz systemowego aktora.
 * Scenariusz może zawierać zagnieżdżone pod-scenariusze, które są realizowane przez klasy {@link Step}.
 */
public class Scenario {
    /**
     * Tytuł scenariusza.
     */
    private String title;

    /**
     * Lista aktorów występujących w scenariuszu.
     */
    private List<String> actors;

    /**
     * Nazwa systemowego aktora (system, który wykonuje operacje w scenariuszu).
     */
    private String systemActor;

    /**
     * Lista kroków składających się na scenariusz.
     */
    private List<Step> steps;

    /**
     * Pobiera tytuł scenariusza.
     * @return Tytuł scenariusza jako {@link String}.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Ustawia tytuł scenariusza.
     * @param title Tytuł scenariusza.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Pobiera listę aktorów uczestniczących w scenariuszu.
     * @return Lista aktorów jako {@link List}.
     */
    public List<String> getActors() {
        return actors;
    }

    /**
     * Ustawia listę aktorów dla scenariusza.
     * @param actors Lista aktorów.
     */
    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    /**
     * Pobiera nazwę systemowego aktora.
     * @return Nazwa systemowego aktora jako {@link String}.
     */
    public String getSystemActor() {
        return systemActor;
    }

    /**
     * Ustawia systemowego aktora dla scenariusza.
     * @param systemActor Nazwa systemowego aktora.
     */
    public void setSystemActor(String systemActor) {
        this.systemActor = systemActor;
    }

    /**
     * Pobiera listę kroków scenariusza.
     * @return Lista kroków jako {@link List}.
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Ustawia listę kroków scenariusza.
     * @param steps Lista kroków.
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    /**
     * Liczy liczbę kroków w scenariuszu, uwzględniając zagnieżdżone pod-scenariusze.
     * @return Liczba kroków jako liczba całkowita.
     */
    public int countStepsIncludingSubsteps() {
        int count = 0;
        for (Step step : steps) {
            count += step.countStepsIncludingSubsteps();
        }
        return count;
    }

    /**
     * Liczy liczbę kroków zawierających określone słowa kluczowe.
     * @param keywords Lista słów kluczowych do wyszukania.
     * @return Liczba kroków zawierających słowa kluczowe.
     */
    public int countStepsWithKeywords(List<String> keywords) {
        if (keywords == null || steps == null) {
            return 0;
        }
        int count = 0;
        for (Step step : steps) {
            if (step.containsKeywords(keywords)) {
                count++;
            }
            count += step.countSubstepsWithKeywords(keywords);
        }
        return count;
    }

    /**
     * Znajduje kroki scenariusza, które nie rozpoczynają się od aktora.
     * @return Lista niepoprawnych kroków jako {@link List}.
     */
    public List<String> findStepsWithoutActor() {
        List<String> invalidSteps = new ArrayList<>();
        for (Step step : steps) {
            if (!step.startsWithActor(actors)) {
                invalidSteps.add(step.getDescription());
            }
            invalidSteps.addAll(step.findSubstepsWithoutActor(actors));
        }
        return invalidSteps;
    }
    /**
     * Liczy liczbę aktorów w scenariuszu.
     * @return Liczba aktorów jako liczba całkowita.
     */
    public String countActors(){
        return "" + actors.size();
    }
    /**
     * Generuje scenariusz z numeracją kroków.
     * @return Scenariusz z numeracją kroków jako {@link String}.
     */
    public String generateNumberedScenario() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps.size(); i++) {
            sb.append((i + 1)).append(". ").append(steps.get(i).generateNumberedText(1)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Upraszcza scenariusz, ograniczając jego zagnieżdżenie do określonego poziomu.
     * @param maxDepth Maksymalny poziom zagnieżdżenia pod-scenariuszy.
     * @return Lista uproszczonych kroków jako {@link List}.
     */
    public List<Step> simplifyScenario(int maxDepth) {
        List<Step> simplifiedSteps = new ArrayList<>();
        for (Step step : steps) {
            simplifiedSteps.add(step.simplify(maxDepth));
        }
        return simplifiedSteps;
    }
    public void accept(ScenarioVisitor visitor) {
        visitor.visitScenario(this);
    }

}

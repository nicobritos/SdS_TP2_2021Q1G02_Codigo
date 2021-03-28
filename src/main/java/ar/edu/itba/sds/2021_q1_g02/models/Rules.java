package models;

import javafx.util.Pair;

public class Rules {
    private static boolean stayAliveRule(final int neighborsAlive) {
        return (neighborsAlive == 2 || neighborsAlive == 3);
    }

    private static boolean revivalRule(final int neighborsAlive) {
        return neighborsAlive == 3;
    }

    private static boolean deathSolitudeRule(final int neighborsAlive) {
        return (neighborsAlive == 0 || neighborsAlive == 1);
    }

    private static boolean deathOverpopulationRule(final int neighborsAlive) {
        return (neighborsAlive >= 4);
    }

    public static Pair<State, Boolean> applyRules(final State state, final int neighborsAlive) {
        if (state.equals(State.ALIVE)) {
            boolean wasRuleApplied = stayAliveRule(neighborsAlive);
            if (wasRuleApplied) return new Pair<>(State.ALIVE, false);
            wasRuleApplied = deathSolitudeRule(neighborsAlive);
            if (wasRuleApplied) return new Pair<>(State.DEAD, true);
            wasRuleApplied = deathOverpopulationRule(neighborsAlive);
            if (wasRuleApplied) return new Pair<>(State.DEAD, true);
        }
        return revivalRule(neighborsAlive) ? new Pair<>(State.ALIVE, true) : new Pair<>(State.DEAD, false);
    }
}

package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

public class Rules {
    private final int solitudeDeathLimit;
    private final int overpopulationDeathLimit;
    private final int revivalMinLimit;
    private final int revivalMaxLimit;

    public Rules(int solitudeDeathLimit, int overpopulationDeathLimit, int revivalMinLimit, int revivalMaxLimit) {
        this.solitudeDeathLimit = solitudeDeathLimit;
        this.overpopulationDeathLimit = overpopulationDeathLimit;
        this.revivalMinLimit = revivalMinLimit;
        this.revivalMaxLimit = revivalMaxLimit;
    }

    public int getSolitudeDeathLimit() {
        return this.solitudeDeathLimit;
    }

    private boolean deathSolitudeRule(final int neighborsAlive) {
        return neighborsAlive <= this.solitudeDeathLimit;
    }

    private boolean deathOverpopulationRule(final int neighborsAlive) {
        return neighborsAlive >= this.overpopulationDeathLimit;
    }

    private boolean revivalRule(final int neighborsAlive) {
        return (neighborsAlive >= this.revivalMinLimit && neighborsAlive <= this.revivalMaxLimit);
    }

    public Pair<State, Boolean> applyRules(final State state, final int neighborsAlive) {
        if (state.equals(State.ALIVE)) {
            boolean wasRuleApplied = this.deathSolitudeRule(neighborsAlive);
            if (wasRuleApplied) return new Pair<>(State.DEAD, true);
            wasRuleApplied = this.deathOverpopulationRule(neighborsAlive);
            if (wasRuleApplied) return new Pair<>(State.DEAD, true);
            else return new Pair<>(State.ALIVE, false);
        }
        return this.revivalRule(neighborsAlive) ? new Pair<>(State.ALIVE, true) : new Pair<>(State.DEAD, false);
    }
}

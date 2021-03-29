package ar.edu.itba.sds_2021_q1_g02.models;

public class CellularParticle extends Particle {

    private State state;


    public CellularParticle(Particle particle, boolean isAlive) {
        super(particle.getId(), particle.getRadius(), particle.getProperty());
        this.state = isAlive ? State.ALIVE : State.DEAD;
    }

    public CellularParticle(int id, double radius, double property, boolean isAlive) {
        super(id, radius, property);
        this.state = isAlive ? State.ALIVE : State.DEAD;
    }


    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }
}

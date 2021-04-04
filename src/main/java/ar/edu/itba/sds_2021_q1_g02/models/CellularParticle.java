package ar.edu.itba.sds_2021_q1_g02.models;

public class CellularParticle extends Particle {
    private State state;

    public CellularParticle(Particle particle, boolean isAlive) {
        super(particle.getId(), particle.getRadius(), particle.getProperty());
        this.state = isAlive ? State.ALIVE : State.DEAD;

        this.setPosition(particle.getPosition() != null ? particle.getPosition().copy() : null);
        this.setVelocity(particle.getVelocity() != null ? particle.getVelocity().copy() : null);
    }

    public CellularParticle(int id, double radius, double property, boolean isAlive) {
        super(id, radius, property);
        this.state = isAlive ? State.ALIVE : State.DEAD;
    }

    @Override
    public CellularParticle copy() {
        return new CellularParticle(this, this.state.equals(State.ALIVE));
    }

    public State getState() {
        return this.state;
    }

    public void setState(State newState) {
        this.state = newState;
    }
}

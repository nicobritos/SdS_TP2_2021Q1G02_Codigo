package models;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellularParticle)) return false;
        CellularParticle particle = (CellularParticle) o;
        return this.getId() == particle.getId();
    }
}

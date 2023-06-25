package entitet;

import java.io.Serializable;

public record Stadion(Long id, String nazivStadiona, Integer kapacitet) implements Serializable {

    public Stadion(Long id, String nazivStadiona, Integer kapacitet) {
        this.id = id;
        this.nazivStadiona = nazivStadiona;
        this.kapacitet = kapacitet;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String nazivStadiona() {
        return nazivStadiona;
    }

    @Override
    public Integer kapacitet() {
        return kapacitet;
    }

    @Override
    public String toString() {
        return nazivStadiona;
    }
}

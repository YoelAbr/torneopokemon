package paquete;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class IntegrantesEquipoId implements Serializable {
    private static final long serialVersionUID = 4379817846306052727L;
    @Column(name = "id_equipo", nullable = false)
    private Integer idEquipo;

    @Column(name = "slot", nullable = false)
    private Integer slot;

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegrantesEquipoId entity = (IntegrantesEquipoId) o;
        return Objects.equals(this.idEquipo, entity.idEquipo) &&
                Objects.equals(this.slot, entity.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo, slot);
    }
}
package paquete;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "INTEGRANTES_EQUIPO", schema = "tornepkm")
public class IntegrantesEquipo {
    @EmbeddedId
    private IntegrantesEquipoId id;

    @MapsId("idEquipo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_equipo", nullable = false)
    private EquiposChampion idEquipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pokedex", nullable = false)
    private PokedexCompetitiva idPokedex;

    public IntegrantesEquipoId getId() {
        return id;
    }

    public void setId(IntegrantesEquipoId id) {
        this.id = id;
    }

    public EquiposChampion getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(EquiposChampion idEquipo) {
        this.idEquipo = idEquipo;
    }

    public PokedexCompetitiva getIdPokedex() {
        return idPokedex;
    }

    public void setIdPokedex(PokedexCompetitiva idPokedex) {
        this.idPokedex = idPokedex;
    }

}
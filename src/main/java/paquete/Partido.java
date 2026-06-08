package paquete;

import jakarta.persistence.*;

@Entity
@Table(name = "PARTIDOS", schema = "tornepkm")
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partido", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_jugador1", nullable = false)
    private Jugadore idJugador1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_jugador2", nullable = false)
    private Jugadore idJugador2;

    @Column(name = "ronda", nullable = false, length = 20)
    private String ronda;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Jugadore getIdJugador1() {
        return idJugador1;
    }

    public void setIdJugador1(Jugadore idJugador1) {
        this.idJugador1 = idJugador1;
    }

    public Jugadore getIdJugador2() {
        return idJugador2;
    }

    public void setIdJugador2(Jugadore idJugador2) {
        this.idJugador2 = idJugador2;
    }

    public String getRonda() {
        return ronda;
    }

    public void setRonda(String ronda) {
        this.ronda = ronda;
    }

}
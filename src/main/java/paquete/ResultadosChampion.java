package paquete;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "RESULTADOS_CHAMPIONS", schema = "tornepkm")
public class ResultadosChampion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_partido", nullable = false)
    private Partido idPartido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ganador", nullable = false)
    private Jugadore idGanador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perdedor", nullable = false)
    private Jugadore idPerdedor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Partido getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Partido idPartido) {
        this.idPartido = idPartido;
    }

    public Jugadore getIdGanador() {
        return idGanador;
    }

    public void setIdGanador(Jugadore idGanador) {
        this.idGanador = idGanador;
    }

    public Jugadore getIdPerdedor() {
        return idPerdedor;
    }

    public void setIdPerdedor(Jugadore idPerdedor) {
        this.idPerdedor = idPerdedor;
    }

}
package paquete;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "JUGADORES", schema = "tornepkm")
public class Jugadore {
    @Id
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuarios;

    @Column(name = "nombre_entrenador", nullable = false, length = 50)
    private String nombreEntrenador;

    @ColumnDefault("0")
    @Column(name = "puntos_champions")
    private Integer puntosChampions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuario usuarios) {
        this.usuarios = usuarios;
    }

    public String getNombreEntrenador() {
        return nombreEntrenador;
    }

    public void setNombreEntrenador(String nombreEntrenador) {
        this.nombreEntrenador = nombreEntrenador;
    }

    public Integer getPuntosChampions() {
        return puntosChampions;
    }

    public void setPuntosChampions(Integer puntosChampions) {
        this.puntosChampions = puntosChampions;
    }

}
package paquete;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "LOGS_ACCESO", schema = "tornepkm")
public class LogsAcceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_hora_login")
    private Instant fechaHoraLogin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Instant getFechaHoraLogin() {
        return fechaHoraLogin;
    }

    public void setFechaHoraLogin(Instant fechaHoraLogin) {
        this.fechaHoraLogin = fechaHoraLogin;
    }

}
package com.banquito.sistema.originacion.model;

import java.sql.Timestamp;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "historialestados")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    @NotNull(message = "El ID de la solicitud es obligatorio")
    @Column(name = "id_solicitud", nullable = false)
    private Long idSolicitud;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 12, message = "El estado no puede tener más de 12 caracteres")
    @Column(name = "estado", nullable = false, length = 12)
    private String estado;

    @NotNull(message = "La fecha y hora es obligatoria")
    @Column(name = "fecha_hora", nullable = false)
    private Timestamp fechaHora;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede tener más de 50 caracteres")
    @Column(name = "usuario", nullable = false, length = 50)
    private String usuario;

    @Size(max = 120, message = "El motivo no puede tener más de 120 caracteres")
    @Column(name = "motivo", length = 120)
    private String motivo;

    public HistorialEstado(Long id) {
        this.idHistorial = id;
    }

    public Long getId() {
        return idHistorial;
    }

    public void setId(Long id) {
        this.idHistorial = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialEstado that = (HistorialEstado) o;
        return Objects.equals(idHistorial, that.idHistorial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHistorial);
    }

    @PrePersist
    protected void onCreate() {
        if (fechaHora == null) {
            fechaHora = new Timestamp(System.currentTimeMillis());
        }
    }
} 
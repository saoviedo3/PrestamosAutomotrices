package com.banquito.sistema.originacion.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "HistorialEstados")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdHistorialEstado")
    private Long id;

    @NotNull(message = "El ID de la solicitud es obligatorio")
    @Column(name = "IdSolicitud", nullable = false)
    private Long idSolicitud;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 12, message = "El estado no puede tener más de 12 caracteres")
    @Column(name = "Estado", nullable = false, length = 12)
    private String estado;

    @Column(name = "FechaHora", nullable = false)
    private Timestamp fechaHora;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 100, message = "El usuario no puede tener más de 100 caracteres")
    @Column(name = "Usuario", nullable = false, length = 100)
    private String usuario;

    @Size(max = 500, message = "El motivo no puede tener más de 500 caracteres")
    @Column(name = "Motivo", length = 500)
    private String motivo;

    @Version
    @Column(name = "version")
    private Long version;

    public HistorialEstado(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialEstado that = (HistorialEstado) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    protected void onCreate() {
        if (fechaHora == null) {
            fechaHora = Timestamp.valueOf(LocalDateTime.now());
        }
    }
} 
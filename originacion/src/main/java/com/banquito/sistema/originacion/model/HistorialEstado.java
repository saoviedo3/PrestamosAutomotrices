package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;
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
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El tipo de entidad es obligatorio")
    @Size(max = 50, message = "El tipo de entidad no puede tener más de 50 caracteres")
    @Column(name = "entidad_tipo", nullable = false, length = 50)
    private String entidadTipo; 

    @NotNull(message = "El ID de la entidad es obligatorio")
    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Size(max = 50, message = "El estado anterior no puede tener más de 50 caracteres")
    @Column(name = "estado_anterior", length = 50)
    private String estadoAnterior;

    @NotBlank(message = "El estado nuevo es obligatorio")
    @Size(max = 50, message = "El estado nuevo no puede tener más de 50 caracteres")
    @Column(name = "estado_nuevo", nullable = false, length = 50)
    private String estadoNuevo;

    @Size(max = 500, message = "El motivo no puede tener más de 500 caracteres")
    @Column(name = "motivo", length = 500)
    private String motivo;

    @NotBlank(message = "El usuario que realiza el cambio es obligatorio")
    @Size(max = 100, message = "El usuario no puede tener más de 100 caracteres")
    @Column(name = "usuario_cambio", nullable = false, length = 100)
    private String usuarioCambio;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Size(max = 1000, message = "Las observaciones no pueden tener más de 1000 caracteres")
    @Column(name = "observaciones", length = 1000)
    private String observaciones;

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
        fechaCambio = LocalDateTime.now();
    }
} 
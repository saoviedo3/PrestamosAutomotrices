package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.*;

@Entity
@Table(name = "historial_estado")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistorialEstado {

    public static class Views {
        public static class Public {}
        public static class Internal extends Public {}
        public static class Create {}
        public static class Update {}
    }

    
    public interface CreateValidation extends Default {}
    public interface UpdateValidation extends Default {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(Views.Public.class)
    private Long id;

    @Column(name = "entidad_tipo", nullable = false, length = 50)
    @NotBlank(message = "El tipo de entidad es obligatorio", groups = {CreateValidation.class})
    @Size(max = 50, message = "El tipo de entidad no puede tener más de 50 caracteres", groups = {CreateValidation.class})
    @JsonView(Views.Public.class)
    private String entidadTipo; 

    @Column(name = "entidad_id", nullable = false)
    @NotNull(message = "El ID de la entidad es obligatorio", groups = {CreateValidation.class})
    @JsonView(Views.Public.class)
    private Long entidadId;

    @Column(name = "estado_anterior", length = 50)
    @Size(max = 50, message = "El estado anterior no puede tener más de 50 caracteres", groups = {CreateValidation.class})
    @JsonView(Views.Public.class)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", nullable = false, length = 50)
    @NotBlank(message = "El estado nuevo es obligatorio", groups = {CreateValidation.class})
    @Size(max = 50, message = "El estado nuevo no puede tener más de 50 caracteres", groups = {CreateValidation.class})
    @JsonView(Views.Public.class)
    private String estadoNuevo;

    @Column(name = "motivo", length = 500)
    @Size(max = 500, message = "El motivo no puede tener más de 500 caracteres", groups = {CreateValidation.class})
    @JsonView(Views.Public.class)
    private String motivo;

    @Column(name = "usuario_cambio", nullable = false, length = 100)
    @NotBlank(message = "El usuario de cambio es obligatorio", groups = {CreateValidation.class})
    @Size(max = 100, message = "El usuario de cambio no puede tener más de 100 caracteres", groups = {CreateValidation.class})
    @JsonView(Views.Public.class)
    private String usuarioCambio;

    @Column(name = "fecha_cambio", nullable = false)
    @JsonView(Views.Public.class)
    private LocalDateTime fechaCambio;

    @Column(name = "observaciones", length = 1000)
    @Size(max = 1000, message = "Las observaciones no pueden tener más de 1000 caracteres", groups = {CreateValidation.class})
    @JsonView(Views.Public.class)
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
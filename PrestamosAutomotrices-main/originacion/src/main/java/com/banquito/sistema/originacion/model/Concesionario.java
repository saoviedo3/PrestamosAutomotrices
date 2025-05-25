package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.*;

@Entity
@Table(name = "concesionario")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Concesionario {

    
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

    @Column(name = "codigo", unique = true, nullable = false, length = 20)
    @NotBlank(message = "El código es obligatorio", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 200)
    @NotBlank(message = "La dirección es obligatoria", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String direccion;

    @Column(name = "telefono", length = 15)
    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String telefono;

    @Column(name = "email", length = 100)
    @Email(message = "El email debe tener un formato válido", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String email;

    @Column(name = "ciudad", nullable = false, length = 50)
    @NotBlank(message = "La ciudad es obligatoria", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 50, message = "La ciudad no puede tener más de 50 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String ciudad;

    @Column(name = "provincia", nullable = false, length = 50)
    @NotBlank(message = "La provincia es obligatoria", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 50, message = "La provincia no puede tener más de 50 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String provincia;

    @Column(name = "estado", nullable = false, length = 20)
    @Pattern(regexp = "ACTIVO|INACTIVO|SUSPENDIDO", message = "El estado debe ser ACTIVO, INACTIVO o SUSPENDIDO", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String estado; 

    @Column(name = "fecha_creacion", nullable = false)
    @JsonView(Views.Internal.class)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    @JsonView(Views.Internal.class)
    private LocalDateTime fechaActualizacion;

    public Concesionario(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concesionario that = (Concesionario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
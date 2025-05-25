package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "concesionario")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Concesionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    @Column(name = "codigo", unique = true, nullable = false, length = 20)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    @Column(name = "telefono", length = 15)
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 50, message = "La ciudad no puede tener más de 50 caracteres")
    @Column(name = "ciudad", nullable = false, length = 50)
    private String ciudad;

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 50, message = "La provincia no puede tener más de 50 caracteres")
    @Column(name = "provincia", nullable = false, length = 50)
    private String provincia;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ACTIVO|INACTIVO|SUSPENDIDO", message = "El estado debe ser ACTIVO, INACTIVO o SUSPENDIDO")
    @Column(name = "estado", nullable = false, length = 20)
    private String estado; 

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
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
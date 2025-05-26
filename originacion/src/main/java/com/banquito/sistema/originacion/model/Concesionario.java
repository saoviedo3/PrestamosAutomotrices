package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "concesionarios")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Concesionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id  concesionario")
    private Long idConcesionario;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 80, message = "La razón social no puede tener más de 80 caracteres")
    @Column(name = "razonsocial", nullable = false, length = 80)
    private String razonSocial;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 50, message = "El email no puede tener más de 50 caracteres")
    @Column(name = "emailcontacto", length = 50)
    private String emailContacto;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    public Concesionario(Long id) {
        this.idConcesionario = id;
    }

    public Long getId() {
        return idConcesionario;
    }

    public void setId(Long id) {
        this.idConcesionario = id;
    }

    public String getNombre() {
        return razonSocial;
    }

    public void setNombre(String nombre) {
        this.razonSocial = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concesionario that = (Concesionario) o;
        return Objects.equals(idConcesionario, that.idConcesionario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idConcesionario);
    }
}
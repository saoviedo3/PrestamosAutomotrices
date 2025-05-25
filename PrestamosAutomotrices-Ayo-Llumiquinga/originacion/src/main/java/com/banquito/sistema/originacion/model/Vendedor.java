package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "vendedores")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    @Column(name = "codigo", unique = true, nullable = false, length = 20)
    private String codigo;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 10, message = "La cédula debe tener exactamente 10 caracteres")
    @Pattern(regexp = "\\d{10}", message = "La cédula debe contener solo números")
    @Column(name = "cedula", unique = true, nullable = false, length = 10)
    private String cedula;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden tener más de 100 caracteres")
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden tener más de 100 caracteres")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    @Column(name = "telefono", length = 15)
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ACTIVO|INACTIVO|SUSPENDIDO", message = "El estado debe ser ACTIVO, INACTIVO o SUSPENDIDO")
    @Column(name = "estado", nullable = false, length = 20)
    private String estado; 

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concesionario_id", nullable = false)
    private Concesionario concesionario;

    
    @JsonProperty("concesionarioId")
    public Long getConcesionarioId() {
        return concesionario != null ? concesionario.getId() : null;
    }

    @JsonProperty("concesionarioNombre")
    public String getConcesionarioNombre() {
        return concesionario != null ? concesionario.getNombre() : null;
    }

    
    @JsonProperty("concesionarioId")
    public void setConcesionarioId(Long concesionarioId) {
        if (concesionarioId != null) {
            this.concesionario = new Concesionario(concesionarioId);
        }
    }

    public Vendedor(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendedor vendedor = (Vendedor) o;
        return Objects.equals(id, vendedor.id);
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

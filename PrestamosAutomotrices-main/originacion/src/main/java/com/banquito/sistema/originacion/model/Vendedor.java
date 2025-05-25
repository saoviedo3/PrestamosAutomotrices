package com.banquito.sistema.originacion.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.*;

@Entity
@Table(name = "vendedor")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Vendedor {

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

    @Column(name = "cedula", unique = true, nullable = false, length = 10)
    @NotBlank(message = "La cédula es obligatoria", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(min = 10, max = 10, message = "La cédula debe tener exactamente 10 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @Pattern(regexp = "\\d{10}", message = "La cédula debe contener solo números", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String cedula;

    @Column(name = "nombres", nullable = false, length = 100)
    @NotBlank(message = "Los nombres son obligatorios", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 100, message = "Los nombres no pueden tener más de 100 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    @NotBlank(message = "Los apellidos son obligatorios", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 100, message = "Los apellidos no pueden tener más de 100 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String apellidos;

    @Column(name = "telefono", length = 15)
    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String telefono;

    @Column(name = "email", length = 100)
    @Email(message = "El email debe tener un formato válido", groups = {CreateValidation.class, UpdateValidation.class})
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String email;

    @Column(name = "estado", nullable = false, length = 20)
    @Pattern(regexp = "ACTIVO|INACTIVO|SUSPENDIDO", message = "El estado debe ser ACTIVO, INACTIVO o SUSPENDIDO", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private String estado; 

    @Column(name = "fecha_ingreso", nullable = false)
    @NotNull(message = "La fecha de ingreso es obligatoria", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonView(Views.Public.class)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_creacion", nullable = false)
    @JsonView(Views.Internal.class)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    @JsonView(Views.Internal.class)
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concesionario_id", nullable = false)
    @NotNull(message = "El concesionario es obligatorio", groups = {CreateValidation.class, UpdateValidation.class})
    @JsonIgnore
    private Concesionario concesionario;

    
    @Transient
    @JsonView(Views.Public.class)
    private Long concesionarioId;

    @Transient
    @JsonView(Views.Public.class)
    private String concesionarioNombre;

    public Vendedor(Long id) {
        this.id = id;
    }

    
    @PostLoad
    public void loadVirtualFields() {
        if (this.concesionario != null) {
            this.concesionarioId = this.concesionario.getId();
            this.concesionarioNombre = this.concesionario.getNombre();
        }
    }

    public void setConcesionarioId(Long concesionarioId) {
        this.concesionarioId = concesionarioId;
        if (concesionarioId != null) {
            this.concesionario = new Concesionario(concesionarioId);
        }
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

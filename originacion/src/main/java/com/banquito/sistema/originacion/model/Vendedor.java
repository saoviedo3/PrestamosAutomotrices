package com.banquito.sistema.originacion.model;

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
    @Column(name = "id_vendedor")
    private Long idVendedor;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 60, message = "El email no puede tener más de 60 caracteres")
    @Column(name = "email", length = 60)
    private String email;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_concesionario", nullable = false)
    private Concesionario concesionario;

    @JsonProperty("concesionarioId")
    public Long getConcesionarioId() {
        return concesionario != null ? concesionario.getIdConcesionario() : null;
    }

    @JsonProperty("concesionarioNombre")
    public String getConcesionarioNombre() {
        return concesionario != null ? concesionario.getRazonSocial() : null;
    }

    @JsonProperty("concesionarioId")
    public void setConcesionarioId(Long concesionarioId) {
        if (concesionarioId != null) {
            this.concesionario = new Concesionario(concesionarioId);
        }
    }

    public Vendedor(Long id) {
        this.idVendedor = id;
    }

    public Long getId() {
        return idVendedor;
    }

    public void setId(Long id) {
        this.idVendedor = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendedor vendedor = (Vendedor) o;
        return Objects.equals(idVendedor, vendedor.idVendedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVendedor);
    }
}

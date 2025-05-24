package com.banquito.sistema.originacion.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "clientes_prospectos")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClienteProspecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente_prospecto")
    private Integer idClienteProspecto;

    @Column(name = "cedula", length = 10, nullable = false, unique = true)
    private String cedula;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 60)
    private String email;

    @Column(name = "direccion", length = 120)
    private String direccion;

    @Column(name = "ingresos", precision = 12, scale = 2)
    private BigDecimal ingresos;

    @Column(name = "egresos", precision = 12, scale = 2)
    private BigDecimal egresos;

    @Column(name = "actividad_economica", length = 120)
    private String actividadEconomica;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    public ClienteProspecto(Integer idClienteProspecto) {
        this.idClienteProspecto = idClienteProspecto;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClienteProspecto that = (ClienteProspecto) obj;
        return idClienteProspecto != null ? idClienteProspecto.equals(that.idClienteProspecto) : that.idClienteProspecto == null;
    }

    @Override
    public int hashCode() {
        return idClienteProspecto != null ? idClienteProspecto.hashCode() : 0;
    }
}

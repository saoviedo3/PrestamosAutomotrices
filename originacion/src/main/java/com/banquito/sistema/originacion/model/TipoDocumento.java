package com.banquito.sistema.originacion.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_documentos")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Integer idTipoDocumento;

    @Column(name = "nombre", length = 40, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    public TipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TipoDocumento that = (TipoDocumento) obj;
        return idTipoDocumento != null ? idTipoDocumento.equals(that.idTipoDocumento) : that.idTipoDocumento == null;
    }

    @Override
    public int hashCode() {
        return idTipoDocumento != null ? idTipoDocumento.hashCode() : 0;
    }
}

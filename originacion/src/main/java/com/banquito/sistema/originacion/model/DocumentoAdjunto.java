package com.banquito.sistema.originacion.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_adjuntos")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DocumentoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer idDocumento;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "id_tipo_documento", nullable = false)
    private Integer idTipoDocumento;

    @Column(name = "ruta_archivo", length = 150, nullable = false)
    private String rutaArchivo;

    @Column(name = "fecha_cargado", nullable = false)
    private LocalDateTime fechaCargado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", insertable = false, updatable = false)
    private SolicitudCredito solicitudCredito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_documento", insertable = false, updatable = false)
    private TipoDocumento tipoDocumento;

    public DocumentoAdjunto(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DocumentoAdjunto that = (DocumentoAdjunto) obj;
        return idDocumento != null ? idDocumento.equals(that.idDocumento) : that.idDocumento == null;
    }

    @Override
    public int hashCode() {
        return idDocumento != null ? idDocumento.hashCode() : 0;
    }
}

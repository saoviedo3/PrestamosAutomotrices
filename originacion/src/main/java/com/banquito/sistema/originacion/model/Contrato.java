package com.banquito.sistema.originacion.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contratos")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrato")
    private Integer idContrato;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "ruta_archivo", length = 150)
    private String rutaArchivo;

    @Column(name = "fecha_generado", nullable = false)
    private LocalDateTime fechaGenerado;

    @Column(name = "fecha_firma")
    private LocalDateTime fechaFirma;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "condicion_especial", length = 120)
    private String condicionEspecial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", insertable = false, updatable = false)
    private SolicitudCredito solicitudCredito;

    public Contrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contrato that = (Contrato) obj;
        return idContrato != null ? idContrato.equals(that.idContrato) : that.idContrato == null;
    }

    @Override
    public int hashCode() {
        return idContrato != null ? idContrato.hashCode() : 0;
    }
} 
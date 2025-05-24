package com.banquito.sistema.originacion.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_creditos")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SolicitudCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @Column(name = "id_cliente_prospecto", nullable = false)
    private Integer idClienteProspecto;

    @Column(name = "id_vendedor")
    private Integer idVendedor;

    @Column(name = "monto_solicitado", precision = 12, scale = 2, nullable = false)
    private BigDecimal montoSolicitado;

    @Column(name = "plazo_meses", nullable = false)
    private Integer plazoMeses;

    @Column(name = "tasa_interes", precision = 6, scale = 2, nullable = false)
    private BigDecimal tasaInteres;

    @Column(name = "cuota_mensual", precision = 12, scale = 2)
    private BigDecimal cuotaMensual;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "motivo")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente_prospecto", insertable = false, updatable = false)
    private ClienteProspecto clienteProspecto;

    public SolicitudCredito(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SolicitudCredito that = (SolicitudCredito) obj;
        return idSolicitud != null ? idSolicitud.equals(that.idSolicitud) : that.idSolicitud == null;
    }

    @Override
    public int hashCode() {
        return idSolicitud != null ? idSolicitud.hashCode() : 0;
    }
}

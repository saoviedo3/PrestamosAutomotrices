package com.banquito.sistema.originacion.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagares")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Pagare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagare")
    private Integer idPagare;

    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "numero_cuota", precision = 3, scale = 0, nullable = false)
    private BigDecimal numeroCuota;

    @Column(name = "ruta_archivo", length = 150)
    private String rutaArchivo;

    @Column(name = "fecha_generado", nullable = false)
    private LocalDateTime fechaGenerado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", insertable = false, updatable = false)
    private SolicitudCredito solicitudCredito;

    public Pagare(Integer idPagare) {
        this.idPagare = idPagare;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pagare that = (Pagare) obj;
        return idPagare != null ? idPagare.equals(that.idPagare) : that.idPagare == null;
    }

    @Override
    public int hashCode() {
        return idPagare != null ? idPagare.hashCode() : 0;
    }
} 
package com.banquito.sistema.originacion.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SimulacionCreditoDTO {
    private BigDecimal montoSolicitado;
    private Integer plazoMeses;
    private BigDecimal tasaInteres;
    private BigDecimal cuotaMensual;
    private BigDecimal capacidadPago;
    private boolean esViable;
    private BigDecimal porcentajeCompromiso;
} 
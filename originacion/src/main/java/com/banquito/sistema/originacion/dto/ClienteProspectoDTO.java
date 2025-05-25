package com.banquito.sistema.originacion.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ClienteProspectoDTO {
    private Integer idClienteProspecto;
    private String cedula;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private BigDecimal ingresos;
    private BigDecimal egresos;
    private String estado;
} 
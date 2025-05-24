package com.banquito.sistema.originacion.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClienteProspectoDTO {

    private Integer idClienteProspecto;
    private String cedula;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal ingresos;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal egresos;
    
    private String actividadEconomica;
    private String estado;
} 
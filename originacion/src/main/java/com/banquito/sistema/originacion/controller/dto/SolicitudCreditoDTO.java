package com.banquito.sistema.originacion.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolicitudCreditoDTO {
    
    private Integer id;
    
    @NotNull(message = "El ID del cliente prospecto es obligatorio")
    private Integer idClienteProspecto;
    
    @NotNull(message = "El ID del vehículo es obligatorio")
    private Integer idVehiculo;
    
    @NotNull(message = "El ID del vendedor es obligatorio")
    private Integer idVendedor;
    
    @NotBlank(message = "El número de solicitud es obligatorio")
    @Size(min = 5, max = 20, message = "El número de solicitud debe tener entre 5 y 20 caracteres")
    private String numeroSolicitud;
    
    @NotNull(message = "El monto solicitado es obligatorio")
    @DecimalMin(value = "1000.0", message = "El monto solicitado debe ser al menos 1000")
    private BigDecimal montoSolicitado;
    
    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 12, message = "El plazo mínimo es de 12 meses")
    private Integer plazoMeses;
    
    @NotNull(message = "La entrada es obligatoria")
    @DecimalMin(value = "0.0", message = "La entrada debe ser mayor o igual a 0")
    private BigDecimal entrada;
    
    private BigDecimal scoreInterno;
    private BigDecimal scoreExterno;
    private BigDecimal relacionCuotaIngreso;
    private BigDecimal tasaAnual;
    private BigDecimal cuotaMensual;
    private BigDecimal totalPagar;
    
    @Pattern(regexp = "Borrador|EnRevision|Aprobada|Rechazada|Cancelada", 
            message = "El estado debe ser uno de: Borrador, EnRevision, Aprobada, Rechazada, Cancelada")
    private String estado;
    
    private String motivo;
} 
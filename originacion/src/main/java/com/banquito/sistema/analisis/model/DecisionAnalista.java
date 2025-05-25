package com.banquito.sistema.analisis.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Clase para transportar la decisión del analista junto con una observación
 */
public class DecisionAnalista {
    
    @Valid
    @NotNull(message = "La observación es obligatoria")
    private ObservacionAnalista observacion;
    
    @NotBlank(message = "La decisión es obligatoria")
    @Pattern(regexp = "Aprobada|Rechazada", message = "La decisión debe ser 'Aprobada' o 'Rechazada'")
    private String decision;
    
    public DecisionAnalista() {
    }
    
    public DecisionAnalista(ObservacionAnalista observacion, String decision) {
        this.observacion = observacion;
        this.decision = decision;
    }
    
    public ObservacionAnalista getObservacion() {
        return observacion;
    }
    
    public void setObservacion(ObservacionAnalista observacion) {
        this.observacion = observacion;
    }
    
    public String getDecision() {
        return decision;
    }
    
    public void setDecision(String decision) {
        this.decision = decision;
    }
} 
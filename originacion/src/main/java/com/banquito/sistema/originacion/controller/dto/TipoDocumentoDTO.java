package com.banquito.sistema.originacion.controller.dto;

import lombok.Data;

@Data
public class TipoDocumentoDTO {

    private Integer idTipoDocumento;
    private String nombre;
    private String descripcion;
    private String estado;
} 
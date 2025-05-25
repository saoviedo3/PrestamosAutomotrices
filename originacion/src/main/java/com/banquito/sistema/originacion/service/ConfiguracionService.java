package com.banquito.sistema.originacion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfiguracionService implements CommandLineRunner {

    private final TipoDocumentoService tipoDocumentoService;

    @Override
    public void run(String... args) {
        inicializarDatosBasicos();
    }

    /**
     * Inicializar datos básicos del sistema
     */
    public void inicializarDatosBasicos() {
        try {
            log.info("Iniciando configuración de datos básicos del sistema...");
            
            // Crear tipos de documentos predeterminados
            tipoDocumentoService.crearTiposPredeterminados();
            
            log.info("Configuración de datos básicos completada exitosamente");
            
        } catch (Exception e) {
            log.error("Error al inicializar datos básicos: {}", e.getMessage());
        }
    }
} 
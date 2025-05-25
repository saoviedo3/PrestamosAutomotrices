package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.dto.EstadoSolicitudCompletoDTO;
import com.banquito.sistema.originacion.mapper.EstadoSolicitudCompletoMapper;
import com.banquito.sistema.originacion.service.OrigenacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/originacion")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrigenacionController {

    private final OrigenacionService origenacionService;
    private final EstadoSolicitudCompletoMapper estadoMapper;

    @GetMapping("/estado-completo/{idSolicitud}")
    public ResponseEntity<EstadoSolicitudCompletoDTO> consultarEstadoCompleto(@PathVariable Integer idSolicitud) {
        OrigenacionService.EstadoSolicitudCompleto estado = origenacionService.consultarEstadoCompleto(idSolicitud);
        EstadoSolicitudCompletoDTO estadoDTO = estadoMapper.toDTO(estado);
        return ResponseEntity.ok(estadoDTO);
    }

    @PostMapping("/simular-con-capacidad")
    public ResponseEntity<OrigenacionService.SimulacionCredito> simularConCapacidad(@RequestParam Integer idCliente,
                                                                        @RequestParam BigDecimal monto,
                                                                        @RequestParam BigDecimal tasa,
                                                                        @RequestParam Integer plazo) {
        OrigenacionService.SimulacionCredito simulacion = origenacionService.simularCreditoConCapacidad(idCliente, monto, tasa, plazo);
        return ResponseEntity.ok(simulacion);
    }

    @PostMapping("/rechazar-completo/{idSolicitud}")
    public ResponseEntity<String> rechazarCompleto(@PathVariable Integer idSolicitud,
                                                 @RequestParam String motivo) {
        origenacionService.rechazarSolicitudCompleta(idSolicitud, motivo);
        return ResponseEntity.ok("Solicitud rechazada y limpieza completada");
    }
} 
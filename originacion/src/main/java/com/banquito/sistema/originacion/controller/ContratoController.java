package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContratoController {

    private final ContratoService contratoService;
    private final ContratoMapper contratoMapper;

    @PostMapping("/generar")
    public ResponseEntity<ContratoDTO> generarContrato(@RequestParam Integer idSolicitud,
                                                     @RequestParam(required = false) String condicionesEspeciales) {
        Contrato contrato = contratoService.generarContrato(idSolicitud, condicionesEspeciales);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratoMapper.toDTO(contrato));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoDTO> buscarPorId(@PathVariable Integer id) {
        Contrato contrato = contratoService.buscarPorId(id);
        return ResponseEntity.ok(contratoMapper.toDTO(contrato));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<ContratoDTO> buscarPorSolicitud(@PathVariable Integer idSolicitud) {
        Contrato contrato = contratoService.buscarPorSolicitud(idSolicitud);
        return ResponseEntity.ok(contratoMapper.toDTO(contrato));
    }

    @GetMapping
    public ResponseEntity<List<ContratoDTO>> listarTodos(@RequestParam(required = false) String estado) {
        List<Contrato> contratos;
        
        if (estado != null && !estado.isEmpty()) {
            contratos = contratoService.listarPorEstado(estado);
        } else {
            contratos = contratoService.listarPorEstado("VIGENTE");
        }
        
        List<ContratoDTO> contratosDTO = contratos.stream()
                .map(contratoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(contratosDTO);
    }

    @PostMapping("/{id}/firmar")
    public ResponseEntity<ContratoDTO> firmarContrato(@PathVariable Integer id) {
        Contrato contrato = contratoService.firmarContrato(id);
        return ResponseEntity.ok(contratoMapper.toDTO(contrato));
    }

    @PostMapping("/{id}/activar")
    public ResponseEntity<ContratoDTO> activarContrato(@PathVariable Integer id) {
        Contrato contrato = contratoService.activarContrato(id);
        return ResponseEntity.ok(contratoMapper.toDTO(contrato));
    }

    @PostMapping("/{id}/anular")
    public ResponseEntity<ContratoDTO> anularContrato(@PathVariable Integer id,
                                                    @RequestParam String motivo) {
        Contrato contrato = contratoService.anularContrato(id, motivo);
        return ResponseEntity.ok(contratoMapper.toDTO(contrato));
    }

    @PutMapping("/{id}/regenerar-archivo")
    public ResponseEntity<ContratoDTO> regenerarArchivo(@PathVariable Integer id) {
        Contrato contrato = contratoService.regenerarArchivo(id);
        return ResponseEntity.ok(contratoMapper.toDTO(contrato));
    }
} 
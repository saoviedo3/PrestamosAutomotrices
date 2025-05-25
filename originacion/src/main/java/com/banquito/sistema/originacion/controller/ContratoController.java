package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping("/generar")
    public ResponseEntity<Contrato> generarContrato(@RequestParam Integer idSolicitud,
                                                    @RequestParam(required = false) String condicionesEspeciales) {
        Contrato contrato = contratoService.generarContrato(idSolicitud, condicionesEspeciales);
        return ResponseEntity.status(HttpStatus.CREATED).body(contrato);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> buscarPorId(@PathVariable Integer id) {
        Contrato contrato = contratoService.buscarPorId(id);
        return ResponseEntity.ok(contrato);
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<Contrato> buscarPorSolicitud(@PathVariable Integer idSolicitud) {
        Contrato contrato = contratoService.buscarPorSolicitud(idSolicitud);
        return ResponseEntity.ok(contrato);
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> listarTodos(@RequestParam(required = false) String estado) {
        List<Contrato> contratos;

        if (estado != null && !estado.isEmpty()) {
            contratos = contratoService.listarPorEstado(estado);
        } else {
            contratos = contratoService.listarVigentes();
        }

        return ResponseEntity.ok(contratos);
    }

    @PostMapping("/{id}/firmar")
    public ResponseEntity<Contrato> firmarContrato(@PathVariable Integer id) {
        Contrato contrato = contratoService.firmarContrato(id);
        return ResponseEntity.ok(contrato);
    }

    @PostMapping("/{id}/activar")
    public ResponseEntity<Contrato> activarContrato(@PathVariable Integer id) {
        Contrato contrato = contratoService.activarContrato(id);
        return ResponseEntity.ok(contrato);
    }

    @PostMapping("/{id}/anular")
    public ResponseEntity<Contrato> anularContrato(@PathVariable Integer id,
                                                   @RequestParam String motivo) {
        Contrato contrato = contratoService.anularContrato(id, motivo);
        return ResponseEntity.ok(contrato);
    }

    @PutMapping("/{id}/regenerar-archivo")
    public ResponseEntity<Contrato> regenerarArchivo(@PathVariable Integer id) {
        Contrato contrato = contratoService.regenerarArchivo(id);
        return ResponseEntity.ok(contrato);
    }
}

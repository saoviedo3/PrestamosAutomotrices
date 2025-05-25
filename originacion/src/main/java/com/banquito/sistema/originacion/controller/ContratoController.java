package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Contrato;
import com.banquito.sistema.originacion.service.ContratoService;
import com.banquito.sistema.originacion.exception.ContratoNotFoundException;
import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping
    public ResponseEntity<Contrato> crear(@RequestBody Contrato contratoRequest) {
        try {
            // Mapeo manual de campos del request a la entidad
            Contrato contrato = new Contrato();
            contrato.setIdSolicitud(contratoRequest.getIdSolicitud());
            contrato.setRutaArchivo(contratoRequest.getRutaArchivo());
            contrato.setFechaFirma(contratoRequest.getFechaFirma());
            contrato.setCondicionEspecial(contratoRequest.getCondicionEspecial());
            
            Contrato contratoCreado = contratoService.createContrato(contrato);
            return ResponseEntity.status(HttpStatus.CREATED).body(contratoCreado);
        } catch (ValidationException | BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> buscarPorId(@PathVariable Integer id) {
        try {
            Contrato contrato = contratoService.getContratoById(id);
            return ResponseEntity.ok(contrato);
        } catch (ContratoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> listarTodos(@RequestParam(required = false) String estado) {
        try {
            List<Contrato> contratos;
            if (estado != null && !estado.isEmpty()) {
                contratos = contratoService.listarPorEstado(estado);
            } else {
                // Para este endpoint básico, retornamos lista vacía o implementar paginación
                contratos = List.of();
            }
            return ResponseEntity.ok(contratos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<Contrato>> listarPorSolicitud(@PathVariable Integer idSolicitud) {
        try {
            List<Contrato> contratos = contratoService.listarPorSolicitud(idSolicitud);
            return ResponseEntity.ok(contratos);
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/estado/{estado}")
    public ResponseEntity<Contrato> buscarPorSolicitudYEstado(@PathVariable Integer idSolicitud,
                                                             @PathVariable String estado) {
        try {
            Contrato contrato = contratoService.buscarPorSolicitudYEstado(idSolicitud, estado);
            return ResponseEntity.ok(contrato);
        } catch (ContratoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Contrato>> listarPorEstado(@PathVariable String estado) {
        try {
            List<Contrato> contratos = contratoService.listarPorEstado(estado);
            return ResponseEntity.ok(contratos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/firmar")
    public ResponseEntity<Contrato> firmarContrato(@PathVariable Integer id) {
        try {
            Contrato contrato = contratoService.firmarContrato(id);
            return ResponseEntity.ok(contrato);
        } catch (ContratoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<Contrato> activarContrato(@PathVariable Integer id) {
        try {
            Contrato contrato = contratoService.activarContrato(id);
            return ResponseEntity.ok(contrato);
        } catch (ContratoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/exists")
    public ResponseEntity<Boolean> existeContrato(@PathVariable Integer idSolicitud) {
        try {
            boolean existe = contratoService.existeContrato(idSolicitud);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/estado/{estado}/count")
    public ResponseEntity<Long> contarPorEstado(@PathVariable String estado) {
        try {
            long count = contratoService.contarPorEstado(estado);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fechas-generacion")
    public ResponseEntity<List<Contrato>> listarPorRangoFechasGeneracion(@RequestParam String fechaInicio,
                                                                        @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Contrato> contratos = contratoService.listarPorRangoFechasGeneracion(inicio, fin);
            return ResponseEntity.ok(contratos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fechas-firma")
    public ResponseEntity<List<Contrato>> listarPorRangoFechasFirma(@RequestParam String fechaInicio,
                                                                   @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Contrato> contratos = contratoService.listarPorRangoFechasFirma(inicio, fin);
            return ResponseEntity.ok(contratos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(ContratoNotFoundException.class)
    public ResponseEntity<Void> handleContratoNotFoundException(ContratoNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({BusinessException.class, ValidationException.class})
    public ResponseEntity<Void> handleBusinessException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
} 
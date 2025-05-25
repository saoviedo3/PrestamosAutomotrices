package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import com.banquito.sistema.originacion.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-creditos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SolicitudCreditoController {

    private final SolicitudCreditoService solicitudCreditoService;

    @GetMapping
    public ResponseEntity<List<SolicitudCredito>> findAll() {
        return ResponseEntity.ok(solicitudCreditoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCredito> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(solicitudCreditoService.buscarPorId(id));
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/numero/{numeroSolicitud}")
    public ResponseEntity<SolicitudCredito> findByNumeroSolicitud(@PathVariable String numeroSolicitud) {
        try {
            return ResponseEntity.ok(solicitudCreditoService.buscarPorNumeroSolicitud(numeroSolicitud));
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SolicitudCredito> crear(@RequestBody SolicitudCredito solicitudCredito) {
        try {
            SolicitudCredito solicitudCreada = solicitudCreditoService.crear(solicitudCredito);
            return ResponseEntity.ok(solicitudCreada);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCredito> actualizar(@PathVariable Integer id,
                                                     @RequestBody SolicitudCredito solicitudCredito) {
        try {
            SolicitudCredito solicitudActualizada = solicitudCreditoService.actualizar(id, solicitudCredito);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            solicitudCreditoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudCredito>> listarPorEstado(@PathVariable String estado) {
        List<SolicitudCredito> solicitudes = solicitudCreditoService.listarPorEstado(estado);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/cliente/{idClienteProspecto}")
    public ResponseEntity<List<SolicitudCredito>> listarPorClienteProspecto(@PathVariable Integer idClienteProspecto) {
        List<SolicitudCredito> solicitudes = solicitudCreditoService.listarPorClienteProspecto(idClienteProspecto);
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/vendedor/{idVendedor}")
    public ResponseEntity<List<SolicitudCredito>> listarPorVendedor(@PathVariable Integer idVendedor) {
        List<SolicitudCredito> solicitudes = solicitudCreditoService.listarPorVendedor(idVendedor);
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping("/{id}/enviar-revision")
    public ResponseEntity<SolicitudCredito> enviarARevision(@PathVariable Integer id) {
        try {
            SolicitudCredito solicitud = solicitudCreditoService.enviarARevision(id);
            return ResponseEntity.ok(solicitud);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudCredito> aprobar(@PathVariable Integer id,
                                                  @RequestParam(required = false) String observaciones) {
        try {
            SolicitudCredito solicitud = solicitudCreditoService.aprobar(id, observaciones);
            return ResponseEntity.ok(solicitud);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudCredito> rechazar(@PathVariable Integer id,
                                                   @RequestParam String motivo) {
        try {
            SolicitudCredito solicitud = solicitudCreditoService.rechazar(id, motivo);
            return ResponseEntity.ok(solicitud);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<SolicitudCredito> cancelar(@PathVariable Integer id) {
        try {
            SolicitudCredito solicitud = solicitudCreditoService.cancelar(id);
            return ResponseEntity.ok(solicitud);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/calcular-cuota")
    public ResponseEntity<BigDecimal> calcularCuotaMensual(@RequestParam BigDecimal monto,
                                                          @RequestParam BigDecimal tasa,
                                                          @RequestParam Integer plazo) {
        try {
            BigDecimal cuota = solicitudCreditoService.calcularCuotaMensual(monto, tasa, plazo);
            return ResponseEntity.ok(cuota);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

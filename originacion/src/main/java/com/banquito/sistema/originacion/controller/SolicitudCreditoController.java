package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.service.SolicitudCreditoService;
import com.banquito.sistema.originacion.exception.SolicitudCreditoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/solicitudes-creditos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SolicitudCreditoController {

    private final SolicitudCreditoService solicitudCreditoService;
    private final SolicitudCreditoMapper solicitudCreditoMapper;

    @GetMapping
    public ResponseEntity<List<SolicitudCredito>> findAll() {
        return ResponseEntity.ok(solicitudCreditoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCredito> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(solicitudCreditoService.findById(id));
        } catch (SolicitudCreditoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/numero/{numeroSolicitud}")
    public ResponseEntity<SolicitudCredito> findByNumeroSolicitud(@PathVariable String numeroSolicitud) {
        try {
            return ResponseEntity.ok(solicitudCreditoService.findByNumeroSolicitud(numeroSolicitud));
        } catch (SolicitudCreditoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SolicitudCredito> create(@RequestBody SolicitudCredito solicitudCredito) {
        try {
            return ResponseEntity.ok(solicitudCreditoService.create(solicitudCredito));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCredito> update(@PathVariable Integer id, @RequestBody SolicitudCredito solicitudCredito) {
        try {
            return ResponseEntity.ok(solicitudCreditoService.update(id, solicitudCredito));
        } catch (SolicitudCreditoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            solicitudCreditoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (SolicitudCreditoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudCredito>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(solicitudCreditoService.findByEstado(estado));
    }

    @GetMapping("/cliente/{idClienteProspecto}")
    public ResponseEntity<List<SolicitudCredito>> findByIdClienteProspecto(@PathVariable Integer idClienteProspecto) {
        return ResponseEntity.ok(solicitudCreditoService.findByIdClienteProspecto(idClienteProspecto));
    }

    @ExceptionHandler(SolicitudCreditoNotFoundException.class)
    public ResponseEntity<Void> handleSolicitudCreditoNotFoundException(SolicitudCreditoNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SolicitudCreditoDTO> crear(@RequestBody SolicitudCreditoDTO solicitudDTO) {
        SolicitudCredito solicitud = solicitudCreditoMapper.toEntity(solicitudDTO);
        SolicitudCredito solicitudCreada = solicitudCreditoService.crear(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitudCreditoMapper.toDTO(solicitudCreada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCreditoDTO> buscarPorId(@PathVariable Integer id) {
        SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(id);
        return ResponseEntity.ok(solicitudCreditoMapper.toDTO(solicitud));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudCreditoDTO>> listarTodas(@RequestParam(required = false) String estado) {
        List<SolicitudCredito> solicitudes;
        
        if (estado != null && !estado.isEmpty()) {
            solicitudes = solicitudCreditoService.listarPorEstado(estado);
        } else {
            solicitudes = solicitudCreditoService.listarPendientes();
        }
        
        List<SolicitudCreditoDTO> solicitudesDTO = solicitudes.stream()
                .map(solicitudCreditoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(solicitudesDTO);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<SolicitudCreditoDTO>> listarPorCliente(@PathVariable Integer idCliente) {
        List<SolicitudCredito> solicitudes = solicitudCreditoService.listarPorCliente(idCliente);
        List<SolicitudCreditoDTO> solicitudesDTO = solicitudes.stream()
                .map(solicitudCreditoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(solicitudesDTO);
    }

    @GetMapping("/vendedor/{idVendedor}")
    public ResponseEntity<List<SolicitudCreditoDTO>> listarPorVendedor(@PathVariable Integer idVendedor) {
        List<SolicitudCredito> solicitudes = solicitudCreditoService.listarPorVendedor(idVendedor);
        List<SolicitudCreditoDTO> solicitudesDTO = solicitudes.stream()
                .map(solicitudCreditoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(solicitudesDTO);
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<SolicitudCreditoDTO>> listarPorRangoFechas(
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        List<SolicitudCredito> solicitudes = solicitudCreditoService.listarPorRangoFechas(fechaInicio, fechaFin);
        List<SolicitudCreditoDTO> solicitudesDTO = solicitudes.stream()
                .map(solicitudCreditoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(solicitudesDTO);
    }

    @PostMapping("/{id}/enviar-revision")
    public ResponseEntity<SolicitudCreditoDTO> enviarARevision(@PathVariable Integer id) {
        SolicitudCredito solicitud = solicitudCreditoService.enviarARevision(id);
        return ResponseEntity.ok(solicitudCreditoMapper.toDTO(solicitud));
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudCreditoDTO> aprobar(@PathVariable Integer id,
                                                      @RequestParam(required = false) String observaciones) {
        SolicitudCredito solicitud = solicitudCreditoService.aprobar(id, observaciones);
        return ResponseEntity.ok(solicitudCreditoMapper.toDTO(solicitud));
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudCreditoDTO> rechazar(@PathVariable Integer id,
                                                       @RequestParam String motivo) {
        SolicitudCredito solicitud = solicitudCreditoService.rechazar(id, motivo);
        return ResponseEntity.ok(solicitudCreditoMapper.toDTO(solicitud));
    }

    @PostMapping("/{id}/marcar-desembolsada")
    public ResponseEntity<SolicitudCreditoDTO> marcarDesembolsada(@PathVariable Integer id) {
        SolicitudCredito solicitud = solicitudCreditoService.marcarDesembolsada(id);
        return ResponseEntity.ok(solicitudCreditoMapper.toDTO(solicitud));
    }

    @PostMapping("/calcular-cuota")
    public ResponseEntity<BigDecimal> calcularCuotaMensual(@RequestParam BigDecimal monto,
                                                         @RequestParam BigDecimal tasa,
                                                         @RequestParam Integer plazo) {
        BigDecimal cuota = solicitudCreditoService.calcularCuotaMensual(monto, tasa, plazo);
        return ResponseEntity.ok(cuota);
    }

    @PostMapping("/simular")
    public ResponseEntity<SolicitudCreditoDTO> simularCredito(@RequestParam BigDecimal monto,
                                                            @RequestParam BigDecimal tasa,
                                                            @RequestParam Integer plazo) {
        SolicitudCredito simulacion = solicitudCreditoService.simularCredito(monto, tasa, plazo);
        return ResponseEntity.ok(solicitudCreditoMapper.toDTO(simulacion));
    }

    @GetMapping("/contar/{estado}")
    public ResponseEntity<Long> contarPorEstado(@PathVariable String estado) {
        long cantidad = solicitudCreditoService.contarPorEstado(estado);
        return ResponseEntity.ok(cantidad);
    }
} 
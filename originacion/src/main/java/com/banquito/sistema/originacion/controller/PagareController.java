package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.service.PagareService;
import com.banquito.sistema.originacion.exception.PagareNotFoundException;
import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pagares")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagareController {

    private final PagareService pagareService;

    @PostMapping
    public ResponseEntity<Pagare> crear(@RequestBody Pagare pagareRequest) {
        try {
            // Mapeo manual de campos del request a la entidad
            Pagare pagare = new Pagare();
            pagare.setIdSolicitud(pagareRequest.getIdSolicitud());
            pagare.setNumeroCuota(pagareRequest.getNumeroCuota());
            pagare.setRutaArchivo(pagareRequest.getRutaArchivo());
            
            Pagare pagareCreado = pagareService.createPagare(pagare);
            return ResponseEntity.status(HttpStatus.CREATED).body(pagareCreado);
        } catch (ValidationException | BusinessException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagare> buscarPorId(@PathVariable Integer id) {
        try {
            Pagare pagare = pagareService.getPagareById(id);
            return ResponseEntity.ok(pagare);
        } catch (PagareNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Pagare>> listarTodos() {
        try {
            // Para este endpoint básico, podríamos listar todos los pagarés
            // pero por ahora retornamos una lista vacía o implementar paginación
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<Pagare>> listarPorSolicitud(@PathVariable Integer idSolicitud) {
        try {
            List<Pagare> pagares = pagareService.listarPorSolicitud(idSolicitud);
            return ResponseEntity.ok(pagares);
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/cuota/{numeroCuota}")
    public ResponseEntity<Pagare> buscarPorSolicitudYCuota(@PathVariable Integer idSolicitud, 
                                                          @PathVariable Integer numeroCuota) {
        try {
            Pagare pagare = pagareService.buscarPorSolicitudYCuota(idSolicitud, numeroCuota);
            return ResponseEntity.ok(pagare);
        } catch (PagareNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/count")
    public ResponseEntity<Long> contarPorSolicitud(@PathVariable Integer idSolicitud) {
        try {
            long count = pagareService.contarPorSolicitud(idSolicitud);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}/cuota/{numeroCuota}/exists")
    public ResponseEntity<Boolean> existePagare(@PathVariable Integer idSolicitud, 
                                               @PathVariable Integer numeroCuota) {
        try {
            boolean existe = pagareService.existePagare(idSolicitud, numeroCuota);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Pagare>> listarPorRangoFechas(@RequestParam String fechaInicio,
                                                            @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Pagare> pagares = pagareService.listarPorRangoFechas(inicio, fin);
            return ResponseEntity.ok(pagares);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(PagareNotFoundException.class)
    public ResponseEntity<Void> handlePagareNotFoundException(PagareNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({BusinessException.class, ValidationException.class})
    public ResponseEntity<Void> handleBusinessException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
} 
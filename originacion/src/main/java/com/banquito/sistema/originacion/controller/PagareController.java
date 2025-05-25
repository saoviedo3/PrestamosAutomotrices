package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.service.PagareService;
import com.banquito.sistema.originacion.exception.PagareNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pagares")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagareController {

    private final PagareService pagareService;

    @PostMapping("/generar-individual")
    public ResponseEntity<Pagare> generarPagareIndividual(@RequestParam Integer idSolicitud,
                                                         @RequestParam Integer numeroCuota) {
        Pagare pagare = pagareService.generarPagareIndividual(idSolicitud, numeroCuota);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagare);
    }

    @PostMapping("/generar-por-solicitud")
    public ResponseEntity<List<Pagare>> generarPagaresPorSolicitud(@RequestParam Integer idSolicitud) {
        List<Pagare> pagares = pagareService.generarPagaresPorSolicitud(idSolicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagares);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagare> buscarPorId(@PathVariable Integer id) {
        try {
            Pagare pagare = pagareService.buscarPorId(id);
            return ResponseEntity.ok(pagare);
        } catch (PagareNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<Pagare>> listarPorSolicitud(@PathVariable Integer idSolicitud) {
        List<Pagare> pagares = pagareService.listarPorSolicitud(idSolicitud);
        return ResponseEntity.ok(pagares);
    }

    @GetMapping("/solicitud/{idSolicitud}/ordenados")
    public ResponseEntity<List<Pagare>> listarPorSolicitudOrdenados(@PathVariable Integer idSolicitud) {
        List<Pagare> pagares = pagareService.listarPorSolicitudOrdenados(idSolicitud);
        return ResponseEntity.ok(pagares);
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

    @PostMapping("/{id}/regenerar-archivo")
    public ResponseEntity<Pagare> regenerarArchivo(@PathVariable Integer id) {
        try {
            Pagare pagare = pagareService.regenerarArchivo(id);
            return ResponseEntity.ok(pagare);
        } catch (PagareNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/solicitud/{idSolicitud}/regenerar-archivos")
    public ResponseEntity<List<Pagare>> regenerarArchivosPorSolicitud(@PathVariable Integer idSolicitud) {
        List<Pagare> pagares = pagareService.regenerarArchivosPorSolicitud(idSolicitud);
        return ResponseEntity.ok(pagares);
    }

    @GetMapping("/solicitud/{idSolicitud}/validar-integridad")
    public ResponseEntity<Boolean> validarIntegridad(@PathVariable Integer idSolicitud) {
        boolean valido = pagareService.validarIntegridadPagares(idSolicitud);
        return ResponseEntity.ok(valido);
    }

    @GetMapping("/solicitud/{idSolicitud}/cuotas-faltantes")
    public ResponseEntity<List<Integer>> obtenerCuotasFaltantes(@PathVariable Integer idSolicitud) {
        List<Integer> cuotasFaltantes = pagareService.obtenerCuotasFaltantes(idSolicitud);
        return ResponseEntity.ok(cuotasFaltantes);
    }

    @GetMapping("/solicitud/{idSolicitud}/contar")
    public ResponseEntity<Long> contarPorSolicitud(@PathVariable Integer idSolicitud) {
        long cantidad = pagareService.contarPagaresPorSolicitud(idSolicitud);
        return ResponseEntity.ok(cantidad);
    }

    @GetMapping("/solicitud/{idSolicitud}/existe-cuota/{numeroCuota}")
    public ResponseEntity<Boolean> existePorSolicitudYCuota(@PathVariable Integer idSolicitud,
                                                            @PathVariable Integer numeroCuota) {
        boolean existe = pagareService.existePagarePorSolicitudYCuota(idSolicitud, numeroCuota);
        return ResponseEntity.ok(existe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            pagareService.eliminarPagare(id);
            return ResponseEntity.noContent().build();
        } catch (PagareNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<Void> eliminarPorSolicitud(@PathVariable Integer idSolicitud) {
        pagareService.eliminarPagaresPorSolicitud(idSolicitud);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(PagareNotFoundException.class)
    public ResponseEntity<Void> handlePagareNotFoundException(PagareNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
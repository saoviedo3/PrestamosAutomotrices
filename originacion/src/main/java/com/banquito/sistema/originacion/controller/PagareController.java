package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.dto.PagareDTO;
import com.banquito.sistema.originacion.mapper.PagareMapper;
import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.service.PagareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagares")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagareController {

    private final PagareService pagareService;
    private final PagareMapper pagareMapper;

    @PostMapping("/generar-individual")
    public ResponseEntity<PagareDTO> generarPagareIndividual(@RequestParam Integer idSolicitud,
                                                           @RequestParam Integer numeroCuota) {
        Pagare pagare = pagareService.generarPagareIndividual(idSolicitud, numeroCuota);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagareMapper.toDTO(pagare));
    }

    @PostMapping("/generar-por-solicitud")
    public ResponseEntity<List<PagareDTO>> generarPagaresPorSolicitud(@RequestParam Integer idSolicitud) {
        List<Pagare> pagares = pagareService.generarPagaresPorSolicitud(idSolicitud);
        List<PagareDTO> pagaresDTO = pagares.stream()
                .map(pagareMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(pagaresDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagareDTO> buscarPorId(@PathVariable Integer id) {
        Pagare pagare = pagareService.buscarPorId(id);
        return ResponseEntity.ok(pagareMapper.toDTO(pagare));
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<PagareDTO>> listarPorSolicitud(@PathVariable Integer idSolicitud) {
        List<Pagare> pagares = pagareService.listarPorSolicitud(idSolicitud);
        List<PagareDTO> pagaresDTO = pagares.stream()
                .map(pagareMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(pagaresDTO);
    }

    @GetMapping("/solicitud/{idSolicitud}/ordenados")
    public ResponseEntity<List<PagareDTO>> listarPorSolicitudOrdenados(@PathVariable Integer idSolicitud) {
        List<Pagare> pagares = pagareService.listarPorSolicitudOrdenados(idSolicitud);
        List<PagareDTO> pagaresDTO = pagares.stream()
                .map(pagareMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(pagaresDTO);
    }

    @GetMapping("/solicitud/{idSolicitud}/cuota/{numeroCuota}")
    public ResponseEntity<PagareDTO> buscarPorSolicitudYCuota(@PathVariable Integer idSolicitud,
                                                            @PathVariable Integer numeroCuota) {
        Pagare pagare = pagareService.buscarPorSolicitudYCuota(idSolicitud, numeroCuota);
        return ResponseEntity.ok(pagareMapper.toDTO(pagare));
    }

    @PostMapping("/{id}/regenerar-archivo")
    public ResponseEntity<PagareDTO> regenerarArchivo(@PathVariable Integer id) {
        Pagare pagare = pagareService.regenerarArchivo(id);
        return ResponseEntity.ok(pagareMapper.toDTO(pagare));
    }

    @PostMapping("/solicitud/{idSolicitud}/regenerar-archivos")
    public ResponseEntity<List<PagareDTO>> regenerarArchivosPorSolicitud(@PathVariable Integer idSolicitud) {
        List<Pagare> pagares = pagareService.regenerarArchivosPorSolicitud(idSolicitud);
        List<PagareDTO> pagaresDTO = pagares.stream()
                .map(pagareMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(pagaresDTO);
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
        pagareService.eliminarPagare(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<Void> eliminarPorSolicitud(@PathVariable Integer idSolicitud) {
        pagareService.eliminarPagaresPorSolicitud(idSolicitud);
        return ResponseEntity.noContent().build();
    }
} 
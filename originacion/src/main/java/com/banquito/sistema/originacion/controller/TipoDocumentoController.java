package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.dto.TipoDocumentoDTO;
import com.banquito.sistema.originacion.mapper.TipoDocumentoMapper;
import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.service.TipoDocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipos-documento")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;
    private final TipoDocumentoMapper tipoDocumentoMapper;

    @PostMapping
    public ResponseEntity<TipoDocumentoDTO> crear(@RequestBody TipoDocumentoDTO tipoDocumentoDTO) {
        TipoDocumento tipoDocumento = tipoDocumentoMapper.toEntity(tipoDocumentoDTO);
        TipoDocumento tipoDocumentoCreado = tipoDocumentoService.crear(tipoDocumento);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tipoDocumentoMapper.toDTO(tipoDocumentoCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumentoDTO> actualizar(@PathVariable Integer id,
                                                     @RequestBody TipoDocumentoDTO tipoDocumentoDTO) {
        TipoDocumento tipoDocumento = tipoDocumentoMapper.toEntity(tipoDocumentoDTO);
        TipoDocumento tipoDocumentoActualizado = tipoDocumentoService.actualizar(id, tipoDocumento);
        return ResponseEntity.ok(tipoDocumentoMapper.toDTO(tipoDocumentoActualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumentoDTO> buscarPorId(@PathVariable Integer id) {
        TipoDocumento tipoDocumento = tipoDocumentoService.buscarPorId(id);
        return ResponseEntity.ok(tipoDocumentoMapper.toDTO(tipoDocumento));
    }

    @GetMapping
    public ResponseEntity<List<TipoDocumentoDTO>> listarTodos(@RequestParam(required = false) String estado) {
        List<TipoDocumento> tiposDocumento;
        
        if (estado != null && !estado.isEmpty()) {
            tiposDocumento = tipoDocumentoService.listarPorEstado(estado);
        } else {
            tiposDocumento = tipoDocumentoService.listarActivos();
        }
        
        List<TipoDocumentoDTO> tiposDocumentoDTO = tiposDocumento.stream()
                .map(tipoDocumentoMapper::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(tiposDocumentoDTO);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoDocumentoDTO> buscarPorNombre(@PathVariable String nombre) {
        TipoDocumento tipoDocumento = tipoDocumentoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(tipoDocumentoMapper.toDTO(tipoDocumento));
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<TipoDocumentoDTO> activar(@PathVariable Integer id) {
        TipoDocumento tipoDocumento = tipoDocumentoService.activar(id);
        return ResponseEntity.ok(tipoDocumentoMapper.toDTO(tipoDocumento));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<TipoDocumentoDTO> desactivar(@PathVariable Integer id) {
        TipoDocumento tipoDocumento = tipoDocumentoService.desactivar(id);
        return ResponseEntity.ok(tipoDocumentoMapper.toDTO(tipoDocumento));
    }
}
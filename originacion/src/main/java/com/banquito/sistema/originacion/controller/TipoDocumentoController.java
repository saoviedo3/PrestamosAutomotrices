package com.banquito.sistema.originacion.controller;

import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.service.TipoDocumentoService;
import com.banquito.sistema.originacion.exception.TipoDocumentoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tipos-documentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    @PostMapping
    public ResponseEntity<TipoDocumento> crear(@RequestBody TipoDocumento tipoDocumento) {
        try {
            TipoDocumento tipoDocumentoCreado = tipoDocumentoService.crear(tipoDocumento);
            return ResponseEntity.status(HttpStatus.CREATED).body(tipoDocumentoCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumento> actualizar(@PathVariable Integer id,
            @RequestBody TipoDocumento tipoDocumento) {
        try {
            TipoDocumento tipoDocumentoActualizado = tipoDocumentoService.actualizar(id, tipoDocumento);
            return ResponseEntity.ok(tipoDocumentoActualizado);
        } catch (TipoDocumentoNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumento> buscarPorId(@PathVariable Integer id) {
        try {
            TipoDocumento tipoDocumento = tipoDocumentoService.buscarPorId(id);
            return ResponseEntity.ok(tipoDocumento);
        } catch (TipoDocumentoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TipoDocumento>> listarTodos(@RequestParam(required = false) String estado) {
        try {
            List<TipoDocumento> tiposDocumento;

            if (estado != null && !estado.isEmpty()) {
                tiposDocumento = tipoDocumentoService.listarPorEstado(estado);
            } else {
                tiposDocumento = tipoDocumentoService.listarActivos();
            }

            return ResponseEntity.ok(tiposDocumento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoDocumento> buscarPorNombre(@PathVariable String nombre) {
        try {
            TipoDocumento tipoDocumento = tipoDocumentoService.buscarPorNombre(nombre);
            return ResponseEntity.ok(tipoDocumento);
        } catch (TipoDocumentoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<TipoDocumento> activar(@PathVariable Integer id) {
        try {
            TipoDocumento tipoDocumento = tipoDocumentoService.activar(id);
            return ResponseEntity.ok(tipoDocumento);
        } catch (TipoDocumentoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<TipoDocumento> desactivar(@PathVariable Integer id) {
        try {
            TipoDocumento tipoDocumento = tipoDocumentoService.desactivar(id);
            return ResponseEntity.ok(tipoDocumento);
        } catch (TipoDocumentoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            tipoDocumentoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (TipoDocumentoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(TipoDocumentoNotFoundException.class)
    public ResponseEntity<Void> handleTipoDocumentoNotFoundException(TipoDocumentoNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
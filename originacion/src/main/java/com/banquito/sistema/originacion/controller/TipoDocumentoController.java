package com.banquito.sistema.originacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.service.TipoDocumentoService;

@RestController
@RequestMapping("/api/tipos-documento")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    /**
     * Obtiene todos los tipos de documento
     */
    @GetMapping
    public ResponseEntity<List<TipoDocumento>> findAll() {
        List<TipoDocumento> tipos = this.tipoDocumentoService.findAll();
        return ResponseEntity.ok(tipos);
    }

    /**
     * Obtiene un tipo de documento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumento> findById(@PathVariable Long id) {
        TipoDocumento tipo = this.tipoDocumentoService.findById(id);
        return ResponseEntity.ok(tipo);
    }

    /**
     * Obtiene tipos de documento activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<TipoDocumento>> findActivos() {
        List<TipoDocumento> tipos = this.tipoDocumentoService.findActivos();
        return ResponseEntity.ok(tipos);
    }

    /**
     * Obtiene un tipo de documento por nombre
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoDocumento> findByNombre(@PathVariable String nombre) {
        TipoDocumento tipo = this.tipoDocumentoService.findByNombre(nombre);
        return ResponseEntity.ok(tipo);
    }

    /**
     * Obtiene los tipos de documentos obligatorios
     */
    @GetMapping("/obligatorios")
    public ResponseEntity<List<TipoDocumento>> findDocumentosObligatorios() {
        List<TipoDocumento> obligatorios = this.tipoDocumentoService.findDocumentosObligatorios();
        return ResponseEntity.ok(obligatorios);
    }

    /**
     * Crea un nuevo tipo de documento
     */
    @PostMapping
    public ResponseEntity<TipoDocumento> create(@RequestBody TipoDocumento tipoDocumento) {
        try {
            TipoDocumento nuevo = this.tipoDocumentoService.create(tipoDocumento);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un tipo de documento
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumento> update(@PathVariable Long id, @RequestBody TipoDocumento tipoDocumento) {
        try {
            tipoDocumento.setId(id);
            TipoDocumento actualizado = this.tipoDocumentoService.update(tipoDocumento);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Desactiva un tipo de documento
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<TipoDocumento> desactivar(@PathVariable Long id) {
        try {
            TipoDocumento desactivado = this.tipoDocumentoService.desactivar(id);
            return ResponseEntity.ok(desactivado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Activa un tipo de documento
     */
    @PutMapping("/{id}/activar")
    public ResponseEntity<TipoDocumento> activar(@PathVariable Long id) {
        try {
            TipoDocumento activado = this.tipoDocumentoService.activar(id);
            return ResponseEntity.ok(activado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Inicializa los tipos de documentos básicos del sistema
     */
    @PostMapping("/inicializar-basicos")
    public ResponseEntity<Void> inicializarTiposDocumentosBasicos() {
        try {
            this.tipoDocumentoService.inicializarTiposDocumentosBasicos();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Verifica si un tipo de documento existe y está activo
     */
    @GetMapping("/existe-activo")
    public ResponseEntity<Boolean> existeYEstaActivo(@RequestParam String nombre) {
        boolean existe = this.tipoDocumentoService.existeYEstaActivo(nombre);
        return ResponseEntity.ok(existe);
    }
} 
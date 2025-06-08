package com.banquito.sistema.contratos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.sistema.contratos.exception.InvalidDocumentTypeException;
import com.banquito.sistema.contratos.model.TipoDocumento;
import com.banquito.sistema.contratos.repository.TipoDocumentoRepository;

@Service
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public TipoDocumento findById(Long id) {
        return this.tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new InvalidDocumentTypeException("Tipo de documento con ID: " + id + " no encontrado"));
    }

    public List<TipoDocumento> findAll() {
        return this.tipoDocumentoRepository.findAll();
    }

    public List<TipoDocumento> findActivos() {
        return this.tipoDocumentoRepository.findByEstadoOrderByNombre("Activo");
    }

    public TipoDocumento findByNombre(String nombre) {
        return this.tipoDocumentoRepository.findByNombre(nombre)
                .orElseThrow(() -> new InvalidDocumentTypeException("Tipo de documento '" + nombre + "' no encontrado"));
    }

    /**
     * Retorna los tipos de documentos obligatorios según las regulaciones bancarias
     * Regla de negocio: Documentos mínimos requeridos para préstamos automotrices
     */
    public List<TipoDocumento> findDocumentosObligatorios() {
        // En un sistema real, esto podría ser una consulta a la base de datos
        // Por ahora, retornamos los tipos activos que son obligatorios por regulación
        return this.tipoDocumentoRepository.findByEstado("Activo").stream()
                .filter(this::esDocumentoObligatorio)
                .toList();
    }

    /**
     * Determina si un tipo de documento es obligatorio según regulaciones
     */
    private boolean esDocumentoObligatorio(TipoDocumento tipo) {
        String nombre = tipo.getNombre().toLowerCase();
        
        // Documentos obligatorios según regulaciones bancarias
        return nombre.contains("cedula") || 
               nombre.contains("identidad") ||
               nombre.contains("comprobante") && nombre.contains("ingreso") ||
               nombre.contains("licencia") && nombre.contains("conducir") ||
               nombre.contains("avaluo") && nombre.contains("vehiculo");
    }

    @Transactional
    public TipoDocumento create(TipoDocumento tipoDocumento) {
        // Validar que no exista otro tipo de documento con el mismo nombre
        if (this.tipoDocumentoRepository.existsByNombre(tipoDocumento.getNombre())) {
            throw new InvalidDocumentTypeException("Ya existe un tipo de documento con el nombre: " + tipoDocumento.getNombre());
        }

        return this.tipoDocumentoRepository.save(tipoDocumento);
    }

    @Transactional
    public TipoDocumento update(TipoDocumento tipoDocumento) {
        TipoDocumento existente = findById(tipoDocumento.getId());
        
        // Validar que no exista otro tipo de documento con el mismo nombre (excluyendo el actual)
        if (!existente.getNombre().equals(tipoDocumento.getNombre()) && 
            this.tipoDocumentoRepository.existsByNombre(tipoDocumento.getNombre())) {
            throw new InvalidDocumentTypeException("Ya existe un tipo de documento con el nombre: " + tipoDocumento.getNombre());
        }

        return this.tipoDocumentoRepository.save(tipoDocumento);
    }

    /**
     * Desactiva un tipo de documento en lugar de eliminarlo
     * Regla de negocio: No se eliminan tipos de documentos para mantener trazabilidad
     */
    @Transactional
    public TipoDocumento desactivar(Long id) {
        TipoDocumento tipoDocumento = findById(id);
        tipoDocumento.setEstado("Inactivo");
        return this.tipoDocumentoRepository.save(tipoDocumento);
    }

    /**
     * Activa un tipo de documento
     */
    @Transactional
    public TipoDocumento activar(Long id) {
        TipoDocumento tipoDocumento = findById(id);
        tipoDocumento.setEstado("Activo");
        return this.tipoDocumentoRepository.save(tipoDocumento);
    }

    /**
     * Inicializa los tipos de documentos básicos del sistema
     * Útil para configuración inicial
     */
    @Transactional
    public void inicializarTiposDocumentosBasicos() {
        String[] tiposBasicos = {
            "Cédula de Identidad",
            "Licencia de Conducir", 
            "Comprobante de Ingresos",
            "Avalúo del Vehículo",
            "Contrato de Trabajo",
            "Referencias Comerciales",
            "Autorización Bureau de Crédito",
            "Póliza de Seguro",
            "Contrato Firmado",
            "Pagaré"
        };

        for (String nombreTipo : tiposBasicos) {
            if (!this.tipoDocumentoRepository.existsByNombre(nombreTipo)) {
                TipoDocumento tipo = new TipoDocumento();
                tipo.setNombre(nombreTipo);
                tipo.setEstado("Activo");
                this.tipoDocumentoRepository.save(tipo);
            }
        }
    }

    /**
     * Verifica si un tipo de documento existe y está activo
     */
    public boolean existeYEstaActivo(String nombre) {
        return this.tipoDocumentoRepository.findByNombre(nombre)
                .map(tipo -> "Activo".equals(tipo.getEstado()))
                .orElse(false);
    }
} 
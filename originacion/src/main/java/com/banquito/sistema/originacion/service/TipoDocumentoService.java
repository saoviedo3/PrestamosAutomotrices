package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.TipoDocumento;
import com.banquito.sistema.originacion.repository.TipoDocumentoRepository;
import com.banquito.sistema.originacion.exception.TipoDocumentoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    // Estados
    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_INACTIVO = "INACTIVO";

    /**
     * Crear nuevo tipo de documento
     */
    public TipoDocumento crear(TipoDocumento tipoDocumento) {
        try {
            validarTipoDocumento(tipoDocumento);
            validarNombreUnico(tipoDocumento.getNombre());
            
            tipoDocumento.setEstado(ESTADO_ACTIVO);
            return tipoDocumentoRepository.save(tipoDocumento);
        } catch (Exception e) {
            throw new BusinessException("Error al crear tipo de documento: " + e.getMessage(), "CREAR_TIPO_DOCUMENTO");
        }
    }

    /**
     * Actualizar tipo de documento existente
     */
    public TipoDocumento actualizar(Integer id, TipoDocumento tipoDocumentoActualizado) {
        try {
            TipoDocumento tipoExistente = buscarPorId(id);
            
            validarTipoDocumento(tipoDocumentoActualizado);
            
            // Validar nombre único solo si cambió
            if (!tipoExistente.getNombre().equals(tipoDocumentoActualizado.getNombre())) {
                validarNombreUnico(tipoDocumentoActualizado.getNombre());
            }
            
            tipoExistente.setNombre(tipoDocumentoActualizado.getNombre());
            tipoExistente.setDescripcion(tipoDocumentoActualizado.getDescripcion());
            
            return tipoDocumentoRepository.save(tipoExistente);
        } catch (Exception e) {
            throw new BusinessException("Error al actualizar tipo de documento: " + e.getMessage(), "ACTUALIZAR_TIPO_DOCUMENTO");
        }
    }

    /**
     * Buscar tipo de documento por ID
     */
    @Transactional(readOnly = true)
    public TipoDocumento buscarPorId(Integer id) {
        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new TipoDocumentoNotFoundException(id.toString(), "ID"));
    }

    /**
     * Buscar tipo de documento por nombre
     */
    @Transactional(readOnly = true)
    public TipoDocumento buscarPorNombre(String nombre) {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByNombre(nombre);
        if (tipoDocumento == null) {
            throw new TipoDocumentoNotFoundException(nombre, "nombre");
        }
        return tipoDocumento;
    }

    /**
     * Listar tipos de documentos activos
     */
    @Transactional(readOnly = true)
    public List<TipoDocumento> listarActivos() {
        return tipoDocumentoRepository.findByEstado(ESTADO_ACTIVO);
    }

    /**
     * Listar tipos de documentos por estado
     */
    @Transactional(readOnly = true)
    public List<TipoDocumento> listarPorEstado(String estado) {
        return tipoDocumentoRepository.findByEstado(estado);
    }

    /**
     * Listar todos los tipos de documentos
     */
    @Transactional(readOnly = true)
    public List<TipoDocumento> listarTodos() {
        return tipoDocumentoRepository.findAll();
    }

    /**
     * Buscar tipos de documentos por nombre (parcial)
     */
    @Transactional(readOnly = true)
    public List<TipoDocumento> buscarPorNombreParcial(String nombre) {
        return tipoDocumentoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Activar tipo de documento
     */
    public TipoDocumento activar(Integer id) {
        TipoDocumento tipoDocumento = buscarPorId(id);
        tipoDocumento.setEstado(ESTADO_ACTIVO);
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    /**
     * Desactivar tipo de documento
     */
    public TipoDocumento desactivar(Integer id) {
        TipoDocumento tipoDocumento = buscarPorId(id);
        tipoDocumento.setEstado(ESTADO_INACTIVO);
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    /**
     * Eliminar tipo de documento
     */
    public void eliminar(Integer id) {
        try {
            TipoDocumento tipoDocumento = buscarPorId(id);
            
            // Verificar que no esté en uso antes de eliminar
            // En una implementación real, aquí verificarías si existen DocumentoAdjunto 
            // que usen este tipo de documento
            
            tipoDocumentoRepository.delete(tipoDocumento);
        } catch (Exception e) {
            throw new BusinessException("Error al eliminar tipo de documento: " + e.getMessage(), "ELIMINAR_TIPO_DOCUMENTO");
        }
    }

    /**
     * Verificar si existe tipo de documento por nombre
     */
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return tipoDocumentoRepository.existsByNombre(nombre);
    }

    /**
     * Obtener tipos de documentos requeridos para solicitudes de crédito
     */
    @Transactional(readOnly = true)
    public List<TipoDocumento> obtenerTiposRequeridos() {
        // Definir qué tipos de documentos son requeridos para una solicitud
        // En una implementación real, esto podría estar configurado en base de datos
        List<String> nombresRequeridos = List.of(
            "CEDULA_IDENTIDAD",
            "COMPROBANTE_INGRESOS", 
            "REFERENCIAS_COMERCIALES",
            "ESTADO_CUENTA_BANCARIO",
            "AUTORIZACION_CENTRALES_RIESGO"
        );
        
        return nombresRequeridos.stream()
                .map(nombre -> {
                    try {
                        return buscarPorNombre(nombre);
                    } catch (NotFoundException e) {
                        return null;
                    }
                })
                .filter(tipo -> tipo != null && ESTADO_ACTIVO.equals(tipo.getEstado()))
                .toList();
    }

    /**
     * Crear tipos de documentos predeterminados
     */
    public void crearTiposPredeterminados() {
        try {
            String[][] tiposPredeterminados = {
                {"CEDULA_IDENTIDAD", "Cédula de identidad del solicitante"},
                {"COMPROBANTE_INGRESOS", "Comprobante de ingresos (rol de pagos, certificado laboral)"},
                {"REFERENCIAS_COMERCIALES", "Referencias comerciales"},
                {"ESTADO_CUENTA_BANCARIO", "Estado de cuenta bancario de los últimos 3 meses"},
                {"AUTORIZACION_CENTRALES_RIESGO", "Autorización consulta centrales de riesgo"},
                {"MATRICULA_VEHICULO", "Matrícula del vehículo a financiar"},
                {"AVALUO_VEHICULO", "Avalúo comercial del vehículo"},
                {"SEGURO_VEHICULO", "Póliza de seguro del vehículo"},
                {"LICENCIA_CONDUCIR", "Licencia de conducir vigente"},
                {"CONTRATO_TRABAJO", "Contrato de trabajo vigente"}
            };
            
            for (String[] tipoPredeterminado : tiposPredeterminados) {
                String nombre = tipoPredeterminado[0];
                String descripcion = tipoPredeterminado[1];
                
                if (!existePorNombre(nombre)) {
                    TipoDocumento tipoDocumento = new TipoDocumento();
                    tipoDocumento.setNombre(nombre);
                    tipoDocumento.setDescripcion(descripcion);
                    tipoDocumento.setEstado(ESTADO_ACTIVO);
                    tipoDocumentoRepository.save(tipoDocumento);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("Error al crear tipos predeterminados: " + e.getMessage(), "CREAR_TIPOS_PREDETERMINADOS");
        }
    }

    /**
     * Validar que un tipo de documento puede ser usado
     */
    @Transactional(readOnly = true)
    public boolean puedeUsarse(Integer id) {
        try {
            TipoDocumento tipoDocumento = buscarPorId(id);
            return ESTADO_ACTIVO.equals(tipoDocumento.getEstado());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtener estadísticas de uso de tipos de documentos
     */
    @Transactional(readOnly = true)
    public long contarPorEstado(String estado) {
        return tipoDocumentoRepository.findByEstado(estado).size();
    }

    // Métodos privados de validación
    private void validarTipoDocumento(TipoDocumento tipoDocumento) {
        if (tipoDocumento.getNombre() == null || tipoDocumento.getNombre().trim().isEmpty()) {
            throw new ValidationException("nombre", "requerido");
        }
        
        if (tipoDocumento.getNombre().length() > 40) {
            throw new ValidationException("nombre", "no puede exceder 40 caracteres");
        }
        
        if (tipoDocumento.getDescripcion() != null && tipoDocumento.getDescripcion().length() > 150) {
            throw new ValidationException("descripcion", "no puede exceder 150 caracteres");
        }
        
        // Validar formato del nombre (solo letras, números y guiones bajos)
        if (!tipoDocumento.getNombre().matches("^[A-Z0-9_]+$")) {
            throw new ValidationException("nombre", "solo puede contener letras mayúsculas, números y guiones bajos");
        }
    }

    private void validarNombreUnico(String nombre) {
        if (existePorNombre(nombre)) {
            throw new ValidationException("nombre", "ya existe un tipo de documento con este nombre");
        }
    }
} 
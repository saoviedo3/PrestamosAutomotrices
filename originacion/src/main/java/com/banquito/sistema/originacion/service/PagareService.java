package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.NotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.repository.PagareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagareService {

    private final PagareRepository pagareRepository;
    private final SolicitudCreditoService solicitudCreditoService;

    // Estados de solicitud requeridos
    private static final String SOLICITUD_APROBADA = "APROBADA";
    private static final String SOLICITUD_DESEMBOLSADA = "DESEMBOLSADA";

    /**
     * Generar pagarés para una solicitud aprobada
     */
    public List<Pagare> generarPagaresPorSolicitud(Integer idSolicitud) {
        try {
            // Validar que la solicitud esté aprobada o desembolsada
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            
            if (!SOLICITUD_APROBADA.equals(solicitud.getEstado()) && 
                !SOLICITUD_DESEMBOLSADA.equals(solicitud.getEstado())) {
                throw new BusinessException("Solo se pueden generar pagarés para solicitudes aprobadas o desembolsadas", "GENERAR_PAGARES");
            }
            
            // Verificar que no existan ya pagarés para esta solicitud
            if (pagareRepository.countByIdSolicitud(idSolicitud) > 0) {
                throw new BusinessException("Ya existen pagarés generados para esta solicitud", "GENERAR_PAGARES");
            }
            
            List<Pagare> pagares = new ArrayList<>();
            
            // Generar un pagaré por cada cuota del crédito
            for (int cuota = 1; cuota <= solicitud.getPlazoMeses().intValue(); cuota++) {
                Pagare pagare = new Pagare();
                pagare.setIdSolicitud(idSolicitud);
                pagare.setNumeroCuota(cuota);
                pagare.setFechaGenerado(LocalDateTime.now());
                
                // Generar ruta del archivo
                String rutaArchivo = generarRutaArchivoPagare(idSolicitud, cuota);
                pagare.setRutaArchivo(rutaArchivo);
                
                pagares.add(pagare);
            }
            
            // Guardar todos los pagarés
            return pagareRepository.saveAll(pagares);
            
        } catch (Exception e) {
            throw new BusinessException("Error al generar pagarés: " + e.getMessage(), "GENERAR_PAGARES");
        }
    }

    /**
     * Generar pagaré individual por cuota
     */
    public Pagare generarPagareIndividual(Integer idSolicitud, Integer numeroCuota) {
        try {
            // Validar que la solicitud esté aprobada o desembolsada
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            
            if (!SOLICITUD_APROBADA.equals(solicitud.getEstado()) && 
                !SOLICITUD_DESEMBOLSADA.equals(solicitud.getEstado())) {
                throw new BusinessException("Solo se pueden generar pagarés para solicitudes aprobadas o desembolsadas", "GENERAR_PAGARE");
            }
            
            // Validar número de cuota
            if (numeroCuota < 1 || numeroCuota > solicitud.getPlazoMeses().intValue()) {
                throw new ValidationException("numeroCuota", "debe estar entre 1 y " + solicitud.getPlazoMeses());
            }
            
            // Verificar que no exista ya un pagaré para esta cuota
            if (pagareRepository.existsByIdSolicitudAndNumeroCuota(idSolicitud, numeroCuota)) {
                throw new BusinessException("Ya existe un pagaré para esta cuota", "GENERAR_PAGARE");
            }
            
            Pagare pagare = new Pagare();
            pagare.setIdSolicitud(idSolicitud);
            pagare.setNumeroCuota(numeroCuota);
            pagare.setFechaGenerado(LocalDateTime.now());
            
            // Generar ruta del archivo
            String rutaArchivo = generarRutaArchivoPagare(idSolicitud, numeroCuota);
            pagare.setRutaArchivo(rutaArchivo);
            
            return pagareRepository.save(pagare);
            
        } catch (Exception e) {
            throw new BusinessException("Error al generar pagaré: " + e.getMessage(), "GENERAR_PAGARE");
        }
    }

    /**
     * Buscar pagaré por ID
     */
    @Transactional(readOnly = true)
    public Pagare buscarPorId(Integer id) {
        return pagareRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id.toString(), "Pagare"));
    }

    /**
     * Buscar pagaré específico por solicitud y número de cuota
     */
    @Transactional(readOnly = true)
    public Pagare buscarPorSolicitudYCuota(Integer idSolicitud, Integer numeroCuota) {
        return pagareRepository.findByIdSolicitudAndNumeroCuota(idSolicitud, numeroCuota)
                .orElseThrow(() -> new NotFoundException(
                    String.format("Solicitud: %d, Cuota: %d", idSolicitud, numeroCuota), "Pagare"));
    }

    /**
     * Listar pagarés por solicitud
     */
    @Transactional(readOnly = true)
    public List<Pagare> listarPorSolicitud(Integer idSolicitud) {
        return pagareRepository.findByIdSolicitud(idSolicitud);
    }

    /**
     * Listar pagarés por solicitud ordenados por número de cuota
     */
    @Transactional(readOnly = true)
    public List<Pagare> listarPorSolicitudOrdenados(Integer idSolicitud) {
        return pagareRepository.findByIdSolicitudOrderByNumeroCuota(idSolicitud);
    }

    /**
     * Actualizar ruta de archivo de pagaré
     */
    public Pagare actualizarRutaArchivo(Integer idPagare, String nuevaRuta) {
        try {
            Pagare pagare = buscarPorId(idPagare);
            
            if (nuevaRuta == null || nuevaRuta.trim().isEmpty()) {
                throw new ValidationException("rutaArchivo", "requerida");
            }
            
            pagare.setRutaArchivo(nuevaRuta);
            return pagareRepository.save(pagare);
            
        } catch (Exception e) {
            throw new BusinessException("Error al actualizar ruta de archivo: " + e.getMessage(), "ACTUALIZAR_RUTA");
        }
    }

    /**
     * Regenerar archivo de pagaré
     */
    public Pagare regenerarArchivo(Integer idPagare) {
        try {
            Pagare pagare = buscarPorId(idPagare);
            
            // Generar nueva ruta de archivo
            String nuevaRuta = generarRutaArchivoPagare(
                pagare.getIdSolicitud(), 
                pagare.getNumeroCuota()
            );
            pagare.setRutaArchivo(nuevaRuta);
            
            return pagareRepository.save(pagare);
            
        } catch (Exception e) {
            throw new BusinessException("Error al regenerar archivo: " + e.getMessage(), "REGENERAR_ARCHIVO");
        }
    }

    /**
     * Regenerar todos los archivos de pagarés de una solicitud
     */
    public List<Pagare> regenerarArchivosPorSolicitud(Integer idSolicitud) {
        try {
            List<Pagare> pagares = listarPorSolicitud(idSolicitud);
            
            for (Pagare pagare : pagares) {
                String nuevaRuta = generarRutaArchivoPagare(
                    pagare.getIdSolicitud(), 
                    pagare.getNumeroCuota()
                );
                pagare.setRutaArchivo(nuevaRuta);
            }
            
            return pagareRepository.saveAll(pagares);
            
        } catch (Exception e) {
            throw new BusinessException("Error al regenerar archivos: " + e.getMessage(), "REGENERAR_ARCHIVOS");
        }
    }

    /**
     * Eliminar pagaré individual
     */
    public void eliminarPagare(Integer idPagare) {
        try {
            Pagare pagare = buscarPorId(idPagare);
            pagareRepository.delete(pagare);
            
        } catch (Exception e) {
            throw new BusinessException("Error al eliminar pagaré: " + e.getMessage(), "ELIMINAR_PAGARE");
        }
    }

    /**
     * Eliminar todos los pagarés de una solicitud
     */
    public void eliminarPagaresPorSolicitud(Integer idSolicitud) {
        try {
            List<Pagare> pagares = listarPorSolicitud(idSolicitud);
            pagareRepository.deleteAll(pagares);
            
        } catch (Exception e) {
            throw new BusinessException("Error al eliminar pagarés: " + e.getMessage(), "ELIMINAR_PAGARES");
        }
    }

    /**
     * Contar pagarés por solicitud
     */
    @Transactional(readOnly = true)
    public long contarPagaresPorSolicitud(Integer idSolicitud) {
        return pagareRepository.countByIdSolicitud(idSolicitud);
    }

    /**
     * Verificar si existen pagarés para una solicitud
     */
    @Transactional(readOnly = true)
    public boolean existenPagaresPorSolicitud(Integer idSolicitud) {
        return pagareRepository.countByIdSolicitud(idSolicitud) > 0;
    }

    /**
     * Verificar si existe pagaré para solicitud y cuota específica
     */
    @Transactional(readOnly = true)
    public boolean existePagarePorSolicitudYCuota(Integer idSolicitud, Integer numeroCuota) {
        return pagareRepository.existsByIdSolicitudAndNumeroCuota(idSolicitud, numeroCuota);
    }

    /**
     * Validar integridad de pagarés por solicitud
     */
    @Transactional(readOnly = true)
    public boolean validarIntegridadPagares(Integer idSolicitud) {
        try {
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            long pagaresGenerados = contarPagaresPorSolicitud(idSolicitud);
            
            return pagaresGenerados == solicitud.getPlazoMeses().longValue();
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtener pagarés faltantes para una solicitud
     */
    @Transactional(readOnly = true)
    public List<Integer> obtenerCuotasFaltantes(Integer idSolicitud) {
        try {
            SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
            List<Pagare> pagaresExistentes = listarPorSolicitud(idSolicitud);
            
            List<Integer> cuotasFaltantes = new ArrayList<>();
            
            for (int cuota = 1; cuota <= solicitud.getPlazoMeses().intValue(); cuota++) {
                final int cuotaFinal = cuota;
                boolean existe = pagaresExistentes.stream()
                    .anyMatch(p -> p.getNumeroCuota() == cuotaFinal);
                
                if (!existe) {
                    cuotasFaltantes.add(cuota);
                }
            }
            
            return cuotasFaltantes;
            
        } catch (Exception e) {
            throw new BusinessException("Error al obtener cuotas faltantes: " + e.getMessage(), "OBTENER_CUOTAS_FALTANTES");
        }
    }

    // Métodos privados auxiliares
    private String generarRutaArchivoPagare(Integer idSolicitud, Integer numeroCuota) {
        // En un caso real, aquí se generaría el archivo PDF del pagaré
        // y se retornaría la ruta real del archivo
        LocalDateTime ahora = LocalDateTime.now();
        return String.format("/pagares/%d/pagare_solicitud_%d_cuota_%d_%d%02d%02d_%02d%02d%02d.pdf",
                ahora.getYear(),
                idSolicitud,
                numeroCuota,
                ahora.getYear(),
                ahora.getMonthValue(),
                ahora.getDayOfMonth(),
                ahora.getHour(),
                ahora.getMinute(),
                ahora.getSecond());
    }
} 
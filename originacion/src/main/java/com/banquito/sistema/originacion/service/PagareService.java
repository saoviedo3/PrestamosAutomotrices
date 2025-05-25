package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.exception.BusinessException;
import com.banquito.sistema.originacion.exception.PagareNotFoundException;
import com.banquito.sistema.originacion.exception.ValidationException;
import com.banquito.sistema.originacion.model.Pagare;
import com.banquito.sistema.originacion.model.SolicitudCredito;
import com.banquito.sistema.originacion.repository.PagareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PagareService {

    private final PagareRepository pagareRepository;
    private final SolicitudCreditoService solicitudCreditoService;

    private static final String SOLICITUD_APROBADA = "APROBADA";
    private static final String SOLICITUD_DESEMBOLSADA = "DESEMBOLSADA";

    public List<Pagare> generarPagaresPorSolicitud(Integer idSolicitud) {
        SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
        validarEstadoSolicitud(solicitud);

        if (pagareRepository.countByIdSolicitud(idSolicitud) > 0) {
            throw new BusinessException("Ya existen pagarés generados para esta solicitud", "GENERAR_PAGARES");
        }

        List<Pagare> pagares = new java.util.ArrayList<>();

        for (int cuota = 1; cuota <= solicitud.getPlazoMeses(); cuota++) {
            Pagare pagare = new Pagare();
            pagare.setIdSolicitud(idSolicitud);
            pagare.setNumeroCuota(cuota);
            pagare.setFechaGenerado(LocalDateTime.now());
            pagare.setRutaArchivo(generarRutaArchivoPagare(idSolicitud, cuota));
            pagares.add(pagare);
        }

        return pagareRepository.saveAll(pagares);
    }

    public Pagare generarPagareIndividual(Integer idSolicitud, Integer numeroCuota) {
        SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
        validarEstadoSolicitud(solicitud);

        if (numeroCuota < 1 || numeroCuota > solicitud.getPlazoMeses()) {
            throw new ValidationException("numeroCuota", "debe estar entre 1 y " + solicitud.getPlazoMeses());
        }

        if (pagareRepository.existsByIdSolicitudAndNumeroCuota(idSolicitud, numeroCuota)) {
            throw new BusinessException("Ya existe un pagaré para esta cuota", "GENERAR_PAGARE");
        }

        Pagare pagare = new Pagare();
        pagare.setIdSolicitud(idSolicitud);
        pagare.setNumeroCuota(numeroCuota);
        pagare.setFechaGenerado(LocalDateTime.now());
        pagare.setRutaArchivo(generarRutaArchivoPagare(idSolicitud, numeroCuota));

        return pagareRepository.save(pagare);
    }

    public Pagare buscarPorId(Integer id) {
        return pagareRepository.findById(id)
                .orElseThrow(() -> new PagareNotFoundException("Pagaré no encontrado con id: " + id));
    }

    public List<Pagare> listarPorSolicitud(Integer idSolicitud) {
        return pagareRepository.findByIdSolicitud(idSolicitud);
    }

    public List<Pagare> listarPorSolicitudOrdenados(Integer idSolicitud) {
        return pagareRepository.findByIdSolicitudOrderByNumeroCuota(idSolicitud);
    }

    public Pagare buscarPorSolicitudYCuota(Integer idSolicitud, Integer numeroCuota) {
        return pagareRepository.findByIdSolicitudAndNumeroCuota(idSolicitud, numeroCuota)
                .orElseThrow(() -> new PagareNotFoundException("Pagaré no encontrado para solicitud " + idSolicitud + " y cuota " + numeroCuota));
    }

    public Pagare regenerarArchivo(Integer id) {
        Pagare pagare = buscarPorId(id);
        String nuevaRuta = generarRutaArchivoPagare(pagare.getIdSolicitud(), pagare.getNumeroCuota());
        pagare.setRutaArchivo(nuevaRuta);
        pagare.setFechaGenerado(LocalDateTime.now());
        return pagareRepository.save(pagare);
    }

    public List<Pagare> regenerarArchivosPorSolicitud(Integer idSolicitud) {
        List<Pagare> pagares = listarPorSolicitud(idSolicitud);
        pagares.forEach(p -> {
            p.setRutaArchivo(generarRutaArchivoPagare(p.getIdSolicitud(), p.getNumeroCuota()));
            p.setFechaGenerado(LocalDateTime.now());
        });
        return pagareRepository.saveAll(pagares);
    }

    public boolean validarIntegridadPagares(Integer idSolicitud) {
        SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
        int plazo = solicitud.getPlazoMeses();

        List<Pagare> pagares = pagareRepository.findByIdSolicitud(idSolicitud);
        if (pagares.size() != plazo) {
            return false;
        }

        // Validar que las cuotas estén completas y sin duplicados
        boolean[] cuotasEncontradas = new boolean[plazo];
        for (Pagare pagare : pagares) {
            int cuotaIndex = pagare.getNumeroCuota() - 1;
            if (cuotaIndex < 0 || cuotaIndex >= plazo || cuotasEncontradas[cuotaIndex]) {
                return false;
            }
            cuotasEncontradas[cuotaIndex] = true;
        }

        return true;
    }

    public List<Integer> obtenerCuotasFaltantes(Integer idSolicitud) {
        SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
        int plazo = solicitud.getPlazoMeses();

        List<Pagare> pagares = pagareRepository.findByIdSolicitud(idSolicitud);
        boolean[] cuotasEncontradas = new boolean[plazo];
        for (Pagare pagare : pagares) {
            int cuotaIndex = pagare.getNumeroCuota() - 1;
            if (cuotaIndex >= 0 && cuotaIndex < plazo) {
                cuotasEncontradas[cuotaIndex] = true;
            }
        }

        List<Integer> cuotasFaltantes = new java.util.ArrayList<>();
        for (int i = 0; i < plazo; i++) {
            if (!cuotasEncontradas[i]) {
                cuotasFaltantes.add(i + 1);
            }
        }
        return cuotasFaltantes;
    }

    public long contarPagaresPorSolicitud(Integer idSolicitud) {
        return pagareRepository.countByIdSolicitud(idSolicitud);
    }

    public boolean existePagarePorSolicitudYCuota(Integer idSolicitud, Integer numeroCuota) {
        return pagareRepository.existsByIdSolicitudAndNumeroCuota(idSolicitud, numeroCuota);
    }

    public void eliminarPagare(Integer id) {
        Pagare pagare = buscarPorId(id);
        pagareRepository.delete(pagare);
    }

    public void eliminarPagaresPorSolicitud(Integer idSolicitud) {
        List<Pagare> pagares = listarPorSolicitud(idSolicitud);
        pagareRepository.deleteAll(pagares);
    }

    private void validarEstadoSolicitud(SolicitudCredito solicitud) {
        if (!SOLICITUD_APROBADA.equals(solicitud.getEstado()) && !SOLICITUD_DESEMBOLSADA.equals(solicitud.getEstado())) {
            throw new BusinessException("Solo se pueden generar pagarés para solicitudes aprobadas o desembolsadas", "ESTADO_SOLICITUD_INVALIDO");
        }
    }

    /**
     * Método simulado para generar la ruta del archivo del pagaré.
     * En una implementación real, puede implicar lógica de generación de documentos, almacenamiento, etc.
     */
    private String generarRutaArchivoPagare(Integer idSolicitud, Integer numeroCuota) {
        // Ejemplo de ruta: "/archivos/pagares/{idSolicitud}_{numeroCuota}.pdf"
        return "/archivos/pagares/" + idSolicitud + "_" + numeroCuota + ".pdf";
    }
}

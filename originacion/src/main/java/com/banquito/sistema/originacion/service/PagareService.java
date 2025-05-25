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
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class PagareService {

    private final PagareRepository pagareRepository;
    private final SolicitudCreditoService solicitudCreditoService;

    private static final String SOLICITUD_APROBADA = "Aprobada";
    private static final String SOLICITUD_DESEMBOLSADA = "Desembolsada";

    public List<Pagare> generarPagaresPorSolicitud(Integer idSolicitud) {
        SolicitudCredito solicitud = solicitudCreditoService.buscarPorId(idSolicitud);
        validarEstadoSolicitud(solicitud);

        if (pagareRepository.countBySolicitudCredito_IdSolicitud(idSolicitud) > 0) {
            throw new BusinessException("Ya existen pagarés generados para esta solicitud", "GENERAR_PAGARES");
        }

        List<Pagare> pagares = new ArrayList<>();
        int plazoMeses = solicitud.getPlazoMeses().intValue();

        for (int cuota = 1; cuota <= plazoMeses; cuota++) {
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

        int plazoMeses = solicitud.getPlazoMeses().intValue();
        if (numeroCuota < 1 || numeroCuota > plazoMeses) {
            throw new ValidationException("numeroCuota", "debe estar entre 1 y " + plazoMeses);
        }

        if (pagareRepository.existsBySolicitudCredito_IdSolicitudAndNumeroCuota(idSolicitud, numeroCuota)) {
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
                .orElseThrow(() -> new NotFoundException(id.toString(), "Pagare"));
    }

    public List<Pagare> listarPorSolicitud(Integer idSolicitud) {
        return pagareRepository.findBySolicitudCredito_IdSolicitud(idSolicitud);
    }

    public List<Pagare> listarPorSolicitudOrdenados(Integer idSolicitud) {
        return pagareRepository.findBySolicitudCredito_IdSolicitudOrderByNumeroCuota(idSolicitud);
    }

    public Pagare buscarPorSolicitudYCuota(Integer idSolicitud, Integer numeroCuota) {
        return pagareRepository.findBySolicitudCredito_IdSolicitudAndNumeroCuota(idSolicitud, numeroCuota)
                .orElseThrow(() -> new NotFoundException("Solicitud: " + idSolicitud + ", Cuota: " + numeroCuota, "Pagare"));
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
        int plazo = solicitud.getPlazoMeses().intValue();

        List<Pagare> pagares = pagareRepository.findBySolicitudCredito_IdSolicitud(idSolicitud);
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
        int plazo = solicitud.getPlazoMeses().intValue();

        List<Pagare> pagares = pagareRepository.findBySolicitudCredito_IdSolicitud(idSolicitud);
        boolean[] cuotasEncontradas = new boolean[plazo];
        for (Pagare pagare : pagares) {
            int cuotaIndex = pagare.getNumeroCuota() - 1;
            if (cuotaIndex >= 0 && cuotaIndex < plazo) {
                cuotasEncontradas[cuotaIndex] = true;
            }
        }

        List<Integer> cuotasFaltantes = new ArrayList<>();
        for (int i = 0; i < plazo; i++) {
            if (!cuotasEncontradas[i]) {
                cuotasFaltantes.add(i + 1);
            }
        }
        return cuotasFaltantes;
    }

    public long contarPagaresPorSolicitud(Integer idSolicitud) {
        return pagareRepository.countBySolicitudCredito_IdSolicitud(idSolicitud);
    }

    public boolean existePagarePorSolicitudYCuota(Integer idSolicitud, Integer numeroCuota) {
        return pagareRepository.existsBySolicitudCredito_IdSolicitudAndNumeroCuota(idSolicitud, numeroCuota);
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

    private String generarRutaArchivoPagare(Integer idSolicitud, Integer numeroCuota) {
        return "/archivos/pagares/" + idSolicitud + "_" + numeroCuota + ".pdf";
    }
}

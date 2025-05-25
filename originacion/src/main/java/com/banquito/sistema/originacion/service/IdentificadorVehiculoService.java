package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.IdentificadorVehiculo;
import com.banquito.sistema.originacion.repository.IdentificadorVehiculoRepository;
import com.banquito.sistema.originacion.exception.CreateEntityException;
import com.banquito.sistema.originacion.exception.IdentificadorVehiculoNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IdentificadorVehiculoService {
    private final IdentificadorVehiculoRepository identificadorRepository;

    public IdentificadorVehiculoService(IdentificadorVehiculoRepository identificadorRepository) {
        this.identificadorRepository = identificadorRepository;
    }

    @Transactional(readOnly = true)
    public List<IdentificadorVehiculo> getAll() {
        return identificadorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public IdentificadorVehiculo getById(Long id) {
        return identificadorRepository.findById(id)
            .orElseThrow(() -> new IdentificadorVehiculoNotFoundException(id));
    }

    @Transactional
    public IdentificadorVehiculo create(IdentificadorVehiculo entity) {
        try {
            return identificadorRepository.save(entity);
        } catch (Exception e) {
            // Lanzamos CreateEntityException con nombre de entidad y detalle del error
            throw new CreateEntityException(
                "IdentificadorVehiculo",
                "Error al crear IdentificadorVehiculo: " + e.getMessage()
            );
        }
    }
}

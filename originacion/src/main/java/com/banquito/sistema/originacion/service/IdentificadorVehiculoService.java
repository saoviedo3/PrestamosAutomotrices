package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.IdentificadorVehiculo;
import com.banquito.sistema.originacion.repository.IdentificadorVehiculoRepository;
import com.banquito.sistema.exception.AlreadyExistsException;
import com.banquito.sistema.exception.CreateEntityException;
import com.banquito.sistema.exception.InvalidDataException;
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
        // 1) VALIDACIONES BÁSICAS
        if (entity.getVin() == null || entity.getVin().length() != 17) {
            throw new InvalidDataException(
                    "IdentificadorVehiculo",
                    "VIN inválido: debe tener exactamente 17 caracteres");
        }
        if (entity.getNumeroMotor() == null || entity.getNumeroMotor().isBlank()) {
            throw new InvalidDataException(
                    "IdentificadorVehiculo",
                    "Número de motor es obligatorio");
        }
        if (entity.getPlaca() == null || entity.getPlaca().isBlank()) {
            throw new InvalidDataException(
                    "IdentificadorVehiculo",
                    "Placa es obligatoria");
        }

        // 2) YA EXISTE?
        if (identificadorRepository.existsByVin(entity.getVin())) {
            throw new AlreadyExistsException(
                    "IdentificadorVehiculo",
                    "Ya existe un Identificador con VIN: " + entity.getVin());
        }
        if (identificadorRepository.existsByNumeroMotor(entity.getNumeroMotor())) {
            throw new AlreadyExistsException(
                    "IdentificadorVehiculo",
                    "Ya existe un Identificador con Número de Motor: " + entity.getNumeroMotor());
        }
        if (identificadorRepository.existsByPlaca(entity.getPlaca())) {
            throw new AlreadyExistsException(
                    "IdentificadorVehiculo",
                    "Ya existe un Identificador con Placa: " + entity.getPlaca());
        }

        // 3) GRABAR O LANZAR ERROR AL CREAR
        try {
            return identificadorRepository.save(entity);
        } catch (Exception e) {
            throw new CreateEntityException(
                    "IdentificadorVehiculo",
                    "Error al crear IdentificadorVehiculo: " + e.getMessage());
        }
    }
}

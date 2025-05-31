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

    public List<IdentificadorVehiculo> getAll() {
        return identificadorRepository.findAll();
    }

    public IdentificadorVehiculo getById(Long id) {
        return identificadorRepository.findById(id)
            .orElseThrow(() -> new IdentificadorVehiculoNotFoundException(id));
    }

    /**
     * Crear un nuevo identificador vehicular.
     * Validaciones:
     *   - VIN debe tener exactamente 17 caracteres.
     *   - Número de motor obligatoria, no vacía.
     *   - Placa obligatoria, no vacía.
     *   - No debe existir otro registro con mismo VIN, motor o placa.
     * */

    @Transactional
    public IdentificadorVehiculo create(IdentificadorVehiculo entity) {
        // 1) Validaciones básicas de formato
        if (entity.getVin() == null || entity.getVin().length() != 17) {
            throw new InvalidDataException(
                "IdentificadorVehiculo",
                "VIN inválido: debe tener exactamente 17 caracteres"
            );
        }
        if (entity.getNumeroMotor() == null || entity.getNumeroMotor().isBlank()) {
            throw new InvalidDataException(
                "IdentificadorVehiculo",
                "Número de motor es obligatorio"
            );
        }
        if (entity.getPlaca() == null || entity.getPlaca().isBlank()) {
            throw new InvalidDataException(
                "IdentificadorVehiculo",
                "Placa es obligatoria"
            );
        }

        // 2) Verificar si ya existe un VIN, motor o placa iguales
        if (identificadorRepository.existsByVin(entity.getVin())) {
            throw new AlreadyExistsException(
                "IdentificadorVehiculo",
                "Ya existe un Identificador con VIN: " + entity.getVin()
            );
        }
        if (identificadorRepository.existsByNumeroMotor(entity.getNumeroMotor())) {
            throw new AlreadyExistsException(
                "IdentificadorVehiculo",
                "Ya existe un Identificador con Número de Motor: " + entity.getNumeroMotor()
            );
        }
        if (identificadorRepository.existsByPlaca(entity.getPlaca())) {
            throw new AlreadyExistsException(
                "IdentificadorVehiculo",
                "Ya existe un Identificador con Placa: " + entity.getPlaca()
            );
        }

        // 3) Intentar guardar en la base de datos
        try {
            return identificadorRepository.save(entity);
        } catch (Exception e) {
            throw new CreateEntityException(
                "IdentificadorVehiculo",
                "Error al crear IdentificadorVehiculo: " + e.getMessage()
            );
        }
    }

}

package com.banquito.sistema.originacion.service;

import com.banquito.sistema.originacion.model.Vehiculo;
import com.banquito.sistema.originacion.repository.VehiculoRepository;
import com.banquito.sistema.originacion.exception.CreateEntityException;
import com.banquito.sistema.originacion.exception.VehiculoNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehiculo getVehiculoById(Long id) {
        return vehiculoRepository.findById(id)
            .orElseThrow(() -> new VehiculoNotFoundException(id));
    }

    @Transactional
    public Vehiculo createVehiculo(Vehiculo vehiculo) {
        try {
            return vehiculoRepository.save(vehiculo);
        } catch (Exception e) {
            throw new CreateEntityException(
                "Vehiculo",
                "Error al crear Vehiculo: " + e.getMessage()
            );
        }
    }
}

package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    boolean existsByIdentificadorVehiculoId(Long idIdentificadorVehiculo);
}

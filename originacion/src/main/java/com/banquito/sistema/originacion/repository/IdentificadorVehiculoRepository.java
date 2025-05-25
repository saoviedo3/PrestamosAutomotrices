package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.IdentificadorVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificadorVehiculoRepository extends JpaRepository<IdentificadorVehiculo, Long> {
    boolean existsByVin(String vin);
    boolean existsByNumeroMotor(String numeroMotor);
    boolean existsByPlaca(String placa);
}

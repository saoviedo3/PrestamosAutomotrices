package com.banquito.sistema.originacion.repository;

import com.banquito.sistema.originacion.model.Vehiculo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    boolean existsByIdentificadorVehiculoId(Long idIdentificadorVehiculo);
    public List<Vehiculo> findByEstado(String estado);
    public List<Vehiculo> findByIdConcesionario(Long idConcesionario);
    public Long countByIdConcesionario(Long idConcesionario);

}

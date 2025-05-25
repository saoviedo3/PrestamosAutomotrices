package com.banquito.sistema.originacion.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdVehiculo", nullable = false)
    private Integer idVehiculo;

    @Column(name = "Marca", length = 50, nullable = false)
    private String marca;

    @Column(name = "Modelo", length = 50, nullable = false)
    private String modelo;

    @Column(name = "Anio", nullable = false)
    private Integer anio;

    @Column(name = "Precio", precision = 12, scale = 2, nullable = false)
    private BigDecimal precio;

    @Version
    @Column(name = "Version", precision = 9, scale = 0, nullable = false)
    private Long version;

    public Vehiculo() {
    }

    public Vehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    // Getters y Setters
    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
} 
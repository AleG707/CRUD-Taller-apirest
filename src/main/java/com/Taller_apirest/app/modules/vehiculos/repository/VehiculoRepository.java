package com.Taller_apirest.app.modules.vehiculos.repository;

import com.Taller_apirest.app.modules.vehiculos.model.Vehiculo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends MongoRepository<Vehiculo, String> {
    Optional<Vehiculo> findByPlaca(String placa);
    List<Vehiculo> findByClienteId(String clienteId);
}

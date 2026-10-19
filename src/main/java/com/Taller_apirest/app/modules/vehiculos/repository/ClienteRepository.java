package com.Taller_apirest.app.modules.vehiculos.repository;

import com.Taller_apirest.app.modules.vehiculos.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    Optional<Cliente> findByIdentificacion(String identificacion);
    Optional<Cliente> findByEmail(String email);
}

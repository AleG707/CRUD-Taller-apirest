package com.Taller_apirest.app.modules.proveedores.repository;

import com.Taller_apirest.app.modules.proveedores.model.Proveedor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends MongoRepository<Proveedor, String> {
    Optional<Proveedor> findByNit(String nit);
    List<Proveedor> findByActivoTrue();
}

package com.Taller_apirest.app.modules.admin.repository;

import com.Taller_apirest.app.modules.admin.model.ServicioCatalogo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioCatalogoRepository extends MongoRepository<ServicioCatalogo, String> {
    Optional<ServicioCatalogo> findByCodigo(String codigo);
    List<ServicioCatalogo> findByActivoTrue();
    List<ServicioCatalogo> findByCategoria(String categoria);
}

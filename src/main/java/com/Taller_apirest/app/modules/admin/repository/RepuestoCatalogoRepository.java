package com.Taller_apirest.app.modules.admin.repository;

import com.Taller_apirest.app.modules.admin.model.RepuestoCatalogo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepuestoCatalogoRepository extends MongoRepository<RepuestoCatalogo, String> {
    Optional<RepuestoCatalogo> findByCodigo(String codigo);
    List<RepuestoCatalogo> findByCategoria(String categoria);
    List<RepuestoCatalogo> findByStockActualLessThanEqual(Integer stockMinimo);
}

package com.Taller_apirest.app.modules.ordenes.repository;

import com.Taller_apirest.app.modules.ordenes.model.Cotizacion;
import com.Taller_apirest.app.modules.ordenes.model.EstadoCotizacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CotizacionRepository extends MongoRepository<Cotizacion, String> {
    Optional<Cotizacion> findByCodigoCotizacion(String codigoCotizacion);
    List<Cotizacion> findByClienteId(String clienteId);
    List<Cotizacion> findByPlaca(String placa);
    List<Cotizacion> findByEstado(EstadoCotizacion estado);
}

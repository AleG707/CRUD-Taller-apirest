package com.Taller_apirest.app.modules.certificaciones.repository;

import com.Taller_apirest.app.modules.certificaciones.model.CertificacionAuditoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificacionAuditoriaRepository extends MongoRepository<CertificacionAuditoria, String> {
    Optional<CertificacionAuditoria> findByCodigoCertificado(String codigoCertificado);
    Optional<CertificacionAuditoria> findByOrdenTrabajoId(String ordenTrabajoId);
    List<CertificacionAuditoria> findByVehiculoPlaca(String vehiculoPlaca);
    List<CertificacionAuditoria> findByAuditorId(String auditorId);
}

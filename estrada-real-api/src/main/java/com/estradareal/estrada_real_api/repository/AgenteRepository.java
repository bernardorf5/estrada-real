package com.estradareal.estrada_real_api.repository;

import com.estradareal.estrada_real_api.model.Agente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
    Optional<Agente> findByLogin(String login);
    boolean existsByCnpj(String cnpj);
}

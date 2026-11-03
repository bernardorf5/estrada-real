package com.estradareal.estrada_real_api.repository;

import com.estradareal.estrada_real_api.model.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByLogin(String login);
    boolean existsByCpf(String cpf);
}

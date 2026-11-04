package com.estradareal.estrada_real_api.repository;

import com.estradareal.estrada_real_api.model.Automovel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {
    Optional<Automovel> findByPlaca(String placa);

    void deleteByProprietarioId(Long proprietarioId);
}

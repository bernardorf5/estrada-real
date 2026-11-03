package com.estradareal.estrada_real_api.service;

import static com.estradareal.estrada_real_api.dto.Dtos.*;

import com.estradareal.estrada_real_api.repository.AgenteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AgenteService {

    private final AgenteRepository agentes;

    public AgenteService(AgenteRepository agentes) {
        this.agentes = agentes;
    }

    public List<AgenteResponse> listar() {
        return agentes.findAll().stream().map(AgenteResponse::de).toList();
    }
}

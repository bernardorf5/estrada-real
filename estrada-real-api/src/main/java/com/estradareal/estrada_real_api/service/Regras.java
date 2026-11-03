package com.estradareal.estrada_real_api.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

final class Regras {

    private Regras() { }

    static ResponseStatusException invalido(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }

    static ResponseStatusException naoEncontrado(String msg) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
    }

    static void maximoTresRendimentos(List<?> lista) {
        if (lista != null && lista.size() > 3) {
            throw invalido("Um cliente pode ter no máximo 3 rendimentos");
        }
    }
}

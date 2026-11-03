package com.estradareal.estrada_real_api.service;

import static com.estradareal.estrada_real_api.dto.Dtos.*;

import com.estradareal.estrada_real_api.model.Cliente;
import com.estradareal.estrada_real_api.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clientes;
    private final PedidoRepository pedidos;
    private final AutomovelRepository automoveis;

    public ClienteService(ClienteRepository clientes, PedidoRepository pedidos, AutomovelRepository automoveis) {
        this.clientes = clientes;
        this.pedidos = pedidos;
        this.automoveis = automoveis;
    }

    private Cliente buscar(String login) {
        return clientes.findByLogin(login).orElseThrow(() -> Regras.naoEncontrado("Cliente não encontrado"));
    }

    @Transactional(readOnly = true)
    public ClienteResponse me(String login) {
        return ClienteResponse.de(buscar(login));
    }

    public ClienteResponse atualizar(String login, ClienteUpdateRequest r) {
        Regras.maximoTresRendimentos(r.rendimentos());
        Cliente c = buscar(login);
        c.setNome(r.nome());
        c.setRg(r.rg());
        c.setEndereco(r.endereco());
        c.setProfissao(r.profissao());
        List<RendimentoDto> rends = r.rendimentos() == null ? List.of() : r.rendimentos();
        c.substituirRendimentos(rends.stream().map(RendimentoDto::paraEntidade).toList());
        return ClienteResponse.de(clientes.save(c));
    }

    public void excluir(String login) {
        Cliente c = buscar(login);
        pedidos.deleteByClienteId(c.getId());
        automoveis.deleteByProprietarioId(c.getId());
        clientes.delete(c);
    }
}

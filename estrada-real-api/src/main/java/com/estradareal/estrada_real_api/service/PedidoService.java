package com.estradareal.estrada_real_api.service;

import static com.estradareal.estrada_real_api.dto.Dtos.*;

import com.estradareal.estrada_real_api.model.*;
import com.estradareal.estrada_real_api.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final AgenteRepository agentes;
    private final AutomovelRepository automoveis;
    private final ContratoCreditoRepository contratos;

    public PedidoService(PedidoRepository pedidos, ClienteRepository clientes, AgenteRepository agentes,
                         AutomovelRepository automoveis, ContratoCreditoRepository contratos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.agentes = agentes;
        this.automoveis = automoveis;
        this.contratos = contratos;
    }

    // ---------- lado do cliente ----------

    public PedidoResponse criar(String login, PedidoRequest req) {
        Cliente cliente = clientes.findByLogin(login)
                .orElseThrow(() -> Regras.naoEncontrado("Cliente não encontrado"));
        Pedido p = new Pedido();
        p.setCliente(cliente);
        p.setAutomovel(new Automovel());
        preencher(p, req);
        return PedidoResponse.de(pedidos.save(p));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> meus(String login) {
        Cliente cliente = clientes.findByLogin(login)
                .orElseThrow(() -> Regras.naoEncontrado("Cliente não encontrado"));
        return pedidos.findByClienteIdOrderByCriadoEmDesc(cliente.getId())
                .stream().map(PedidoResponse::de).toList();
    }

    public PedidoResponse atualizar(String login, Long id, PedidoRequest req) {
        Pedido p = pedidoDoCliente(login, id);
        if (p.getStatus() != StatusPedido.EM_ANALISE) {
            throw Regras.invalido("Só é possível modificar pedidos em análise");
        }
        preencher(p, req);
        return PedidoResponse.de(pedidos.save(p));
    }

    public PedidoResponse cancelar(String login, Long id) {
        Pedido p = pedidoDoCliente(login, id);
        if (p.getStatus() == StatusPedido.CONTRATO_EXECUTADO || p.getStatus() == StatusPedido.CANCELADO) {
            throw Regras.invalido("Este pedido não pode mais ser cancelado");
        }
        p.setStatus(StatusPedido.CANCELADO);
        return PedidoResponse.de(pedidos.save(p));
    }

    // ---------- lado do agente ----------

    @Transactional(readOnly = true)
    public List<PedidoResponse> todos() {
        return pedidos.findAllByOrderByCriadoEmDesc().stream().map(PedidoResponse::de).toList();
    }

    public PedidoResponse atualizarStatus(String loginAgente, Long id, StatusPedido novo) {
        Agente agente = agentes.findByLogin(loginAgente)
                .orElseThrow(() -> Regras.naoEncontrado("Agente não encontrado"));
        Pedido p = pedidos.findById(id).orElseThrow(() -> Regras.naoEncontrado("Pedido não encontrado"));

        boolean avaliacao = p.getStatus() == StatusPedido.EM_ANALISE
                && (novo == StatusPedido.APROVADO || novo == StatusPedido.REJEITADO);
        boolean execucao = p.getStatus() == StatusPedido.APROVADO && novo == StatusPedido.CONTRATO_EXECUTADO;
        if (!avaliacao && !execucao) {
            throw Regras.invalido("Transição inválida: " + p.getStatus() + " para " + novo);
        }
        p.setStatus(novo);
        p.setAvaliador(agente);
        return PedidoResponse.de(pedidos.save(p));
    }

    // ---------- auxiliares ----------

    private Pedido pedidoDoCliente(String login, Long id) {
        Pedido p = pedidos.findById(id).orElseThrow(() -> Regras.naoEncontrado("Pedido não encontrado"));
        if (!p.getCliente().getLogin().equals(login)) {
            throw Regras.naoEncontrado("Pedido não encontrado");
        }
        return p;
    }

    private void preencher(Pedido p, PedidoRequest req) {
        Automovel auto = p.getAutomovel();
        AutomovelDto d = req.automovel();

        automoveis.findByPlaca(d.placa())
                .filter(outro -> !outro.getId().equals(auto.getId()))
                .ifPresent(outro -> { throw Regras.invalido("Já existe um automóvel com essa placa"); });

        Usuario dono;
        if (req.proprietarioId() == null) {
            dono = p.getCliente();
        } else {
            dono = agentes.findById(req.proprietarioId())
                    .orElseThrow(() -> Regras.invalido("Proprietário inválido"));
        }

        auto.setProprietario(dono);
        auto.setMatricula(d.matricula());
        auto.setAno(d.ano());
        auto.setMarca(d.marca());
        auto.setModelo(d.modelo());
        auto.setPlaca(d.placa());

        if (req.bancoId() != null) {
            Agente banco = agentes.findById(req.bancoId())
                    .orElseThrow(() -> Regras.invalido("Banco inválido"));
            if (banco.getPapel() != Papel.BANCO) {
                throw Regras.invalido("O agente escolhido não é um banco");
            }
            if (req.valorCredito() == null || req.valorCredito().signum() <= 0) {
                throw Regras.invalido("Informe o valor do crédito");
            }
            ContratoCredito cc = p.getContratoCredito() != null ? p.getContratoCredito() : new ContratoCredito();
            cc.setBanco(banco);
            cc.setValor(req.valorCredito());
            p.setContratoCredito(cc);
        } else if (p.getContratoCredito() != null) {
            ContratoCredito antigo = p.getContratoCredito();
            p.setContratoCredito(null);
            contratos.delete(antigo);
        }
    }
}

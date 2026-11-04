import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../auth.jsx';

const autoVazio = { marca: '', modelo: '', ano: '', placa: '', matricula: '' };

export default function PedidoForm() {
  const { id } = useParams();
  const { sessao, chamar } = useAuth();
  const navigate = useNavigate();
  const [auto, setAuto] = useState(autoVazio);
  const [proprietarioId, setProprietarioId] = useState('');
  const [bancoId, setBancoId] = useState('');
  const [valorCredito, setValorCredito] = useState('');
  const [agentes, setAgentes] = useState([]);
  const [erro, setErro] = useState('');

  useEffect(() => {
    chamar('/api/agentes').then(setAgentes).catch((e) => setErro(e.message));
    if (id) {
      chamar('/api/pedidos/meus')
        .then((lista) => {
          const p = lista.find((x) => String(x.id) === id);
          if (!p) return setErro('Pedido não encontrado');
          const a = p.automovel;
          setAuto({ marca: a.marca, modelo: a.modelo, ano: String(a.ano), placa: a.placa, matricula: a.matricula });
          setProprietarioId(p.proprietarioId === sessao.usuario.id ? '' : String(p.proprietarioId));
          setBancoId(p.bancoId ? String(p.bancoId) : '');
          setValorCredito(p.valorCredito ? String(p.valorCredito) : '');
        })
        .catch((e) => setErro(e.message));
    }
  }, [id]);

  const campo = (nome) => ({
    value: auto[nome],
    onChange: (e) => setAuto({ ...auto, [nome]: e.target.value }),
    required: true,
  });

  async function enviar(e) {
    e.preventDefault();
    setErro('');
    const body = {
      automovel: { ...auto, ano: Number(auto.ano) },
      proprietarioId: proprietarioId ? Number(proprietarioId) : null,
      bancoId: bancoId ? Number(bancoId) : null,
      valorCredito: bancoId ? Number(valorCredito) : null,
    };
    try {
      await chamar(id ? `/api/pedidos/${id}` : '/api/pedidos', {
        method: id ? 'PUT' : 'POST',
        body,
      });
      navigate('/pedidos');
    } catch (err) {
      setErro(err.message);
    }
  }

  const bancos = agentes.filter((a) => a.papel === 'BANCO');

  return (
    <form className="card auth larga" onSubmit={enviar}>
      <h1>{id ? 'Editar pedido' : 'Novo pedido de aluguel'}</h1>
      <p className="sub">Informe os dados do automóvel e do contrato.</p>

      <div className="row">
        <div><label>Marca</label><input {...campo('marca')} /></div>
        <div><label>Modelo</label><input {...campo('modelo')} /></div>
      </div>
      <div className="row">
        <div><label>Ano</label><input type="number" min="1900" max="2100" {...campo('ano')} /></div>
        <div><label>Placa</label><input {...campo('placa')} /></div>
        <div><label>Matrícula</label><input {...campo('matricula')} /></div>
      </div>

      <label>Propriedade do automóvel</label>
      <select value={proprietarioId} onChange={(e) => setProprietarioId(e.target.value)}>
        <option value="">Cliente (eu mesmo)</option>
        {agentes.map((a) => (
          <option key={a.id} value={a.id}>{a.razaoSocial} ({a.papel === 'BANCO' ? 'Banco' : 'Empresa'})</option>
        ))}
      </select>

      <div className="row">
        <div>
          <label>Contrato de crédito</label>
          <select value={bancoId} onChange={(e) => setBancoId(e.target.value)}>
            <option value="">Sem crédito</option>
            {bancos.map((b) => (
              <option key={b.id} value={b.id}>{b.razaoSocial}</option>
            ))}
          </select>
        </div>
        {bancoId && (
          <div>
            <label>Valor do crédito (R$)</label>
            <input
              type="number"
              min="0.01"
              step="0.01"
              value={valorCredito}
              onChange={(e) => setValorCredito(e.target.value)}
              required
            />
          </div>
        )}
      </div>

      <div className="err">{erro}</div>
      <button className="btn full">{id ? 'Salvar alterações' : 'Enviar pedido'}</button>
    </form>
  );
}

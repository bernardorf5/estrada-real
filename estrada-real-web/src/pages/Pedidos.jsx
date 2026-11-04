import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import Badge from '../components/Badge.jsx';
import { brl } from '../util.js';

export default function Pedidos() {
  const { chamar } = useAuth();
  const [lista, setLista] = useState([]);
  const [erro, setErro] = useState('');

  const carregar = () =>
    chamar('/api/pedidos/meus').then(setLista).catch((e) => setErro(e.message));

  useEffect(() => {
    carregar();
  }, []);

  async function cancelar(id) {
    if (!confirm('Cancelar este pedido?')) return;
    try {
      await chamar(`/api/pedidos/${id}/cancelar`, { method: 'PATCH' });
      carregar();
    } catch (e) {
      setErro(e.message);
    }
  }

  return (
    <>
      <div className="top">
        <div>
          <h1>Meus pedidos</h1>
          <p className="sub">Acompanhe o status de cada aluguel.</p>
        </div>
        <Link className="btn" to="/pedidos/novo">+ Novo pedido</Link>
      </div>
      <div className="err">{erro}</div>
      {lista.length === 0 && !erro && <p className="sub">Você ainda não tem pedidos.</p>}
      <div className="grid">
        {lista.map((p) => (
          <div className="card ped" key={p.id}>
            <span className="t">{p.automovel.marca} {p.automovel.modelo} {p.automovel.ano}</span>
            <span className="m">Placa {p.automovel.placa} · Propriedade: {p.proprietario}</span>
            <span className="m">{p.banco ? `${p.banco}, ${brl(p.valorCredito)}` : 'Sem crédito'}</span>
            <Badge status={p.status} />
            <div className="a">
              {p.status === 'EM_ANALISE' && (
                <Link className="btn ghost sm" to={`/pedidos/${p.id}/editar`}>Editar</Link>
              )}
              {p.status !== 'CANCELADO' && p.status !== 'CONTRATO_EXECUTADO' && (
                <button className="btn ghost sm" onClick={() => cancelar(p.id)}>Cancelar</button>
              )}
            </div>
          </div>
        ))}
      </div>
    </>
  );
}

import { useEffect, useState } from 'react';
import { useAuth } from '../auth.jsx';
import Badge from '../components/Badge.jsx';
import { brl } from '../util.js';

export default function Painel() {
  const { chamar } = useAuth();
  const [lista, setLista] = useState([]);
  const [erro, setErro] = useState('');

  const carregar = () =>
    chamar('/api/agentes/pedidos').then(setLista).catch((e) => setErro(e.message));

  useEffect(() => {
    carregar();
  }, []);

  async function mudar(id, status) {
    setErro('');
    try {
      await chamar(`/api/agentes/pedidos/${id}/status`, { method: 'PATCH', body: { status } });
      carregar();
    } catch (e) {
      setErro(e.message);
    }
  }

  const total = (s) => lista.filter((p) => p.status === s).length;
  const renda = (p) => p.rendimentosCliente.reduce((soma, r) => soma + Number(r.valor), 0);

  return (
    <>
      <h1>Painel do agente</h1>
      <p className="sub">Avalie financeiramente os pedidos e execute os contratos.</p>
      <div className="stats">
        <div className="card stat"><b>{total('EM_ANALISE')}</b><span>Em análise</span></div>
        <div className="card stat"><b>{total('APROVADO')}</b><span>Aprovados</span></div>
        <div className="card stat"><b>{total('CONTRATO_EXECUTADO')}</b><span>Contratos</span></div>
      </div>
      <div className="err">{erro}</div>
      <div className="card tw">
        <table>
          <thead>
            <tr><th>Cliente</th><th>Automóvel</th><th>Renda total</th><th>Crédito</th><th>Status</th><th></th></tr>
          </thead>
          <tbody>
            {lista.map((p) => (
              <tr key={p.id}>
                <td>{p.cliente}</td>
                <td>{p.automovel.marca} {p.automovel.modelo} {p.automovel.ano}</td>
                <td>{brl(renda(p))}</td>
                <td>{p.banco ? `${p.banco}, ${brl(p.valorCredito)}` : 'Sem crédito'}</td>
                <td><Badge status={p.status} /></td>
                <td>
                  {p.status === 'EM_ANALISE' && (
                    <>
                      <button className="btn sm" onClick={() => mudar(p.id, 'APROVADO')}>Aprovar</button>{' '}
                      <button className="btn ghost sm" onClick={() => mudar(p.id, 'REJEITADO')}>Rejeitar</button>
                    </>
                  )}
                  {p.status === 'APROVADO' && (
                    <button className="btn sm" onClick={() => mudar(p.id, 'CONTRATO_EXECUTADO')}>Executar contrato</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {lista.length === 0 && !erro && <p className="sub">Nenhum pedido ainda.</p>}
      </div>
    </>
  );
}

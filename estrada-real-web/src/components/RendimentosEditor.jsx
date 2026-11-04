export default function RendimentosEditor({ itens, onChange }) {
  const alterar = (i, campo, valor) =>
    onChange(itens.map((r, j) => (j === i ? { ...r, [campo]: valor } : r)));

  return (
    <div>
      {itens.map((r, i) => (
        <div className="row" key={i}>
          <div>
            <label>Entidade empregadora</label>
            <input
              value={r.entidadeEmpregadora}
              onChange={(e) => alterar(i, 'entidadeEmpregadora', e.target.value)}
              required
            />
          </div>
          <div>
            <label>Rendimento (R$)</label>
            <input
              type="number"
              min="0"
              step="0.01"
              value={r.valor}
              onChange={(e) => alterar(i, 'valor', e.target.value)}
              required
            />
          </div>
          <div className="fim">
            <button
              type="button"
              className="btn ghost sm"
              onClick={() => onChange(itens.filter((_, j) => j !== i))}
            >
              Remover
            </button>
          </div>
        </div>
      ))}
      {itens.length < 3 && (
        <button
          type="button"
          className="btn ghost sm"
          style={{ marginTop: 12 }}
          onClick={() => onChange([...itens, { entidadeEmpregadora: '', valor: '' }])}
        >
          + Adicionar rendimento
        </button>
      )}
    </div>
  );
}

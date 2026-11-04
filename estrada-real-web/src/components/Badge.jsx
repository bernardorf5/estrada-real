const ROTULOS = {
  EM_ANALISE: ['Em análise', 'analise'],
  APROVADO: ['Aprovado', 'aprovado'],
  REJEITADO: ['Rejeitado', 'rejeitado'],
  CONTRATO_EXECUTADO: ['Contrato executado', 'contrato'],
  CANCELADO: ['Cancelado', 'cancelado'],
};

export default function Badge({ status }) {
  const [texto, classe] = ROTULOS[status] ?? [status, 'analise'];
  return <span className={`badge b-${classe}`}>{texto}</span>;
}

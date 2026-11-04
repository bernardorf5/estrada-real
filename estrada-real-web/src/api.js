const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080';

function basic(login, senha) {
  const bytes = new TextEncoder().encode(`${login}:${senha}`);
  let bin = '';
  bytes.forEach((b) => (bin += String.fromCharCode(b)));
  return 'Basic ' + btoa(bin);
}

export async function api(path, { method = 'GET', body, auth } = {}) {
  const headers = {};
  if (body) headers['Content-Type'] = 'application/json';
  if (auth) headers.Authorization = basic(auth.login, auth.senha);

  let res;
  try {
    res = await fetch(BASE + path, {
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined,
    });
  } catch {
    throw new Error('Não foi possível conectar ao servidor');
  }

  if (res.status === 204) return null;
  if (res.status === 401) throw new Error('Usuário ou senha inválidos');

  const data = await res.json().catch(() => null);
  if (!res.ok) {
    const detalhe = data?.errors?.[0];
    if (detalhe) throw new Error(`${detalhe.field ?? ''} ${detalhe.defaultMessage ?? ''}`.trim());
    throw new Error(data?.message || `Erro ${res.status}`);
  }
  return data;
}

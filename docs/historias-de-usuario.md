# Histórias do Usuário: Estrada Real

## Cliente

**HU01. Cadastro**
Como cliente, quero me cadastrar informando RG, CPF, nome, endereço, profissão e até 3 rendimentos, para poder usar o sistema.
- O login e o CPF não podem se repetir.
- Não é possível informar mais de 3 rendimentos.

**HU02. Login**
Como usuário cadastrado, quero entrar com usuário e senha, para acessar as funcionalidades do sistema.
- Sem login, nenhuma tela além do login e do cadastro é acessível.
- Usuário ou senha incorretos exibem uma mensagem de erro.

**HU03. Gerenciar perfil**
Como cliente, quero consultar, editar e excluir meus dados, para mantê-los atualizados.
- O CPF não pode ser alterado.
- Ao excluir a conta, os pedidos do cliente também são removidos.

**HU04. Criar pedido**
Como cliente, quero criar um pedido de aluguel informando os dados do automóvel (marca, modelo, ano, placa e matrícula), para solicitar o aluguel.
- O pedido nasce com o status "Em análise".
- Não pode haver dois automóveis com a mesma placa.

**HU05. Definir proprietário**
Como cliente, quero indicar se o automóvel é de minha propriedade, de uma empresa ou de um banco, porque isso depende do tipo de contrato.

**HU06. Solicitar crédito**
Como cliente, quero associar ao pedido um contrato de crédito de um banco, informando o valor, para financiar o aluguel.
- O crédito é opcional.
- Só é possível escolher agentes do tipo banco.

**HU07. Consultar pedidos**
Como cliente, quero ver a lista dos meus pedidos e o status de cada um, para acompanhar o andamento.

**HU08. Modificar pedido**
Como cliente, quero editar um pedido que ainda está em análise, para corrigir informações.
- Pedidos já avaliados não podem ser editados.

**HU09. Cancelar pedido**
Como cliente, quero cancelar um pedido, para desistir do aluguel.
- Não é possível cancelar um pedido com contrato já executado.

## Agente (empresa ou banco)

**HU10. Cadastro de agente**
Como empresa ou banco, quero me cadastrar com CNPJ e razão social, para participar do sistema.

**HU11. Visualizar pedidos**
Como agente, quero ver todos os pedidos com os rendimentos do cliente, para fazer a análise financeira.

**HU12. Avaliar pedido**
Como agente, quero aprovar ou rejeitar um pedido em análise, para dar o parecer financeiro.

**HU13. Executar contrato**
Como agente, quero executar o contrato de um pedido aprovado, para concretizar o aluguel.
- Só pedidos aprovados podem ter o contrato executado.

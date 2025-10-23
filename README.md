## API Agendamento de Aulas/ Serviços

API REST de Back-end para gerenciamento de agendamento de aulas e serviços.


Estrutura do Projeto
O projeto segue o padrão MVC (Model-View-Controller) e Arquitetura em Camadas:

model: Entidades JPA (tabelas do banco).

repository: Interfaces de acesso a dados (CRUD).

service: Lógica de Negócio (a ser desenvolvida).

controller: Endpoints REST (a ser desenvolvida).



##  Tecnologias Principais

| Categoria | Tecnologia | Versão |
| :--- | :--- | :---: |
| Framework | Spring Boot | 3.x |
| Linguagem | Java | 21 |
| Persistência | Spring Data JPA / Hibernate | |
| Banco de Dados | MySQL | |
| Ferramenta | Maven | |


## Lógica de Negócios Essencial (Mínimo Viável) ##

1. Criação de um Novo Agendamento (POST /agendamentos)
Ao receber uma solicitação para criar um novo agendamento, o serviço deve garantir as seguintes regras:

Disponibilidade do Instrutor (Regra de Conflito): É obrigatório verificar se o instrutor selecionado para a aula está livre. O sistema não deve permitir a criação de um agendamento se o instrutor já tiver outro compromisso marcado exatamente no mesmo horário (data e hora de início). Se houver conflito, o agendamento é rejeitado.

Validação de Data e Hora (Regra Temporal): A data e hora escolhidas para o agendamento devem ser sempre no futuro. Não é permitido criar agendamentos com datas ou horários que já passaram.

Validação de Entidades (Regra de Referência): O agendamento só é válido se referenciar entidades que realmente existem. O serviço deve garantir que o Instrutor e o Serviço (aulas) informados no pedido de agendamento estejam cadastrados e ativos no banco de dados.

Se todas essas três regras forem satisfeitas, o agendamento pode ser persistido no banco de dados.

2. Cancelamento de um Agendamento (DELETE /agendamentos/{id})
Ao processar o cancelamento de um agendamento existente, o serviço deve impor uma regra de prazo:

Prazo Mínimo de Cancelamento (Regra de Antecedência): O cancelamento só será permitido se houver um tempo mínimo de antecedência em relação ao horário de início da aula. Por exemplo, se a regra for de 2 horas, o cancelamento será negado se a hora atual estiver a menos de duas horas da hora de início agendada, para evitar cancelamentos de última hora. Se o prazo for respeitado, o agendamento pode ser removido ou marcado como cancelado.

## Lógica de Negócios Avançada (Para Escalabilidade)
Após implementar o mínimo viável, as seguintes regras devem ser adicionadas à camada de serviços para aumentar a inteligência do sistema:

1. Gerenciamento de Disponibilidade do Instrutor
Blocos de Trabalho (Regra de Horário Fixo): O sistema deve respeitar os horários oficiais de trabalho do instrutor. Um agendamento só pode ser aceito se a hora de início e a hora de fim estiverem dentro do intervalo de trabalho definido para aquele profissional (Ex: Se o instrutor trabalha das 08:00h às 18:00h, agendamentos às 19:00h devem ser rejeitados). Essa lógica deve ser exposta em um endpoint próprio para consulta de disponibilidade.

2. Atualização de Agendamentos
Integridade na Edição (Regra de Revalidação): Ao permitir que um agendamento seja alterado (mudança de data, hora ou instrutor), todas as regras de criação (Disponibilidade, Data no Futuro, Validação de Entidades) devem ser revalidadas. Uma atualização só é permitida se o novo horário não gerar conflito com outras aulas ou violar as regras temporais.

3. Cálculo de Duração e Hora de Fim
Duração Dinâmica do Serviço (Regra de Duração): A hora de término de um agendamento não deve ser inserida pelo usuário; ela deve ser calculada pelo sistema. Ao criar um agendamento, o serviço deve consultar a duração padrão do Serviço escolhido (ex: 60 minutos) e, a partir da hora de início fornecida, calcular e registrar automaticamente a hora de fim do compromisso.

4. Recursos de Busca e Filtragem
Filtros para Consulta (Regra de Acesso a Dados): O sistema deve oferecer maneiras flexíveis de consultar os agendamentos. A busca na API deve permitir filtros por parâmetros-chave, como buscar todos os agendamentos em uma determinada data, por um Instrutor específico, ou por status (como "Confirmado", "Cancelado" ou "Concluído").

A implementação dessas regras garante que a API não apenas armazene dados, mas também atue como um sistema inteligente que aplica restrições do mundo real.



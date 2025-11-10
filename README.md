# Tema 🧠 
- Hábitos e Organização Pessoal

# Colaboradores 🧑‍💻
- Caio Lopes
- Gabriel Rabello

# Descrição 📗
Uma api de gestão pessoal, voltada para controle de tarefas e gastos, visando construir uma rotina saudável e uma boa gestão.

# Técnologias utilizadas 🧬

![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) <br>
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) <br>
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

# Entidades 🏦


### Tasks  
Representa as tarefas criadas pelo usuário.  
- `id`  
- `task_status_id`  
- `user_id`  
- `description`  
- `title`  

### Task-steps  
Passos necessários para a conclusão da tarefa.  
- `id`  
- `description`  

### Task-status  
Define o estado atual da tarefa.  
- `id`  
- `status`  

### Users  
Dados de login e perfil de cada usuário.  
- `id`  
- `email`  
- `name`  
- `password`  

## Rotas da API  
- `POST /api/auth/register` — Registra o usuário  
- `POST /api/auth/login` — Faz login do usuário  
- `POST /api/task` — Cria uma tarefa  
- `DELETE /api/task/{id}` — Deleta uma tarefa  
- `POST /api/taskstep/{taskId}` — Cria um passo para a tarefa  
- `DELETE /api/taskstep/{id}` — Deleta um passo de tarefa  
- `GET /api/task/{id}` — Retorna a tarefa com o ID informado  


[![Captura-de-tela-2025-10-27-200258.png](https://i.postimg.cc/brcN11zT/Captura-de-tela-2025-10-27-200258.png)](https://postimg.cc/ygjHHg3g)

# Implementação de Exportação 📤

- Em breve

- # Como executar localmente

## 1 Passo: clonar o repositório

Git clone https://github.com/CaioLopesxd/todoThings.git

## 2 Passo: Criar o arquivo .env

DB_URL: URL do banco <Br>
DB_USER: Usuário do banco <Br>
DB_PASS: Senha do banco <Br>

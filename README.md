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

### Tasks: Tarefas criadas pelo o usuário.
-id <br>
-task_status_id <br>
-user_id <br>
-description <br>
-title <br>
### Task-steps: Passos necessários para a conclusão da tarefa.
-id <br>
-description <br>
### Task-status: Mostrar o estado atual da tarefa.
-id <br>
-status <br>
### Users: Responsável pelo login e informações de cada  usuário.
-id <br>
-email <br>
-name <br>
-password <br>

# ROTAS 🛤️

api/auth/register - Registra o usuário <br>
api/auth/login - Logar o usuário <br>
POST api/task - Criar uma tarefa <br>
POST api/{id}/taskstep - Criar os passos da tarefa <br>
GET api/task/{id} - Retornar a tarefa criada. <br>

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

# CRUD Manager – Gestão de RH (Recrutamento e Vagas)

Projeto da disciplina **Tecnologias de Desenvolvimento para Internet III** (Servlets e JSP),
construído a partir do projeto base *CRUD Manager*.

## Como executar

**Requisitos:** JDK 11+, Apache Tomcat **10.1** (Jakarta EE), MySQL 8.

1. **Banco de dados** – execute o `script-sql.sql` (cria o banco `crud_manager`, as tabelas e dados de exemplo).
   > Atenção: o script começa com `DROP DATABASE IF EXISTS crud_manager;` (como no projeto base).
   > Para manter dados já existentes, rode apenas o bloco *"ATIVIDADE 3"* no final do arquivo.
2. **Credenciais do banco** – confira usuário/senha em `src/main/java/model/dao/MySQLConnectionFactory.java`
   (padrão do projeto: `root` / `root1234`).
3. **Implantação** – importe como projeto web no Eclipse e execute no Tomcat 10.1.
   O *context root* precisa ser **`/crud-manager`** (os controllers comparam a URL com `/crud-manager/...`).
4. Acesse `http://localhost:8080/crud-manager` e entre com:

   | E-mail | Senha |
   |--------|-------|
   | `admin@mail.com` | `admin123` |

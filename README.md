# CRUD Manager – Gestão de RH (Recrutamento e Vagas)

Projeto da disciplina **Tecnologias de Desenvolvimento para Internet II** (Servlets e JSP),
construído a partir do projeto base *CRUD Manager*.

Foram adicionados ao projeto base:

| Item | Descrição |
|------|-----------|
| **CRUD de Vagas** (`/jobs`) | título (`text`), salário (`number`), data de encerramento (`date`), vaga 100% remota (`checkbox`) e empresa contratante (`select`, FK → `companies`) |
| **CRUD de Candidatos** (`/candidates`) | nome completo (`text`), pretensão salarial (`number`), data de nascimento (`date`), nível de experiência (`radio`: Júnior/Pleno/Sênior) e vaga desejada (`select`, FK → `jobs`) |
| **Login / Logout** | tela de login, `AuthenticationFilter` protegendo todo o sistema, sessão, senha com hash PBKDF2 |
| **Paginação** | em **todos** os cadastros (Usuários, Posts, Empresas, Vagas e Candidatos), 5 registros por página |

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

## Estrutura do que foi criado/alterado

```
script-sql.sql                         tabelas accounts, jobs, candidates + dados de exemplo
src/main/java/
  model/   Account, ExperienceLevel (enum), Job, Candidate
  model/dao/  AccountDAO, JobDAO, CandidateDAO (+ MySQL*DAO) e registro no DAOFactory
  controller/ JobsController, CandidatesController, LoginController, AuthenticationFilter
  util/    Pagination (cálculo da paginação), PasswordUtil (hash de senha)
src/main/webapp/
  jobs.jsp, form-job.jsp, candidates.jsp, form-candidate.jsp   telas dos novos CRUDs
  login.jsp                                                     tela de login
  pagination.jsp                                                barra de paginação reutilizável
  nav-menu.jsp                                                  novos itens no cabeçalho + nome do usuário + Sair
```

### Alterações em arquivos do projeto base
* `DBHandler`: métodos para `BigDecimal` e `boolean`; `close()` público e fechamento automático da
  conexão ao fim do `ResultSet` (o original deixava conexões abertas a cada consulta).
* `UserDAO`, `PostDAO`, `CompanyDAO` (+ implementações): `listPage(page, pageSize)` e `countAll()`;
  `MySQLCompanyDAO.delete` passou a tratar violação de chave estrangeira (empresas com vagas).
* `UsersController`, `PostsController`, `CompaniesController`: listagens paginadas.
* `index.jsp`, `users.jsp`, `companies.jsp`: barra de paginação real (`pagination.jsp`) no lugar da fixa.
* `ControllerUtil`: `createPagination(...)`; `formatDate` tolerante a `null` e sem datas inexistentes.
* `CharacterEncodingFilter`: removido `setContentType("charset=UTF-8")`, que não é um tipo MIME válido e
  fazia o navegador ignorar o CSS em versões recentes do Tomcat.

## Problemas comuns

* **`NoClassDefFoundError: javax/servlet/jsp/tagext/TagLibraryValidator`** – a pasta `WEB-INF/lib` traz duas
  versões do JSTL: `jakarta.servlet.jsp.jstl-*` (correta para Tomcat 10) e `taglibs-standard-*` (versão
  `javax`, do Tomcat 9). Se o Tomcat escolher a errada, remova os três `taglibs-standard-*.jar`.
* **Todas as páginas dão 404** – o *context root* não é `/crud-manager`.
* **`The server time zone value ... is unrecognized`** – acrescente `?serverTimezone=America/Sao_Paulo`
  à `DATABASE_URL` em `MySQLConnectionFactory`.
* **Login recusado** – rode novamente o `script-sql.sql` (ele recria a conta `admin@mail.com`).

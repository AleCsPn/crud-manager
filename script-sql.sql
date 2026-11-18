DROP DATABASE IF EXISTS crud_manager;

CREATE DATABASE IF NOT EXISTS crud_manager;

USE crud_manager;

CREATE TABLE IF NOT EXISTS users(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    sexo ENUM('M', 'F'),
    email VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS posts(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    post_date DATE NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(user_id) 
    REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS companies (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(128) NOT NULL,
	`role` VARCHAR(128) NOT NULL,
	`start` DATE NOT NULL,
	`end` DATE,
	user_id INT NOT NULL,
	FOREIGN KEY(user_id)
	REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS companies (
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(128) NOT NULL,
`role` VARCHAR(128) NOT NULL,
`start` DATE NOT NULL,
`end` DATE,
user_id INT NOT NULL,
FOREIGN KEY(user_id)
REFERENCES users(id)
);

INSERT INTO users VALUES
(DEFAULT, "Emerson Carvalho", "M", "emerson@mail.com"),
(DEFAULT, "Luiza Carvalho", "F", "lu@mail.com"),
(DEFAULT, "Elenice Carvalho", "F", "le@mail.com"),
(DEFAULT, "Noé Carvalho", "M", "noe@mail.com"),
(DEFAULT, "Rosânia Carvalho", "F", "ro@mail.com");

INSERT INTO posts VALUES
(DEFAULT, "Olá do Emerson", CURDATE(), 1),
(DEFAULT, "Olá da Luiza", CURDATE(), 2),
(DEFAULT, "Olá da Denise", CURDATE(), 3),
(DEFAULT, "Olá do Noé", CURDATE(), 4),
(DEFAULT, "Olá da Rosânia", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 1", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 2", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 3", CURDATE(), 5);

CREATE TABLE IF NOT EXISTS accounts (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS jobs (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    closing_date DATE NOT NULL,
    remote BOOLEAN NOT NULL DEFAULT FALSE,
    company_id INT NOT NULL,
    FOREIGN KEY (company_id)
    REFERENCES companies(id)
);

CREATE TABLE IF NOT EXISTS candidates (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    salary_expectation DECIMAL(10,2) NOT NULL,
    birth_date DATE NOT NULL,
    experience_level ENUM('JUNIOR', 'PLENO', 'SENIOR') NOT NULL,
    job_id INT NOT NULL,
    FOREIGN KEY (job_id)
    REFERENCES jobs(id)
);

INSERT INTO accounts VALUES
(DEFAULT, "Administrador", "admin@mail.com", "pbkdf2_sha256$120000$Uxm6fRSZAKHu1EAMQWn3GA==$vc/jcd4VPLq6wBCfEHUo1YE6d1T8dkFx10Ot4ntlQX0=");

INSERT INTO users VALUES
(DEFAULT, "Marina Souza", "F", "marina@mail.com"),
(DEFAULT, "Carlos Pereira", "M", "carlos@mail.com"),
(DEFAULT, "Fernanda Lima", "F", "fernanda@mail.com");

INSERT INTO companies VALUES
(DEFAULT, "TechNova Soluções", "Analista de Sistemas", "2019-03-01", NULL, 1),
(DEFAULT, "Banco Horizonte", "Gerente de Contas", "2018-06-15", "2022-01-31", 2),
(DEFAULT, "Agro Minas Ltda", "Engenheiro Agrônomo", "2020-02-10", NULL, 3),
(DEFAULT, "Café Serra Azul", "Supervisor de Qualidade", "2017-09-01", "2021-12-20", 4),
(DEFAULT, "Logística Rápida", "Coordenador de Frota", "2021-05-03", NULL, 5),
(DEFAULT, "Escola Futuro", "Professora", "2016-02-15", NULL, 6),
(DEFAULT, "Hospital Vida", "Enfermeiro", "2019-08-12", NULL, 7);

INSERT INTO jobs VALUES
(DEFAULT, "Desenvolvedor Java Pleno", 8500.00, "2026-12-15", TRUE, 1),
(DEFAULT, "Analista de Dados Júnior", 4200.00, "2026-12-10", TRUE, 1),
(DEFAULT, "Gerente de Relacionamento", 9800.00, "2026-12-20", FALSE, 2),
(DEFAULT, "Analista de Crédito", 5600.00, "2026-11-30", FALSE, 2),
(DEFAULT, "Engenheiro Agrônomo", 7300.00, "2027-01-10", FALSE, 3),
(DEFAULT, "Técnico em Agropecuária", 3500.00, "2026-12-05", FALSE, 3),
(DEFAULT, "Supervisor de Torra", 4800.00, "2026-12-18", FALSE, 4),
(DEFAULT, "Auxiliar Administrativo", 2400.00, "2026-11-25", FALSE, 5),
(DEFAULT, "Analista de Logística Sênior", 7800.00, "2027-01-05", TRUE, 5),
(DEFAULT, "Professor de Matemática", 4100.00, "2026-12-12", FALSE, 6),
(DEFAULT, "Enfermeiro Plantonista", 5200.00, "2026-12-22", FALSE, 7),
(DEFAULT, "Recrutador de RH", 4500.00, "2027-01-15", TRUE, 1);

INSERT INTO candidates VALUES
(DEFAULT, "Ana Beatriz Ferreira", 9000.00, "1995-04-12", "PLENO", 1),
(DEFAULT, "Bruno Henrique Costa", 4000.00, "2001-09-30", "JUNIOR", 2),
(DEFAULT, "Camila Rodrigues Alves", 10500.00, "1988-01-25", "SENIOR", 3),
(DEFAULT, "Daniel Martins Oliveira", 5800.00, "1993-07-08", "PLENO", 4),
(DEFAULT, "Eduarda Nogueira Santos", 7000.00, "1997-11-19", "PLENO", 5),
(DEFAULT, "Felipe Augusto Ribeiro", 3300.00, "2002-03-02", "JUNIOR", 6),
(DEFAULT, "Gabriela Castro Moreira", 5000.00, "1990-12-14", "SENIOR", 7),
(DEFAULT, "Henrique Silva Barbosa", 2600.00, "2003-06-21", "JUNIOR", 8),
(DEFAULT, "Isabela Duarte Pinto", 8200.00, "1985-10-05", "SENIOR", 9),
(DEFAULT, "João Pedro Teixeira", 4300.00, "1996-02-17", "PLENO", 10),
(DEFAULT, "Larissa Mendes Cardoso", 5500.00, "1999-08-29", "JUNIOR", 11),
(DEFAULT, "Marcos Vinícius Araújo", 4700.00, "1992-05-11", "PLENO", 12);

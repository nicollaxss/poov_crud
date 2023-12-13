-- Active: 1701555184342@@127.0.0.1@5432@poov@public

-- Table: public.vacina

-- DROP TABLE IF EXISTS public.vacina;

CREATE TABLE IF NOT EXISTS public.vacina
(
    codigo bigint NOT NULL DEFAULT nextval('vacina_codigo_seq'::regclass),
    nome text COLLATE pg_catalog."default",
    descricao text COLLATE pg_catalog."default",
    situacao text COLLATE pg_catalog."default" DEFAULT 'ATIVO'::text,
    CONSTRAINT vacina_pkey PRIMARY KEY (codigo)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.vacina
    OWNER to postgres;

SELECT * FROM vacina;

INSERT INTO vacina (codigo, nome, descricao, situacao)
VALUES
    (1, 'covid', 'vacina_covid', 'ATIVO'),
    (2, 'febre_amarela', 'febre_amarela', 'ATIVO');

INSERT INTO vacina (codigo, nome, descricao, situacao)
VALUES(3, 'covid2', 'vacina_covid2', 'ATIVO');

UPDATE vacina 
set situacao = 'ATIVO'
WHERE codigo = 4;

select * FROM vacina;

-- Table: public.pessoa

-- DROP TABLE IF EXISTS public.pessoa;

CREATE TABLE IF NOT EXISTS public.pessoa
(
    codigo bigint NOT NULL,
    nome text COLLATE pg_catalog."default",
    cpf text COLLATE pg_catalog."default",
    dataNascimento DATE,
    situacao text COLLATE pg_catalog."default" DEFAULT 'ATIVO'::text,
    CONSTRAINT pessoa_pkey PRIMARY KEY (codigo)
)

DROP Table pessoa;

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.pessoa
    OWNER to postgres;

INSERT INTO pessoa (codigo, nome, cpf, "dataNascimento", situacao) 
VALUES (1, 'nicollas', '12345678912', '11/02/1971', 'ATIVO');

INSERT INTO pessoa (codigo, nome, cpf, datanascimento, situacao) 
VALUES (2, 'outro', '12345678912', '11/05/1979', 'ATIVO');

INSERT INTO pessoa (codigo, nome, cpf, datanascimento, situacao) 
VALUES  (3, 'outro2', '23456789012', '01/07/1990', 'ATIVO'),
        (4, 'outro3', '21632154612', '02/08/2003', 'ATIVO'),
        (5, 'outro4', '09128738782', '05/02/2019', 'ATIVO'),
        (6, 'outro5', '78126387126', '09/03/2014', 'ATIVO'),
        (7, 'outro6', '12983891642', '10/01/2000', 'ATIVO');


SELECT * FROM pessoa;

SELECT * FROM vacina;

SELECT * FROM pessoa
WHERE datanascimento BETWEEN '1970-01-01' AND '1980-12-31';

-- criando a tabela de aplicação
CREATE TABLE IF NOT EXISTS public.aplicacao
(
    codigo bigserial NOT NULL,
    data date,
    pessoa_codigo bigint,
    vacina_codigo bigint,
    situacao text COLLATE pg_catalog."default" DEFAULT 'ATIVO'::text,
    CONSTRAINT aplicacao_pkey PRIMARY KEY (codigo),
    CONSTRAINT fk_pessoa FOREIGN KEY (pessoa_codigo) REFERENCES public.pessoa (codigo),
    CONSTRAINT fk_vacina FOREIGN KEY (vacina_codigo) REFERENCES public.vacina (codigo)
);

DROP Table aplicacao;

SELECT * FROM aplicacao;

INSERT into aplicacao(codigo, "data", pessoa_codigo, vacina_codigo)
VALUES (1, CURRENT_DATE, 1, 2);

INSERT into aplicacao(codigo, "data", pessoa_codigo, vacina_codigo)
VALUES (2, CURRENT_DATE, 1, 1);



SELECT * FROM aplicacao a INNER JOIN vacina v on a.vacina_codigo = v.codigo;

DELETE from aplicacao
WHERE codigo = 1 and codigo = 2;

SELECT * FROM vacina;

DELETE from vacina WHERE codigo = 25;
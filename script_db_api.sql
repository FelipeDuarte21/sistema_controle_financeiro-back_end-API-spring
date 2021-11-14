-- Database: db_controle_financeiro

-- DROP DATABASE db_controle_financeiro;

CREATE DATABASE db_controle_financeiro
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
CREATE TABLE usuario(
	id serial NOT NULL PRIMARY KEY,
	nome varchar(50) NOT NULL,
	email varchar(80) not null unique,
	senha text not null
);

CREATE TABLE tipo_usuario(
	usuario_id bigint NOT NULL,
	tipo int
);

create table tipo_lancamento(
	id serial not null primary key,
	valor int not null,
	nome varchar(30) not null
);

create table categoria(
	id serial not null primary key,
	nome varchar(60) not null,
	descricao text,
	data_cadastro date not null,
	id_usuario bigint not null,
	foreign key(id_usuario) references usuario(id)
);

create table balanco(
	id serial not null primary key,
	mes int not null,
	ano int not null,
	saldo_anterior double precision not null,
	saldo_atual double precision not null,
	fechado boolean default 'false',
	id_categoria bigint not null,
	foreign key(id_categoria) references categoria(id)
);

create table lancamento(
	id serial not null primary key,
	nome varchar(80) not null,
	descricao text,
	valor double precision not null,
	data_cadastro date not null,
	sugestao boolean default 'false',
	id_tipo bigint not null,
	id_balanco bigint not null,
	foreign key(id_tipo) references tipo_lancamento(id),
	foreign key(id_balanco) references balanco(id)
);

create table parcelado(
	id serial not null primary key,
	nome varchar(80) not null,
	descricao text,
	valor_total double precision not null,
	data_cadastro date not null,
	pago boolean default 'false'
);

create table parcela(
	id serial not null primary key,
	numero int not null,
	valor double precision not null,
	data_vencimento date not null,
	pago boolean default 'false',
	id_parcelado bigint not null,
	foreign key(id_parcelado) references parcelado(id)
);


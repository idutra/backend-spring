create table personagem
(
	id varchar(255) not null primary key,
	data_hora_criacao datetime not null,
	data_hora_ult_atualizacao datetime not null,
	house_id varchar(255),
	name varchar(255) constraint personagem_nome_uk unique,
	patronus varchar(255),
	role varchar(255),
	school varchar(255)
)
go


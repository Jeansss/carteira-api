CREATE TABLE perfis_usuarios (
  "usuario_id" bigint NOT NULL,
  "perfil_id" bigint NOT NULL,
  
  primary key("usuario_id", "perfil_id"),
  
  foreign key(usuario_id) references usuarios(id),
  foreign key(perfil_id) references perfis(id)
);

insert into perfis values(1, 'ROLE_ADMIN');
insert into perfis values(2, 'ROLE_COMUM');
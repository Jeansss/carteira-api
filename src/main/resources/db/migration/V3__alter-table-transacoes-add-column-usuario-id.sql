ALTER TABLE transacoes add column usuario_id bigint not null;
ALTER TABLE transacoes add foreign key(usuario_id) references usuarios(id);
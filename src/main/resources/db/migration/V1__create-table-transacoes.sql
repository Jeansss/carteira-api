CREATE TABLE "transacoes" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "ticker" varchar NOT NULL,
  "preco" decimal NOT NULL,
  "quantidade" int NOT NULL,
  "tipo" varchar NOT NULL,
  "data" date NOT NULL
);
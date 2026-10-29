# Concessionária — API Spring Boot

API REST com três entidades relacionadas:

```
Plataforma 1 ── N Modelo 1 ── N Peca
```

- Uma **Plataforma** tem vários **Modelos**.
- Um **Modelo** pertence a uma Plataforma e tem várias **Peças**.
- Uma **Peça** pertence a um Modelo.

## Rodando

```
mvn spring-boot:run
```

API em `http://localhost:8080`. Console do H2 em `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:mem:concessionaria`, usuário `sa`, sem senha).

## Endpoints

| Método | Rota | O que faz |
|---|---|---|
| GET | `/plataforma` | Lista plataformas (com seus modelos) |
| GET | `/plataforma/{id}` | Busca uma plataforma |
| GET | `/plataforma/{id}/modelos` | Modelos da plataforma |
| POST | `/plataforma` | Cria |
| PUT | `/plataforma/{id}` | Atualiza |
| DELETE | `/plataforma/{id}` | Remove (409 se ainda tiver modelos) |
| GET | `/modelo` | Lista modelos (com plataforma e peças) |
| GET | `/modelo/{id}` | Busca um modelo |
| GET | `/modelo/{id}/pecas` | Peças do modelo |
| POST | `/modelo` | Cria |
| PUT | `/modelo/{id}` | Atualiza |
| DELETE | `/modelo/{id}` | Remove (409 se ainda tiver peças) |
| GET | `/peca` | Lista peças (com o modelo) |
| GET | `/peca/{id}` | Busca uma peça |
| POST | `/peca` | Cria |
| PUT | `/peca/{id}` | Atualiza |
| DELETE | `/peca/{id}` | Remove |

Ids que não existem devolvem **404**. Ao criar um modelo ou uma peça apontando
para uma plataforma/modelo que não existe, a resposta é **400**.

## Exemplos (na ordem)

```
POST /plataforma
{ "nome": "MQB" }

POST /modelo
{ "nome": "Golf", "carroceria": "Hatch", "cambio": "Automático",
  "motor": "1.4 TSI", "ano": "2020", "plataforma": { "id": 1 } }

POST /peca
{ "nome": "Pastilha de freio", "codigo": "PF-123", "fabricante": "Bosch",
  "preco": 189.90, "modelo": { "id": 1 } }
```

Para vincular basta mandar o `id` do pai; o controller busca o registro completo no banco.

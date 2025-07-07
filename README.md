# 🗓️ EventAPI - Cadastro e Gerenciamento de Eventos com Spring Boot + AWS

API REST para cadastro, listagem e filtro de eventos com cupons de desconto. A aplicação foi construída com **Java 21**, **Spring Boot 3**, **PostgreSQL**, **AWS S3** e segue o padrão de **arquitetura hexagonal (Ports & Adapters)**.

## ✅ Funcionalidades

- Criar eventos com upload de imagem
- Listar todos os eventos ou eventos filtrados por data, cidade e estado
- Buscar eventos por título
- Visualizar detalhes de um evento
- Cadastrar cupons de desconto para eventos

## 🚀 Endpoints

### Eventos (`/api/event`)
- `POST /api/event` (multipart): Cadastrar novo evento
- `GET /api/event`: Listar eventos
- `GET /api/event/{id}`: Detalhes de um evento
- `GET /api/event/search?title=`: Buscar por título
- `GET /api/event/filter?city=&uf=&startDate=&endDate=`: Filtro por cidade, estado e período

### Cupons (`/api/coupon`)
- `POST /api/coupon/event/{eventId}`: Adicionar cupom a um evento

## 🧱 Arquitetura Hexagonal

- `domain`: Entidades de negócio puras (Event, Coupon, Address)
- `application`: Casos de uso e regras de negócio
- `adapters`:
  - `inbound.controller`: Controllers da API REST
  - `outbound`: Comunicação externa (repositories, AWS S3)
- `infrastructure.configuration`: Beans, configurações CORS e AWS
- `utils`: Mapeadores e utilitários de exceções

## ☁️ Integrações AWS

- Armazenamento de imagens no **S3**
- Configuração via `AWSConfig`

## 🧪 Tecnologias

- Java 21
- Spring Boot 3
- PostgreSQL (AWS RDS)
- AWS S3
- Docker
- Arquitetura Hexagonal
- Maven

## 📦 Rodando Localmente

```bash
# Clone o projeto
git clone https://github.com/caio20538/events_api.git

# Configure application.yml ou .env com as variáveis de conexão

# Build e execute com Docker
docker-compose up --build
📁 Estrutura de Pastas
scss
Copiar
Editar
com.events.api
├── adapters
│   ├── inbound.controller
│   └── outbound (entities, repositories, storage)
├── application (services, usecases)
├── domain (event, coupon, address)
├── infrastructure.configuration (aws, cors, exceptions)
└── utils (mapper, exception util)

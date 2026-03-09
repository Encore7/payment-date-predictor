# Payment Date Predictor

Refactored full-stack project with Dockerized React frontend, Spring Boot backend, and MySQL.

## Project Structure

- `frontend/`: React 18 + MUI v5 app
- `backend/`: Spring Boot 3 REST API + Flyway + CSV auto-seed
- `legacy/`: archived original snapshot (`legacy-frontend-src`, `legacy-backend-src`)
- `docker-compose.yml`: one-command local stack

## Quick Start (Docker)

1. Copy env file:

```bash
cp .env.example .env
```

2. Build and start:

```bash
docker compose up --build
```

3. Open apps:

- Frontend: `http://localhost:3000`
- Backend health: `http://localhost:8080/api/health`

## API

- `GET /api/invoices?page=1&size=50&search=`
- `POST /api/invoices`
- `PUT /api/invoices/{invoiceId}`
- `DELETE /api/invoices`
- `POST /api/invoices/predict`
- `GET /api/health`

## Notes

- On first startup, backend auto-loads `backend/src/main/resources/data/1805277.csv` into MySQL.
- With the persistent Docker volume, restart will not re-import duplicates.

## Local (Without Docker)

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm start
```

Set `REACT_APP_API_BASE_URL` if backend URL differs from `http://localhost:8080`.

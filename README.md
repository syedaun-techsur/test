# Minimal Full-Stack Vite + React + Spring Boot Project (2024 Best Practices)

## Project Structure

```
frontend/   # Vite + React + TypeScript
backend/    # Spring Boot (Maven, Java 17+)
```

## Prerequisites
- Node.js & npm
- Java 17+
- Maven

## Running the Backend

```
cd backend
./mvnw spring-boot:run
```

Test the backend endpoint:

```
curl http://localhost:8080/api/hello
```
Expected response:
```
{"message":"Hello from backend!"}
```

## Running the Frontend

```
cd frontend
npm install
npm run dev
```

> **Note:** There is an intentional error in `frontend/package.json`: the `start` script is misspelled as `strat`. Running `npm start` will fail. To fix, change `"strat"` to `"start"` in `package.json`.

## Integration
- The React frontend fetches from the backend at `/api/hello` and displays the message.
- CORS is enabled on the backend for `http://localhost:5173` (Vite default).
- Uses [React Query](https://tanstack.com/query/latest) for data fetching and caching.
- Uses [Axios](https://axios-http.com/) for HTTP requests.
- Feature-based structure is recommended for scaling.

## Troubleshooting
- If you see an error when running `npm start`, check the scripts section in `frontend/package.json`.
- If the frontend cannot fetch from the backend, ensure the backend is running on port 8080.

---

This project is designed to test pipeline detection, build, run, and error correction capabilities using the latest best practices (2024/2025). 
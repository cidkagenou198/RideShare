# RideShare Backend API

This is a mini Ride Sharing backend built with Spring Boot, MongoDB, and JWT Authentication.

## Features
- User Registration & Login (JWT)
- Ride Request (Passenger)
- Ride Acceptance (Driver)
- Ride Completion
- Input Validation
- Global Exception Handling

## Endpoints

### Authentication
- **POST** `/api/auth/register` - Register a new user (`ROLE_USER` or `ROLE_DRIVER`)
  ```json
  { "username": "...", "password": "...", "role": "ROLE_USER" }
  ```
- **POST** `/api/auth/login` - Login and get JWT token
  ```json
  { "username": "...", "password": "..." }
  ```

### Rides (Passenger)
- **POST** `/api/v1/rides` - Request a ride
  ```json
  { "pickupLocation": "...", "dropLocation": "..." }
  ```
- **GET** `/api/v1/user/rides` - View my ride history
- **POST** `/api/v1/rides/{id}/complete` - Complete a ride (after acceptance)

### Driver
- **GET** `/api/v1/driver/rides/requests` - View pending ride requests
- **POST** `/api/v1/driver/rides/{id}/accept` - Accept a ride

## running the application
1. Ensure MongoDB is running.
39: 2. Run `./mvnw spring-boot:run`
40:
41: ## How to Test (Using Command Prompt)
42: Open `cmd.exe` and use these commands:
43:
44: **1. Register User**
45: ```cmd
46: curl -X POST http://localhost:8081/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"john\",\"password\":\"1234\",\"role\":\"ROLE_USER\"}"
47: ```
48:
49: **2. Login**
50: ```cmd
51: curl -X POST http://localhost:8081/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"john\",\"password\":\"1234\"}"
52: ```
53: *Copy the token from the response!*
54:
55: **3. Create Ride**
56: ```cmd
57: curl -X POST http://localhost:8081/api/v1/rides -H "Authorization: Bearer <PASTE_TOKEN>" -H "Content-Type: application/json" -d "{\"pickupLocation\":\"Mall\",\"dropLocation\":\"Home\"}"
58: ```

## Project Structure
- `org.example.rideshare.config` - Security & JWT configs
- `org.example.rideshare.controller` - API Endpoints
- `org.example.rideshare.dto` - Data Transfer Objects
- `org.example.rideshare.exception` - Global Exception Handling
- `org.example.rideshare.model` - Database Entities
- `org.example.rideshare.repository` - MongoDB Repositories
- `org.example.rideshare.service` - Business Logic

# 🚗 Ride-Sharing Backend System

A Mini-Uber backend implemented using **Spring Boot**, **MongoDB**, **JWT Authentication**, and **Mongo Aggregations**.

## 🔰 Features
- **User Authentication**: JWT-based secure login/registration.
- **Roles**: Support for `ROLE_USER` (Passenger) and `ROLE_DRIVER`.
- **Ride Management**: Create, Accept, and Complete rides.
- **Advanced Search**: Regex-based search, filtering by distance/date, and sorting.
- **Analytics**: Real-time dashboards using Mongo Aggregation Pipelines (Earnings, Rides per Day, etc.).

---

## 🛠️ Tech Stack
- **Language**: Java 17
- **Framework**: Spring Boot
- **Database**: MongoDB
- **Security**: Spring Security + JWT
- **Build Tool**: Maven

---

## 🏃 How to Run

### Prerequisites
- Java 17+ installed.
- MongoDB running on `localhost:27017`.

### Steps
1. **Clean the project**:
   ```powershell
   .\mvnw clean
   ```
2. **Run the application**:
   ```powershell
   .\mvnw spring-boot:run
   ```
   *The server runs on port **8081**.*

---

## 📝 API Cheat Sheet

### Authentication
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/register` | POST | Register new user/driver |
| `/api/auth/login` | POST | Login and get JWT Token |

### Core Ride Operations
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/rides` | POST | Create a ride request |
| `/api/rides/accept/{id}` | POST | Driver accepts a ride |
| `/api/rides/complete/{id}` | POST | Driver completes a ride |

### Testing via PowerShell
**Register & Login:**
```powershell
# Register
Invoke-RestMethod -Uri "http://localhost:8081/api/auth/register" -Method Post -Headers @{"Content-Type"="application/json"} -Body '{"username": "sunny", "password": "pass123", "role": "ROLE_USER"}'

# Login
$token = (Invoke-RestMethod -Uri "http://localhost:8081/api/auth/login" -Method Post -Headers @{"Content-Type"="application/json"} -Body '{"username": "sunny", "password": "pass123"}').token
Write-Host "Token: $token"
```

---

## 📌 API Details & Analytics

### 🔍 Search & Filter APIs (`/api/v1/rides`)

| # | Endpoint | Method | Purpose | Concept |
|---|----------|--------|---------|---------|
| 1 | `/search?text=...` | GET | Search pickup OR drop by keyword | OR Query + Regex |
| 2 | `/filter-distance?min=..&max=..` | GET | Filter by distance range | Comparison (gte/lte) |
| 3 | `/filter-date-range?start=..&end=..` | GET | Rides between two dates | Date Comparison + AND |
| 4 | `/sort?order=asc\|desc` | GET | Sort by fare | Sorting |
| 5 | `/user/{userId}` | GET | All rides for a user | Equality Filter |
| 6 | `/user/{userId}/status/{status}` | GET | Filter user rides by status | AND Criteria |
| 7 | `/filter-status?status=..&search=..` | GET | Status filter + keyword search | AND + OR Combo |
| 8 | `/advanced-search` | GET | Keyword + Status + Sort + Pagination | Advanced Query |
| 9 | `/date/{date}` | GET | Rides on specific date | LocalDate Equality |

### 📊 Analytics APIs (`/api/v1/analytics`)

| # | Endpoint | Purpose | Concept |
|---|----------|---------|---------|
| 10 | `/rides-per-day` | Rides count grouped by date | Group + Sort |
| 11 | `/driver/{driverId}/summary` | Driver stats (Args, Total Fare) | Group + Project |
| 12 | `/user/{userId}/spending` | User total spending | Match + Group |
| 13 | `/status-summary` | Count rides grouped by status | Grouping |

### 🚕 Driver API
| # | Endpoint | Description |
|---|----------|-------------|
| 14 | `/driver/{driverId}/active-rides` | Rides accepted by driver |

---

## 📁 Project Structure
The project is refactored to `com.saicharan`:

```
src/main/java/com/saicharan/demo
├── config          # Security & JWT Configuration
├── controller      # REST Controllers (Auth, Ride, Analytics)
├── dto             # Data Transfer Objects
├── exception       # Global Exception Handling
├── model           # MongoDB Documents (User, Ride)
├── repository      # MongoRepositories
├── service         # Business Logic & Aggregations
└── util            # Helper classes (JwtUtil)
```

---

## 🔐 Security Concepts Used
- **Stateless Session**: No server-side sessions; fully JWT based.
- **Password Hashing**: BCrypt for secure password storage.
- **Role-Based Access**:
    - `ROLE_USER`: Can request rides.
    - `ROLE_DRIVER`: Can accept/complete rides.
# EverAfter - Wedding Planning & Vendor Booking System

Group project for **SE1020 (Object Oriented Programming)** - Group G25.  
Built by **Vidura, Daham, Chanuka, and Lahiru**.

EverAfter is a web application for managing wedding packages, customer bookings, payments, user accounts, and reviews. Instead of using an external SQL database, all records are persisted in plain `.txt` files using custom file handling, with a custom singly linked list and bubble sort for package management.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.2 (Spring Web + Thymeleaf)
- **Frontend:** HTML5, CSS3, Bootstrap 5
- **Storage:** File-based persistence (`.txt` files, pipe-delimited)
- **Build & Deployment:** Maven, Docker (multi-stage build)

## OOP Concepts & Data Structures

| Concept | Implementation in Project |
|---------|---------------------------|
| **Abstraction** | `Person`, `WeddingPackage`, and `Review` are abstract base classes defining abstract methods such as `getRole()`, `getDiscountedPrice()`, `getReviewType()`, and `toFileString()`. |
| **Inheritance** | `Person` -> `User` -> `AdminUser`; `WeddingPackage` -> `BasicPackage`, `PremiumPackage`; `Review` -> `PublicReview`, `VerifiedReview`. |
| **Polymorphism** | Subclasses override `getRole()`, `getDiscountedPrice()`, `getReviewType()`, and `toFileString()` to provide type-specific pricing, display labels, and serialization. |
| **Encapsulation** | All model classes (`User`, `WeddingPackage`, `Booking`, `Payment`, `Review`, etc.) keep fields `private` with public getters and setters. |
| **Linked List** | `VendorLinkedList` is a custom linked list implementation used to store and manage wedding packages in memory. |
| **Bubble Sort** | `sortByPrice()` and `sortByName()` inside `VendorLinkedList` sort packages without relying on built-in collection sorters. |
| **File Handling** | `FileHandler` handles reading and writing records to `.txt` files, and `PasswordUtil` hashes user passwords with SHA-256. |

## Team Contributions

### Vidura - User Management, Analytics Dashboard & Core Utilities
- **Models:** `Person.java`, `User.java`, `AdminUser.java`
- **Services & Controllers:** `UserService.java`, `UserController.java`, `DashboardController.java`
- **Utilities:** `FileHandler.java`, `IdGenerator.java`, `PasswordUtil.java`, `VendorLinkedList.java`
- **Views:** `home.html`, `dashboard.html`, `users/login.html`, `users/register.html`, `users/list.html`, `users/edit.html`, `users/profile.html`
- **Features:** Registration, login, profile updates, admin user management (CRUD + search), and dashboard analytics.

### Lahiru - Wedding Package Management
- **Models, Service & Controller:** `WeddingPackage.java`, `BasicPackage.java`, `PremiumPackage.java`, `PackageService.java`, `PackageController.java`
- **Views:** `packages/list.html`, `packages/detail.html`, `packages/add.html`, `packages/edit.html`
- **Features:** Create, view, sort (via `VendorLinkedList` + Bubble Sort), update, and delete wedding packages.

### Daham - Booking & Payment Management
- **Models, Services & Controller:** `Booking.java`, `Payment.java`, `BookingService.java`, `PaymentService.java`, `BookingController.java`
- **Views:** `bookings/list.html`, `bookings/detail.html`, `bookings/create.html`, `bookings/edit.html`
- **Features:** Create bookings, search/filter bookings, update booking status, delete bookings, and record/view payments.

### Chanuka - Feedback & Review Management
- **Models, Service & Controller:** `Review.java`, `PublicReview.java`, `VerifiedReview.java`, `ReviewService.java`, `ReviewController.java`
- **Views:** `reviews/list.html`, `reviews/submit.html`, `reviews/edit.html`
- **Features:** Submit public or verified reviews, list/filter reviews, edit, and delete reviews.

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.9+ (or IntelliJ IDEA with bundled Maven)

### Option 1: IntelliJ IDEA
1. Clone the repository:
   ```bash
   git clone https://github.com/IT25102240/SKU_OOP_G25_Wedding_Planning.git
   cd SKU_OOP_G25_Wedding_Planning
   ```
2. Open the project folder in IntelliJ IDEA (`File -> Open`) and let Maven sync dependencies.
3. Run `src/main/java/com/wedding/WeddingBookingApplication.java`.
4. Open `http://localhost:8080` in your browser.

### Option 2: Command Line (Maven)
```bash
mvn spring-boot:run
```
Then visit `http://localhost:8080`.

### Option 3: Docker
```bash
docker build -t everafter .
docker run -p 8080:8080 everafter
```

## Demo Accounts

Default sample data is seeded automatically into `/data` on startup if the files are missing or empty:

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@wedding.com` | `admin123` |
| Customer | `emma@example.com` | `customer123` |

## Data Storage

Records are stored in the `data/` directory (created automatically at runtime):
- `data/users.txt`
- `data/packages.txt`
- `data/bookings.txt`
- `data/payments.txt`
- `data/reviews.txt`

Each line represents one record with fields separated by `|`.

## Project Structure

```text
├── src/main/java/com/wedding/
│   ├── WeddingBookingApplication.java
│   ├── model/
│   │   ├── Person.java
│   │   ├── User.java
│   │   ├── AdminUser.java
│   │   ├── WeddingPackage.java
│   │   ├── BasicPackage.java
│   │   ├── PremiumPackage.java
│   │   ├── Booking.java
│   │   ├── Payment.java
│   │   ├── Review.java
│   │   ├── PublicReview.java
│   │   └── VerifiedReview.java
│   ├── util/
│   │   ├── VendorLinkedList.java
│   │   ├── FileHandler.java
│   │   ├── IdGenerator.java
│   │   └── PasswordUtil.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── PackageService.java
│   │   ├── BookingService.java
│   │   ├── PaymentService.java
│   │   └── ReviewService.java
│   └── controller/
│       ├── UserController.java
│       ├── PackageController.java
│       ├── BookingController.java
│       ├── ReviewController.java
│       └── DashboardController.java
├── src/main/resources/
│   ├── application.properties
│   └── templates/
│       ├── home.html
│       ├── dashboard.html
│       ├── shared/
│       │   └── layout.html
│       ├── users/
│       ├── packages/
│       ├── bookings/
│       └── reviews/
├── data/
├── Dockerfile
├── .dockerignore
└── pom.xml
```

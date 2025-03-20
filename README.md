# EnergyEmissions

# Spring Boot API with Secure Authentication & Payments

## Features Implemented  

### **User Authentication & Security**  
- Implemented **User Registration, Login, Email Verification**, and **Password Reset (via email)**  
- Used **JWT tokens** stored in cookies for secure authentication  
- Configured **Spring Security** to allow public access to:  
  - Registration & Login  
  - Health Check Endpoint  
  - Stripe Payment Webhook  
  - All other endpoints require authentication  
- Added **CORS filter** to restrict origins & methods, preventing **Cross-Site Request Forgery (CSRF) attacks**  

### **API Development & Logging**  
- Developed numerous **RESTful CRUD APIs** with **proper error handling**  
- Added a **Request Logging Filter** to:  
  - Log **query parameters**, **request payload**, **processing time**  
  - Set a **max request body length** for logging efficiency  

### **Background Processing & Scheduling**  
- Implemented **Scheduled Tasks** using **Cron expressions** to simulate data generation  
- Used **Pessimistic Locking** to prevent **data duplication** across multiple pods in a distributed environment  

### **Geolocation & Distance Calculation**  
- Integrated **Rapid API** to compute **air/land distance** between two coordinates  

### **Payments & File Storage**  
- **Integrated Stripe Payments** with webhooks to track payment status  
- Connected **Amazon S3** for storing reports and large data files  

## **Tech Stack**
- **Backend:** Spring Boot, Spring Security, JWT Authentication  
- **Database:** PostgreSQL  
- **Logging & Monitoring:** Custom Request Logging Filter  
- **APIs & Integrations:** Stripe, Amazon S3, Rapid API (Geolocation)  
- **Infrastructure:** Kubernetes, AWS, PostgresQL 

---

## **Setup & Installation**
### **Prerequisites**
- Java 17+  
- Maven  
- PostgreSQL  
- Stripe API Keys  
- AWS S3 Credentials  

### **Installation**
1. Clone the repository:  
   ```sh
   git clone https://github.com/your-repo-name.git
   cd your-repo-name
2. Configure env variables in application properties
3. RUN mvn spring-boot:run

# Habit Tracker - Dreamdeck Demo
A comprehensive, full-stack web application designed for tracking personal habits. This project utilizes a classic three-tier architecture and features a robust, cloud-native DevOps infrastructure.

## ⚠️ IMPORTANT NOTICE: 
> The complete DevOps infrastructure configuration—including Dockerfiles, docker-compose.yml, GitHub Actions CI/CD workflows, deployment scripts, and Terraform configurations—is currently located in the test branch. Please switch to the test branch to review the infrastructure setup.

### 🛠 Tech Stack
Application:
- Frontend: Angular, Node.js, Nginx (Reverse Proxy & Static File Serving)
- Backend: Java, Spring Boot, Gradle
- Database: PostgreSQL

### ☁️ DevOps & Infrastructure:
- Cloud Provider: AWS (Amazon Web Services)
- Compute: Amazon EC2 (t4g.medium, ARM architecture, Ubuntu Linux)
- Containerization: Docker, Docker Compose (Multi-stage builds)
- CI/CD: GitHub Actions
- Infrastructure as Code (IaC): Terraform
- Container Registry: AWS ECR (Elastic Container Registry)

## 🚀 Quick Start (Local Development)
To get the project up and running locally in a few minutes, follow these steps:

1. Clone the repository and switch to the infrastructure branch
```bash
git clone https://github.com/1vanytska/habit-tracker-dreamdeck-demo.git
cd habit-tracker-dreamdeck-demo
git checkout test
```

2. Configure Environment Variables
The project uses environment variables for security and configuration. Copy the provided example environment file to create your own .env file:
```bash
cp .env.example .env
```
Open the .env file and replace the placeholders with your actual credentials (database passwords, Google OAuth keys, etc.).

3. Launch the Application Stack
Run the following command to build and start all services (Frontend, Backend, and Database) in detached mode:
```bash
docker-compose up -d --build
```

4. Access the Application
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- Database: localhost:5432

Note: Use docker compose ps to ensure all containers are running, or docker compose logs -f to view real-time logs.

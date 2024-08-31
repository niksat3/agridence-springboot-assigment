# Running the Application

Follow these steps to run the Assignment application using Docker:

1. **Prerequisites**
   - Ensure you have Docker installed on your system
   - Make sure you have Docker Compose installed (usually comes with Docker Desktop)

2. **Clone the Repository**
   ```
   git clone git@github.com:niksat3/agridence-springboot-assigment.git
   cd Assignment
   ```

3. **Create the .env file**
   Create a file named `.env` in the root directory of the project with the following content:
   ```
   JWT_SECRET=a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6
   JWT_EXPIRATION=3600000
   ```
   Note: In a production environment, use a strong, unique secret and never share it publicly. You can also adjust it according to your needs.

4. **Build the Docker Image**
   ```
   docker build -t assignment-app .
   ```

5. **Run the Application**
   ```
   docker-compose up
   ```

6. **Verify the Application is Running**
   - Open a web browser or use a tool like curl
   - Access `http://localhost:8086` (adjust the port if you've configured a different one)

7. **Test the Signup Endpoint**
   - Use a tool like Postman or curl to send a POST request
   - Endpoint: `http://localhost:8080/signup`
   - Headers: 
     - `Content-Type: application/json`
   - Request body:
     ```json
     {
       "username": "testuser",
       "email": "testuser@example.com",
       "password": "securePassword123"
     }
     ```

8. **Stopping the Application**
   - Press `Ctrl + C` in the terminal where the application is running
   - Then run: `docker-compose down`

Note: Ensure that your project includes a `Dockerfile` and a `docker-compose.yml` file configured for your application. Adjust any configurations in these files if necessary before running.
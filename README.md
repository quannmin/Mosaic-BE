# Mosaic Store - T-Shirt E-Commerce Platform

A modern, scalable e-commerce platform built with Spring Boot for selling and managing T-shirt products.

## 🚀 Features

### Product Management
- **Product Catalog**: Complete product management with variants (sizes, colors)
- **Image Management**: AWS S3 integration for reliable product image storage
- **Variant-based Inventory**: Track stock for each product variant
- **Pricing Flexibility**: Support for base prices, discounts, and quantity-based pricing

### User Experience
- **RESTful API**: Well-documented API endpoints using Swagger/OpenAPI
- **Response Standardization**: Consistent API response format with status codes
- **Secure Authentication**: JWT-based authentication system

### Technical Architecture
- **Modular Design**: Clean separation of controllers, services, and repositories
- **Data Validation**: Comprehensive request validation
- **Cloud Integration**: AWS S3 for scalable asset management
- **Database**: PostgreSQL with JPA/Hibernate for data persistence

## 🔧 Technology Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Authentication**: Spring Security with JWT

### Cloud Services
- **Image Storage**: AWS S3 for secure and scalable image management
- **Deployment**: (Your deployment platform)

## 📋 Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL
- AWS Account (for S3 access)

### Configuration
The application requires basic configuration for:
- Database connection details
- AWS credentials and S3 bucket information
- Server port and other Spring Boot properties

See `application.yml` for all configuration options.

### Running the Application
1. Clone the repository
2. Build the project: `mvn clean install`
3. Run the application: `mvn spring-boot:run`
4. Access Swagger UI: `http://localhost:8080/swagger-ui.html`

## 🌟 Future Enhancements

- Order and payment processing integration
- User profile management
- Administrator dashboard
- Recommendation engine
- Advanced search functionality
- Email notifications

## 📄 License

[MIT License](LICENSE)

## 📧 Contact

- Email: louisquinn296@gmail.com
- GitHub: [My GitHub Profile](https://github.com/quannmin)

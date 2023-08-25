# Retail Store Discounts

## Description

The Retail Store Discounts project is a web-based application that calculates discounts for users shopping on a retail
website. It provides a seamless and efficient way to apply discounts based on user profiles and purchase amounts. Here's
how it works:

### Discount Rules

1. **Employee Discount**: If the user is an employee of the store, they are eligible for a 30% discount on their
   purchase.

2. **Affiliate Discount**: Users who are affiliates of the store enjoy a 10% discount on their orders.

3. **Customer Loyalty Discount**: Customers who have been shopping with the store for over 2 years receive a 5% discount
   as a token of appreciation for their loyalty.

4. **Bill Amount Discount**: For every $100 spent on the bill, the user receives a $5 discount. For instance, if a
   user's bill totals $990, they receive a $45 discount.

5. **Exclusion of Grocery Items**: Percentage-based discounts do not apply to grocery items, ensuring fair pricing for
   essential products.

6. **Single Discount Limit**: A user can receive only one of the percentage-based discounts on a bill. The system
   automatically selects the highest applicable discount for the user.

### How It Works

Users can shop on the retail website and add items to their cart. When they proceed to checkout, the Retail Store
Discounts system calculates their eligible discounts based on their user profile and the total bill amount. The final
payable amount is displayed to the user, reflecting any applicable discounts.

## Project Features

- User authentication and role management.
- Automatic discount calculation based on user profiles and purchase amount.
- Exclusion of grocery items from percentage-based discounts.
- Clear and transparent pricing for users.
- Flexibility to add new discount rules in the future.
- Extensive unit testing for code reliability and accuracy.

## Technologies Used

- Java 17
- Spring Boot 3
- Spring Security
- Hibernate JPA
- MongoDB
- Docker
- Hibernate Validation
- Mockito

## Getting Started

To run the project locally and see the discount calculation in action, follow these steps:

### Prerequisites

- Java 17
- Maven
- Docker (if you want to run the project in a container)

### Installation

1. Clone the repository: `git clone https://github.com/ramimohsen/Retail-Store-Discounts.git`
2. Build the project: `mvn clean install`
3. Run the project: `java -jar target/retail-store-discounts.jar`

## Usage

Once the project is up and running, you can access the retail website and explore how discounts are applied during the
checkout process. Refer to the [Swagger documentation](http://localhost:8080/swagger-ui/index.html#/) for API details.

**Authentication:**

- The service is secured with JWT (JSON Web Token) authentication.
- To access the protected endpoints, you need to log in first.
- After successful login, you will receive a JWT token.
- Include this JWT token in the `Authorization` header of subsequent requests as a bearer token to authenticate
  yourself.

**Default Users:**

When the application is running, three default users are added to the system:

1. **User 1**
    - Email: test1@gmail.com
    - Password: admin123
    - Roles: ROLE_EMPLOYEE, ROLE_AFFILIATE

2. **User 2**
    - Email: test2@gmail.com
    - Password: admin123
    - Roles: ROLE_AFFILIATE

3. **User 3**
    - Email: test3@gmail.com
    - Password: admin123
    - Roles: ROLE_CUSTOMER

These default users are provided for testing and demonstration purposes.

Please ensure you generate and use the JWT token for authorized access to the service.

**Default Discount Rules:**

When the application is running, a set of default discount rules is available for use. These rules are designed to
provide discounts to different user profiles based on specific criteria. Here are the default discount rules:

**Employee Discount**

- Description: Employee discount rule
- Discount Percentage: 30%
- Active: Yes

**Affiliate Discount**

- Description: Affiliate discount rule
- Discount Percentage: 10%
- Active: Yes

**Customer Loyalty Discount**

- Description: Customer for over 2 years discount rule
- Discount Percentage: 5%
- Active: Yes

**Bill Amount Discount**

- Description: For every $100 on the bill, there would be a $5 discount (e.g., for $990, you get $45 as a discount)
- Discount Percentage: 5%
- Active: Yes

These default discount rules are automatically applied based on user profiles and purchase amounts when using the
application. You can also extend or customize these rules as needed to meet specific business requirements.

For more details on how these rules are applied and the application's features, please refer to the [Usage](#usage)
section in this README.

If you want to add, modify, or deactivate discount rules, you can do so through the application's administrative
interface or by directly updating the database.

Please note that the default rules provided here are for demonstration purposes, and you can tailor them to match your
specific business logic.

## Contributing

We welcome contributions to improve and expand the Retail Store Discounts project. To contribute, follow these
guidelines:

- Fork the repository.
- Create a feature branch: `git checkout -b feature-name`
- Commit your changes: `git commit -m 'Add some feature'`
- Push to the branch: `git push origin feature-name`
- Create a pull request.

## License

This project is licensed under the [MIT License]().

## Acknowledgments

We'd like to thank the open-source community for their valuable contributions and inspiration.

For any questions or issues, please [open an issue](https://github.com/ramimohsen/Retail-Store-Discounts/issues).

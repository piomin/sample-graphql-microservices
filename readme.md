## GraphQL - The Future of Microservices?  [![Twitter](https://img.shields.io/twitter/follow/piotr_minkowski.svg?style=social&logo=twitter&label=Follow%20Me)](https://twitter.com/piotr_minkowski)

Detailed description can be found here: [GraphQL - The Future of Microservices?](https://piotrminkowski.com/2018/08/16/graphql-the-future-of-microservices/) 

## Overview

This project demonstrates a **microservices architecture using GraphQL** with Spring Boot, Spring Cloud (Eureka), and the GraphQL Java Kickstart library. Instead of traditional REST-based communication, services expose GraphQL APIs and communicate with each other using the Apollo GraphQL client. Eureka is used for service discovery.

## Architecture

The system consists of four microservices organized in a hierarchical domain model:

```
                        +---------------------+
                        |  discovery-service   |
                        |   (Eureka Server)    |
                        |     Port: 8061       |
                        +---------------------+
                           /       |        \
                          /        |         \
              +-----------+  +-----------+  +------------------+
              | employee  |  | department|  |  organization    |
              | service   |  |  service  |  |    service       |
              | Port:8090 |  | Port:8091 |  | (from config)    |
              +-----------+  +-----------+  +------------------+
```

### Inter-Service Communication

```
organization-service --[Apollo GraphQL]--> employee-service
organization-service --[Feign REST]------> department-service
department-service   --[Apollo GraphQL]--> employee-service
```

- **department-service** calls **employee-service** via Apollo GraphQL to fetch employees belonging to a department.
- **organization-service** calls **employee-service** via Apollo GraphQL to fetch employees belonging to an organization.
- **organization-service** calls **department-service** via Feign REST client.
- All services register with the **Eureka discovery server**. GraphQL clients use `EurekaClient` to look up service instances at runtime with random instance selection for client-side load balancing.

### Data Model

```
Organization (1) --> (*) Department --> (*) Employee
Organization (1) --> (*) Employee
```

Each service maintains its own simplified view of the domain model, containing only the fields it needs. All data is stored in-memory using `ArrayList`-based repositories and is pre-seeded at application startup.

## Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Language |
| Spring Boot | 2.7.18 | Application framework |
| Spring Cloud | 2021.0.9 | Microservices infrastructure (Eureka, Config, OpenFeign, Sleuth) |
| GraphQL Java Kickstart | 14.1.0 | Schema-first GraphQL endpoint auto-configuration |
| GraphiQL | 11.1.0 | Interactive GraphQL IDE (browser-based) |
| Voyager | 11.1.0 | GraphQL schema visualization |
| Apollo GraphQL Client | 1.0.1 | Inter-service GraphQL communication |
| graphql-java-tools | 13.0.2 | Resolver-based schema wiring |

## Project Structure

```
sample-graphql-microservices/
|-- pom.xml                          # Parent POM (multi-module)
|-- discovery-service/               # Eureka Server
|-- employee-service/                # Employee GraphQL API
|-- department-service/              # Department GraphQL API
|-- organization-service/            # Organization GraphQL API
```

## Modules

### discovery-service

Netflix Eureka Server for service registration and discovery.

- **Port:** 8061
- **Annotations:** `@EnableEurekaServer`
- **Configuration:** Self-registration disabled; acts as a standalone registry.

### employee-service

Manages employee data and exposes a GraphQL API.

- **Port:** 8090
- **Annotations:** `@EnableDiscoveryClient`
- **Pre-seeded data:** 10 employees across 2 organizations and 4 departments.

**GraphQL Schema** (`employee.graphqls`):

| Queries | Description |
|---|---|
| `employees: [Employee]` | List all employees |
| `employee(id: ID!): Employee!` | Find employee by ID |
| `employeesByOrganization(organizationId: Int!): [Employee]` | Find employees by organization |
| `employeesByDepartment(departmentId: Int!): [Employee]` | Find employees by department |

| Mutations | Description |
|---|---|
| `newEmployee(employee: EmployeeInput!): Employee` | Create a new employee |
| `deleteEmployee(id: ID!): Boolean` | Delete an employee |
| `updateEmployee(id: ID!, employee: EmployeeInput!): Employee` | Update an employee |

**Employee type fields:** `id`, `organizationId`, `departmentId`, `name`, `age`, `position`, `salary`

### department-service

Manages department data. Calls **employee-service** via Apollo GraphQL to fetch employees belonging to a department.

- **Port:** 8091
- **Annotations:** `@EnableDiscoveryClient`, `@EnableFeignClients`
- **Pre-seeded data:** 4 departments across 2 organizations.
- **Distributed tracing:** Enabled via Spring Cloud Sleuth.

**GraphQL Schema** (`department.graphqls`):

| Queries | Description |
|---|---|
| `departments: [Department]` | List all departments |
| `department(id: ID!): Department!` | Find department by ID |
| `departmentsByOrganization(organizationId: Int!): [Department]` | Find departments by organization |
| `departmentsByOrganizationWithEmployees(organizationId: Int!): [Department]` | Find departments by organization with employees fetched from employee-service |

| Mutations | Description |
|---|---|
| `newDepartment(department: DepartmentInput!): Department` | Create a new department |
| `deleteDepartment(id: ID!): Boolean` | Delete a department |
| `updateDepartment(id: ID!, department: DepartmentInput!): Department` | Update a department |

**Department type fields:** `id`, `organizationId`, `name`, `employees: [Employee]`

### organization-service

Manages organization data. Calls **employee-service** via Apollo GraphQL and **department-service** via Feign REST.

- **Annotations:** `@EnableDiscoveryClient`, `@EnableFeignClients`
- **Pre-seeded data:** 2 organizations (Microsoft, Oracle).

**GraphQL Schema** (`organization.graphqls`):

| Queries | Description |
|---|---|
| `organizations: [Organization]` | List all organizations |
| `organization(id: ID!): Organization!` | Find organization by ID |
| `organizationByIdWithEmployees(id: Int!): Organization` | Find organization with employees from employee-service |
| `organizationByIdWithDepartments(id: Int!): Organization` | Find organization with departments |
| `organizationByIdWithDepartmentsAndEmployees(id: Int!): Organization` | Find organization with departments and employees |

| Mutations | Description |
|---|---|
| `newOrganization(organization: OrganizationInput!): Organization` | Create a new organization |
| `deleteOrganization(id: ID!): Boolean` | Delete an organization |
| `updateOrganization(id: ID!, organization: OrganizationInput!): Organization` | Update an organization |

**Organization type fields:** `id`, `name`, `address`, `employees: [Employee]`, `departments: [Department]`

## Prerequisites

- **Java 17** or higher
- **Maven 3.x**
- No external database required (all data is in-memory)

## Building the Project

Clone the repository and build all modules:

```bash
git clone https://github.com/piomin/sample-graphql-microservices.git
cd sample-graphql-microservices
mvn clean package
```

## Running the Services

Start the services in the following order:

```bash
# 1. Start the Eureka discovery server
java -jar discovery-service/target/discovery-service-1.0-SNAPSHOT.jar

# 2. Start the employee service
java -jar employee-service/target/employee-service-1.0-SNAPSHOT.jar

# 3. Start the department service
java -jar department-service/target/department-service-1.0-SNAPSHOT.jar

# 4. Start the organization service
java -jar organization-service/target/organization-service-1.0-SNAPSHOT.jar
```

## Accessing the Services

Once the services are running, you can interact with them using the following endpoints:

| Service | GraphQL Endpoint | GraphiQL UI | Voyager |
|---|---|---|---|
| employee-service | `http://localhost:8090/graphql` | `http://localhost:8090/graphiql` | `http://localhost:8090/voyager` |
| department-service | `http://localhost:8091/graphql` | `http://localhost:8091/graphiql` | `http://localhost:8091/voyager` |
| Eureka Dashboard | - | - | `http://localhost:8061` |

- **GraphiQL** provides an interactive, in-browser GraphQL IDE for writing and testing queries.
- **Voyager** provides a visual, interactive representation of the GraphQL schema.

## Sample Queries

### Employee Service

Query all employees:
```graphql
{
  employees {
    id
    name
    age
    position
    salary
    departmentId
    organizationId
  }
}
```

Query a single employee:
```graphql
{
  employee(id: 1) {
    name
    position
    salary
  }
}
```

Find employees by department:
```graphql
{
  employeesByDepartment(departmentId: 1) {
    id
    name
    position
  }
}
```

Create a new employee:
```graphql
mutation {
  newEmployee(employee: {
    organizationId: 1
    departmentId: 1
    name: "Jane Doe"
    age: 28
    position: "Designer"
    salary: 4000
  }) {
    id
    name
  }
}
```

### Department Service

Query departments by organization with employees (inter-service call):
```graphql
{
  departmentsByOrganizationWithEmployees(organizationId: 1) {
    id
    name
    employees {
      id
      name
      position
      salary
    }
  }
}
```

### Organization Service

Query an organization with its employees (inter-service call):
```graphql
{
  organizationByIdWithEmployees(id: 1) {
    id
    name
    address
    employees {
      id
      name
    }
  }
}
```

## Key Design Patterns

1. **Schema-first GraphQL** -- `.graphqls` schema files define the API contract; Java resolver classes (`GraphQLQueryResolver`, `GraphQLMutationResolver`) implement the operations.
2. **GraphQL for inter-service communication** -- Services call each other using the Apollo GraphQL client instead of traditional REST, demonstrating GraphQL as a service mesh communication layer.
3. **Service discovery** -- Eureka handles service registration and discovery. Apollo clients use `EurekaClient` to resolve service URLs at runtime.
4. **Simplified domain models** -- Each service maintains its own view of shared entities (e.g., `Employee`) with only the fields it needs, avoiding tight coupling.
5. **In-memory repositories** -- `ArrayList`-based data stores pre-seeded via Spring `@Bean` methods for demonstration purposes.

## CI/CD

The project uses **CircleCI** for continuous integration:
- Runs Maven tests on every commit using `cimg/openjdk:17.0`
- Performs static analysis via **SonarCloud** (`sonar-maven-plugin`)

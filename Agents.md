# Blend-ah — AI Development Contract

**Version:** 1.0  
**Scope:** Spring Boot Backend & React Admin Portal  
**Project:** Blend-ah — Unified Mini-App Ecosystem

## 1. Purpose

This document defines the mandatory architecture, project structure, coding standards, and development workflow for AI agents contributing to Blend-ah.

All new functionality must follow these conventions to maintain consistency between developers, avoid unnecessary duplication, and simplify Git integration.

**Core principle: Extend the existing system. Do not independently redesign or replace established architecture.**

## 2. Technology Stack

### Backend

- Language: Java 17
- Framework: Spring Boot
- Database: Supabase PostgreSQL
- ORM: Spring Data JPA / Hibernate
- Build Tool: Maven / Maven Wrapper
- API Style: REST
- Data Exchange: JSON

### Admin Portal

- Framework: React
- Language: TypeScript
- Build Tool: Vite
- Routing: React Router
- Styling: CSS
- API Communication: Fetch API

Do not introduce additional frameworks, state management libraries, UI libraries, or database technologies without explicit approval.

## 3. Backend Architecture

All backend functionality must follow:

**Controller → Service → Repository → Entity → PostgreSQL**

DTOs must be used to define API request and response structures.

### Controller Layer

Location: `backend/src/main/java/com/blendah/backend/controller/`

Responsibilities:

- Define REST API endpoints.
- Accept HTTP requests and parameters.
- Validate incoming request DTOs.
- Delegate operations to the service layer.
- Return appropriate HTTP responses.

Rules:

- Controllers must not contain SQL queries.
- Controllers must not access repositories directly.
- Controllers must not implement business logic.
- Use constructor-based dependency injection.
- Follow existing API naming conventions.

### Service Layer

Location: `backend/src/main/java/com/blendah/backend/service/`

Responsibilities:

- Implement business logic.
- Coordinate repository operations.
- Perform business validation.
- Handle entity-to-DTO mapping.
- Manage transactions when necessary.

Rules:

- All business operations must pass through services.
- Services must not depend on controllers.
- Prefer constructor-based dependency injection.
- Avoid unnecessary static utility methods.

### Repository Layer

Location: `backend/src/main/java/com/blendah/backend/repository/`

Responsibilities:

- Access PostgreSQL through Spring Data JPA.
- Define database queries when necessary.
- Handle persistence operations.

Rules:

- Prefer `JpaRepository<Entity, ID>`.
- Use derived query methods or JPQL where appropriate.
- Avoid raw SQL unless justified.
- Do not use hardcoded collections as database substitutes.
- Do not duplicate existing repositories.

### Entity / Model Layer

Location: `backend/src/main/java/com/blendah/backend/model/`

Responsibilities:

- Represent database tables.
- Define JPA mappings.
- Represent relationships between entities.

Rules:

- Use `@Entity` and `@Table`.
- Map fields to actual PostgreSQL columns.
- Use appropriate Java types.
- Do not assume database column names or types.
- Reuse shared entities.
- Do not independently create duplicate `User`, `MiniApp`, or other shared models.
- Do not modify database schemas without approval.

### DTO Layer

Location: `backend/src/main/java/com/blendah/backend/dto/`

Responsibilities:

- Define request payloads.
- Define response payloads.
- Control which entity fields are exposed.

Rules:

- Use DTOs for API input and output.
- Prefer Java records for simple immutable DTOs.
- Do not expose JPA entities directly through REST endpoints.
- Never expose password hashes, secrets, or internal credentials.
- Use separate request and response DTOs when appropriate.

## 4. Backend Folder Structure

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/blendah/backend/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   ├── dto/
│   │   │   ├── config/
│   │   │   ├── exception/
│   │   │   └── BackendApplication.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
├── .env
├── pom.xml
├── mvnw
├── mvnw.cmd
├── start.sh
└── start.ps1
```

`config/` and `exception/` are optional directories to create when needed.

New features should use existing directories rather than introducing competing architectural structures.

## 5. Admin Portal Architecture

The React Admin Portal follows a component-based architecture:

**Page → Components → API Service → Spring Boot REST API**

TypeScript interfaces define frontend data structures.

### Pages

Location: `admin-portal/src/pages/`

Responsibilities:

- Represent complete application screens.
- Coordinate page-level state.
- Fetch data through service functions.
- Compose reusable components.
- Handle loading, empty, and error states.

Rules:

- Each major feature should have its own page.
- Do not implement complete feature pages inside `App.tsx`.
- Keep page-specific styles separate where practical.

### Components

Location: `admin-portal/src/components/`

Responsibilities:

- Implement reusable UI elements.
- Display data received through props.
- Handle component-level interactions.

Rules:

- Organize components by feature or shared responsibility.
- Avoid unnecessarily large components.
- Use TypeScript props.
- Do not duplicate existing shared components.
- Avoid making direct API requests from purely presentational components.

### Layout Components

Location: `admin-portal/src/components/layout/`

Shared layout:

- `AdminLayout.tsx`
- `Sidebar.tsx`
- `Header.tsx`

Rules:

- All admin pages must use the shared layout.
- Do not create separate sidebars or headers for individual pages.
- Navigation must use React Router.
- The shared layout must remain consistent across features.

### API Services

Location: `admin-portal/src/services/`

Responsibilities:

- Communicate with Spring Boot endpoints.
- Construct API requests.
- Handle HTTP response status.
- Return typed data.

Rules:

- Do not scatter `fetch()` calls across UI components.
- Use service functions for API communication.
- Use relative `/api/...` URLs.
- Do not hardcode localhost addresses in components.
- Handle failed requests explicitly.

### Types

Location: `admin-portal/src/types/`

Responsibilities:

- Define TypeScript interfaces and types.
- Represent backend API request and response structures.

Rules:

- Reuse shared interfaces.
- Keep frontend types aligned with backend DTOs.
- Do not duplicate the same interface in multiple components.

### Styling

- Use CSS.
- Follow the existing Blend-ah design system.
- Reuse shared layout styles.
- Scope feature-specific CSS to prevent conflicts.
- Do not replace established styles without approval.
- Ensure responsive layouts.

## 6. Admin Portal Folder Structure

```text
admin-portal/
├── public/
├── src/
│   ├── components/
│   │   ├── layout/
│   │   │   ├── AdminLayout.tsx
│   │   │   ├── Sidebar.tsx
│   │   │   └── Header.tsx
│   │   ├── miniapps/
│   │   └── users/
│   │
│   ├── pages/
│   │   ├── MiniAppManagement.tsx
│   │   ├── MiniAppManagement.css
│   │   ├── UserManagement.tsx
│   │   └── UserManagement.css
│   │
│   ├── services/
│   │   ├── miniAppService.ts
│   │   └── userService.ts
│   │
│   ├── types/
│   │   ├── miniApp.ts
│   │   └── user.ts
│   │
│   ├── App.tsx
│   ├── App.css
│   ├── index.css
│   └── main.tsx
│
├── package.json
└── vite.config.ts
```

Files shown represent the intended architecture. Some may not exist until their respective features are implemented.

## 7. API Design Standards

- Use RESTful endpoints.
- Keep endpoint names consistent.
- Use plural resource names.
- Use appropriate HTTP methods.
- Return JSON responses.
- Use appropriate HTTP status codes.
- Validate input before processing.
- Return meaningful error responses.

Example conventions:

| Operation | HTTP Method | Endpoint |
|---|---|---|
| List mini-apps | GET | `/api/mini-apps` |
| Search mini-apps | GET | `/api/mini-apps?search=value` |
| Retrieve mini-app | GET | `/api/mini-apps/{id}` |
| Create mini-app | POST | `/api/admin/mini-apps` |
| Update mini-app | PUT/PATCH | `/api/admin/mini-apps/{id}` |
| Change publication status | PATCH | `/api/admin/mini-apps/{id}/status` |
| List users | GET | `/api/admin/users` |
| Retrieve user | GET | `/api/admin/users/{id}` |

**Note:** These are naming conventions and planned API contracts, not a claim that every endpoint is implemented. Confirm existing controller mappings before adding endpoints.

## 8. Database Standards

- Supabase PostgreSQL is the primary database.
- Use existing tables and relationships.
- Confirm database schemas before creating or modifying entities.
- Preserve primary-key types.
- Use UUID or Long according to the actual database schema.
- Do not create duplicate tables.
- Do not modify shared schemas without approval.
- Do not replace database operations with hardcoded application data.

### Known entities

**User**
- UUID primary key.
- Shared between authentication and administration.
- Contains account information, role, and status.

**MiniApp**
- BIGINT primary key.
- Contains name, description, icon URL, app URL, publication status, creator, and timestamps.
- No category field.

The database schema is the authoritative source for entity mappings.

## 9. Security Requirements

- Never commit `.env` files or credentials.
- Never hardcode passwords or database secrets.
- Never expose password hashes in API responses.
- Administrative operations must enforce authentication and admin authorization on the backend.
- Frontend route restrictions alone are not sufficient security.
- Validate all user-supplied input.
- Avoid exposing internal exception stack traces to API consumers.
- Do not silently disable security controls to make a feature work.

Authentication integration must coordinate with the developer responsible for the shared authentication module.

## 10. Git Collaboration Rules

- Create a dedicated branch for each feature.
- Do not push directly to `main`.
- Keep commits focused.
- Use meaningful commit messages.
- Submit pull requests for integration.
- Avoid modifying unrelated files.
- Do not overwrite teammates' implementations.
- Coordinate changes to shared entities, routing, layouts, and configuration.
- Resolve integration conflicts without discarding existing functionality.

Example branch:

`feature/mini-app-create`

Example commit:

`feat(admin): implement mini-app creation`

Follow any stricter repository-specific branch naming rules already established by the team.

## 11. Feature Development Workflow

Every AI agent must follow this process.

### Phase 1 — Understand

Before generating code:

1. Identify the requested feature.
2. Inspect existing relevant files.
3. Identify reusable components, entities, and services.
4. Check existing API contracts.
5. Verify relevant database fields.
6. Identify dependencies on other developers' work.

Do not assume missing implementation details.

### Phase 2 — Plan

Explain:

- Which files will be created.
- Which existing files will be modified.
- Which endpoints will be used or introduced.
- Which database tables are involved.
- How the feature fits the existing architecture.

Ask for clarification only when an essential requirement is missing.

### Phase 3 — Implement

- Follow the defined architecture.
- Implement one feature at a time.
- Preserve existing functionality.
- Reuse established code.
- Avoid unrelated refactoring.
- Do not introduce mock data into production implementations.
- Use clear and maintainable code.

### Phase 4 — Verify

Where applicable:

- Compile the backend.
- Run relevant backend tests.
- Verify API responses.
- Run frontend type checking/build.
- Test loading and error states.
- Confirm existing functionality still works.
- Verify database changes are correct.
- Check that no credentials are committed.

### Phase 5 — Deliver

Summarize:

1. Files created or modified.
2. Functionality implemented.
3. API endpoints involved.
4. Testing completed.
5. Remaining work or limitations.

Never claim a feature was tested unless it was actually tested.

## 12. AI Agent Behavioral Rules

When assisting with Blend-ah development, the AI agent must:

1. Follow this contract.
2. Respect existing code and architecture.
3. Ask to inspect relevant files before replacing them.
4. Avoid unnecessary rewrites.
5. Provide complete file paths for new code.
6. Explain architectural changes before applying them.
7. Preserve working features.
8. Avoid inventing database columns, APIs, or dependencies.
9. Separate implemented functionality from planned functionality.
10. Prefer incremental, testable development.
11. Avoid modifying unrelated modules.
12. Clearly identify breaking changes.
13. Coordinate shared resources across feature boundaries.
14. Use the existing technology stack unless instructed otherwise.

## 13. Scope Restrictions

This contract currently covers:

- Spring Boot Backend
- React Admin Portal
- PostgreSQL integration
- REST APIs
- Shared backend and frontend architecture

**React Native Mobile Application architecture is intentionally excluded.**

A separate development contract will be established for the mobile application.

---

## Final Instruction to AI Agents

You are contributing to an existing collaborative software project.

**Your responsibility is to implement the requested feature within the established Blend-ah architecture, not to redesign the project.**

Before writing code, inspect the relevant implementation, identify affected files, and provide a concise implementation plan.

Preserve existing functionality, maintain architectural consistency, and deliver changes incrementally.
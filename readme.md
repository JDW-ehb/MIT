# MIT — Most Important Tasks
A focused productivity and collaboration platform built with Spring Boot

## Overview

The MIT platform is designed around a simple productivity principle:

People become more effective when they limit themselves to **a small number of meaningful daily objectives** instead of overwhelming themselves with long task lists.

This application allows users to define up to **three “Most Important Tasks” (MITs) per day**, track progress, and optionally share these goals publicly or within groups to create a sense of accountability and teamwork.

The platform also functions as a lightweight social productivity space, where users can browse objectives, join groups, and follow progress in an environment that encourages balance and realistic planning.

---

## Core Features

### Daily MIT Objectives

Users may define and manage their daily Most Important Tasks:

- Maximum three per day to promote focus
- Optional sub-objectives for structure
- Tasks grouped into categories such as Work, Health, Study, etc.
- Ability to indicate skills used or developed

This structure encourages consistent, achievable progress instead of overloaded planning.

---

### Public Objectives Catalog

The application provides a catalog-style list of user objectives. Users can:

- Browse publicly visible objectives
- View objectives per user
- Explore tasks grouped by category or type
- Open user profiles to see public information and objectives

This enables a community-driven layer of shared productivity.

---

### Groups and Collaboration

Users may:

- Create groups
- Share and track objectives with group members
- Maintain both personal and collaborative objectives

---

### Filtering and Search

Users can filter objectives by:

- Category
- User
- Type
- Group association

---

### Security and Authentication

The system implements:

- Secure login and registration
- Password hashing using **BCrypt**
- Authentication-protected areas
- Configurable session timeout

User accounts and sensitive areas are protected by default.

---

## Technology Stack

This project is built using:

- Java (Spring Boot)
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL 
- HTML / CSS / JavaScript
- docker

The application follows the MVC architectural pattern.

---

## Missing / Planned Features

Two important features are not yet implemented but are planned for future versions.

### 1. Public / Private Visibility Controls

Planned functionality will allow users to control the visibility of objectives and profile elements, including options such as:

- Public
- Contacts / group-only
- Private

Current state:

- Objectives shown in the catalog do not yet support granular privacy control.

This will improve privacy and selective sharing.

---

### 2. Profile Editing

Planned functionality includes:

- Editing profile information
- Updating profile images
- Modifying country and biography

Current state:

- Profiles cannot be edited after account creation.

This will allow users to maintain accurate and up-to-date profiles.

---

## Running the Application

To run the project:

to run the db & phpmyadmin
```bash
docker-compose up -d
```

launching the java spring boot app
```bash
mvn spring-boot:run

```

## Primary Routes

Primary routes include:

/login

/register

/catalog

/groups

/profile/{username}



Authenticated access is required for protected sections of the platform.

---

## Purpose and Direction

This platform is intended for users who value:

- Structured focus
- Realistic daily planning
- Sustainable progress

---

## Author

**MIT Productivity Platform**  
Developed by **Jimmy De Wit**

---

## AI Assistance and Credits

Part of the documentation writing, UI refinement suggestions, troubleshooting support, and feature-design brainstorming was assisted using **ChatGPT (OpenAI)**.

Conversation references:

- https://chatgpt.com/share/6957fb15-bf40-8005-bc95-18fcb126f3fd
- https://chatgpt.com/share/695a99cb-26bc-8005-b447-d3c2bbc4ee3b
- https://chatgpt.com/share/695ac4d6-23cc-8005-b624-c92bf2125329

Final implementation decisions and project ownership remain with the author.

---

## Future Roadmap

Planned or potential next steps:

- Objective visibility controls
- Editable user profiles
- Group invitations and admin tools
- Basic analytics on objective completion
- Email notifications
- Improved UX and visual themes

Development will continue iteratively as requirements evolve.





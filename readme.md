MIT — Most Important Tasks

A focused productivity and collaboration platform built with Spring Boot

Overview

The MIT platform is designed around a simple productivity principle:

People become more effective when they limit themselves to a small number of meaningful daily objectives instead of overwhelming themselves with long task lists.

This application allows users to define up to three “Most Important Tasks” (MITs) per day, track progress, and optionally share these goals publicly or within groups to create a sense of accountability and teamwork.

The platform also functions as a lightweight social productivity space, where users can browse objectives, join groups, and follow progress in an environment that encourages balance and realistic planning.

Core Features
Daily MIT Objectives

Users may define and manage their daily Most Important Tasks:

Maximum three per day to promote focus

Optional sub-objectives for structure

Tasks grouped into categories such as Work, Health, Study, etc.

Ability to indicate skills used or developed

This structure encourages consistent, achievable progress instead of overloaded planning.

Public Objectives Catalog

The application provides a catalog-style list of user objectives. Users can:

Browse public objectives

View objectives per user

Explore tasks grouped by category or type

Open user profiles to see additional information and public goals

This enables a community-driven layer of shared productivity.

Groups and Collaboration

Users may:

Create groups

Join groups

Share and track objectives with group members

Maintain both personal and collaborative objectives

This allows coordinated task-setting and mutual accountability.

Filtering and Search

Users can filter objectives by:

Category

User

Objective type

Group association

This makes the catalog usable at scale.

Security and Authentication

The system implements:

Secure login and registration

Password hashing via BCrypt

Session-secured areas requiring authentication

Configurable session timeout within Spring Security

User accounts and sensitive areas of the application are protected by default.

Technology Stack

This project is built using:

Java (Spring Boot)

Spring Security

Spring Data JPA

Thymeleaf

MySQL (or compatible relational database)

HTML / CSS / JavaScript

The overall architecture follows the MVC pattern.

Missing / Planned Features

Two key features are intentionally not yet implemented. These are considered future improvements.

1. Public / Private Visibility Controls

Planned functionality will allow users to choose which objectives and profile elements are visible. Intended settings include:

Public visibility

Contacts / group-only visibility

Private objectives

Current state:

Objectives shown in the catalog do not yet support granular visibility levels.

This feature will enable better privacy management and professional use-cases.

2. Profile Editing

Planned functionality:

Modify profile information

Update profile images

Edit country and biography fields

Current state:

Profile data cannot be edited after account creation

This will allow users to maintain an evolving and accurate public identity.

Running the Application

To run the project in development:

mvn spring-boot:run


Default routes include:

/login
/register
/catalog


A valid account is required to access authenticated sections.

Purpose and Direction

This platform is intended for individuals and teams who value:

Structured focus

Realistic goal-setting

Consistency over intensity

Light social accountability

A clean and distraction-free workflow

The application prioritizes clarity, intentionality, and measured progress rather than volume-driven productivity.

Author

MIT Productivity Platform
Developed by Jimmy De Wit

Future Roadmap (Non-Exhaustive)

Planned or possible developments include:

Profile editing

Objective privacy controls

Analytics and reporting

Group enhancements

Notifications

Extended filtering and browsing tools

The application is designed to grow iteratively while maintaining simplicity.
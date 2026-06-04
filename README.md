# 📋 GestorApp — Gestor de Proyectos y Entregas Universitarias

Aplicación web desarrollada con **Spring Boot** que permite a estudiantes organizar sus proyectos y tareas académicas mediante un tablero Kanban. Incluye autenticación segura con Spring Security y vistas construidas con Thymeleaf.

---

## 🚀 Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Spring Boot | Framework principal del backend |
| Spring Security | Autenticación y autorización |
| Spring Data JPA | Acceso y gestión de base de datos |
| Thymeleaf | Motor de plantillas para las vistas |
| MySQL | Base de datos relacional |
| BCrypt | Cifrado de contraseñas |

---

## ✨ Funcionalidades

- ✅ Registro e inicio de sesión de usuarios
- ✅ Cada usuario ve únicamente sus propios proyectos
- ✅ Crear, editar y eliminar proyectos
- ✅ Crear y eliminar tareas dentro de cada proyecto
- ✅ Tablero Kanban con 4 columnas: **Pendiente**, **En Progreso**, **En Revisión**, **Finalizado**
- ✅ Mover tareas entre columnas desde el tablero
- ✅ Etiquetas con colores para clasificar tareas
- ✅ Dashboard con resumen de proyectos y tareas
- ✅ Cierre de sesión seguro

- ## 🗃️ Base de datos

El sistema usa las siguientes tablas:

- **usuario** — datos de registro y autenticación
- **proyecto** — proyectos creados por cada usuario
- **tarea** — tareas asociadas a cada proyecto
- **estado** — columnas del tablero Kanban
- **etiqueta** — etiquetas de clasificación
- **tarea_etiqueta** — relación muchos a muchos entre tareas y etiquetas

---
## 🔐 Seguridad

- Las contraseñas se almacenan cifradas con **BCrypt**
- Las rutas protegidas redirigen al login si el usuario no está autenticado
- El cierre de sesión usa petición POST con token CSRF para evitar ataques

- ## 👨‍💻 Desarrollado por

Proyecto universitario — UTS  
Curso: Programacion Web
Estudiantes: Andrea Fonseca - Diego Cobos
Corte 3


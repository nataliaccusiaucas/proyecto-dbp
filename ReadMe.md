Hola. He condensado el contenido solicitado, que originalmente era un texto extenso, en un resumen detallado que cumple con el límite de **2000 palabras** y conserva toda la información esencial sobre el proyecto **HireHub**.

-----

## 1\. Introducción: HireHub – La Solución para el Mercado Freelance Competitivo

El **mercado laboral digital** presenta una **alta saturación** y competencia, haciendo que la búsqueda de proyectos estables sea un desafío para muchos profesionales freelance. Plataformas tradicionales suelen favorecer a freelancers con mayor historial, creando una desigualdad en el acceso a oportunidades.

**HireHub** surge como una respuesta a este problema, aplicando la lógica del modelo de ofertas competitivas de plataformas como **InDriver** al ámbito de los servicios profesionales. En HireHub, un **cliente** publica una solicitud de servicio (e.g., diseño de un logotipo), y esta es enviada a todos los freelancers de la especialidad correspondiente. Los **freelancers** envían sus propuestas o cotizaciones, y el cliente elige la opción más conveniente, promoviendo la **libre competencia**, la **transparencia** y la **eficiencia** en la contratación.

-----

## 2\. Objetivos del Proyecto

El objetivo principal de HireHub es desarrollar una plataforma digital que conecte a clientes con freelancers mediante un proceso de contratación **transparente, competitivo y eficiente** a través de un sistema de solicitudes y ofertas en tiempo real.

### Objetivo General

* **Diseñar e implementar** un sistema web funcional que permita la interacción directa entre clientes y profesionales independientes, optimizando la búsqueda, selección y contratación de servicios digitales a través de un modelo de ofertas competitivas.

### Objetivos Específicos

* **Desarrollar** un módulo de registro y autenticación segura con **Spring Boot** y **JWT**.
* **Implementar** un sistema de publicaciones de solicitudes detalladas por parte de los clientes.
* **Crear** un módulo de **ofertas dinámicas** para que los freelancers propongan precios, tiempos y condiciones.
* **Diseñar** un mecanismo de **matching inteligente** basado en especialidad y reputación.
* **Desarrollar** un sistema de gestión de **comisiones y facturación** transparente.
* **Implementar** un sistema de **calificaciones y reseñas** para fomentar la confianza.
* **Asegurar** una arquitectura **escalable y modular** aplicando buenas prácticas backend (DTOs, inyección de dependencias, *testing* automatizado).

-----

## 3\. Identificación del Problema y Justificación

### Descripción del Problema

La **expansión exponencial** del mercado freelance ha provocado una **fuerte saturación y desigualdad** en la distribución de proyectos. Las plataformas existentes dificultan la competitividad de **nuevos talentos** al favorecer a usuarios con más reseñas o historial. Además, los clientes carecen de **transparencia en precios** y la posibilidad de comparar múltiples propuestas personalizadas de forma ágil. Esto crea una **brecha de acceso** que obstaculiza la contratación justa y directa.

### Justificación

HireHub responde a la necesidad de **democratizar el acceso al trabajo freelance** introduciendo un modelo donde el cliente tiene el control total de la decisión y los profesionales acceden a oportunidades en **igualdad de condiciones**, sin depender únicamente de su antigüedad o ranking. Este enfoque:

* **Empodera a los clientes**, dándoles mayor capacidad de decisión y comparación.
* **Empodera a los freelancers**, ofreciéndoles acceso equitativo a oportunidades.
* Contribuye a la **formalización** del trabajo digital gracias a la transparencia en costos y relaciones laborales más confiables.

-----

## 4\. Descripción de la Solución y Tecnologías

HireHub es una plataforma **backend** que gestiona el ciclo de contratación completo, desde el registro hasta la facturación y la evaluación del servicio.

### Funcionalidades Implementadas

1.  **Autenticación y Autorización Seguras:** Uso de **JWT (JSON Web Token)** para el acceso seguro con roles diferenciados (**cliente** y **freelancer**).
2.  **Gestión de Usuarios y Perfiles:** Módulos de *User Management* y *FreelancerProfile* para gestionar especialidad, experiencia, calificaciones, y disponibilidad.
3.  **Sistema de Ofertas y Solicitudes:** Los clientes publican **solicitudes** (*JobRequest*), y los freelancers envían **ofertas personalizadas** (*Offer*) con precio, plazo y condiciones.
4.  **Módulo de Comisiones y Facturación:** Implementación de *CommissionInvoice* para registrar comisiones, montos, fechas de emisión/vencimiento y estado de pago.
5.  **Sistema de Notificaciones por Correo Electrónico:** Integración con **Spring Mail** para notificaciones automáticas (registro, nuevas ofertas, facturas).
6.  **Gestión de Excepciones y Errores:** *Global Exception Handler* para respuestas consistentes ante fallos (400, 401, 404, 500).
7.  **Eventos y Asincronía:** Uso de **eventos asincrónicos** para la comunicación interna, mejorando la eficiencia y evitando bloqueos (ej. envío de facturas y notificaciones).

### Tecnologías Utilizadas

* **Lenguaje:** **Java 21**
* **Framework:** **Spring Boot 3.5**
* **Base de datos:** **PostgreSQL 15** (Contenerizada en Docker)
* **ORM:** **Spring Data JPA**
* **Seguridad:** **Spring Security + JWT**
* **Pruebas:** **JUnit + Testcontainers**
* **Infraestructura:** **Docker Compose**
* **Correo Electrónico:** **Spring Mail** (SMTP Gmail)
* **Arquitectura:** Arquitectura en capas (Controller, Service, Repository) y patrón **DTO** (*Data Transfer Object*).

-----

## 5\. Modelo de Entidades (Base de Datos)

El modelo de datos relacional en **PostgreSQL** garantiza integridad y escalabilidad, gestionado por **Spring Data JPA**.

| Entidad | Rol Principal | Atributos Clave | Relaciones Clave |
| :--- | :--- | :--- | :--- |
| **User** | Autenticación de todos los usuarios. | *id, name, email, password, role*. | 1:1 con *FreelancerProfile*, 1:M con *JobRequest* (si es cliente). |
| **FreelancerProfile** | Almacena detalles profesionales. | *specialization, experience, rating, portfolioUrl*. | 1:1 con *User*, 1:M con *Offer* y *Review*. |
| **JobRequest** | Solicitud de servicio publicada por el cliente. | *title, description, budget, deadline, status*. | M:1 con *User* (cliente), 1:M con *Offer*. |
| **Offer** | Propuesta del freelancer a una solicitud. | *amount, deliveryTime, message, status*. | M:1 con *JobRequest*, M:1 con *FreelancerProfile*, 1:1 con *CommissionInvoice* (al ser aceptada). |
| **CommissionInvoice** | Facturación de comisiones por contratación. | *amount, issuedAt, dueDate, status*. | 1:1 con *Offer*, M:1 con *FreelancerProfile*. |
| **Review** | Calificación y comentario del cliente al freelancer. | *rating, comment*. | M:1 con *FreelancerProfile*, M:1 con *User* (cliente). |
| **Notification**| Alertas y mensajes del sistema. | *title, message, type, isRead*. | M:1 con *User* (destinatario). |

El flujo de negocio se refleja en las relaciones: un cliente crea una **JobRequest**, los freelancers envían **Offers**, una oferta aceptada genera una **CommissionInvoice**, y al finalizar, el cliente emite una **Review**, con el sistema enviando **Notifications** en cada etapa.

-----

## 6\. Arquitectura del Sistema

La arquitectura de HireHub se basa en un modelo **multicapa** que asegura la **separación de responsabilidades**, **mantenibilidad** y **escalabilidad**.

### Capas Clave

1.  **Capa de Presentación (Controller Layer):** Gestiona las solicitudes **HTTP RESTful** y delega la lógica. Devuelve **DTOs** para seguridad. (Controladores: *UserController, JobRequestController, OfferController*, etc.).
2.  **Capa de Servicio (Service Layer):** Concentra la **lógica de negocio** (*UserService, OfferService, CommissionService*). Aplica el principio **SRP** (*Single Responsibility Principle*) y utiliza **Inyección de Dependencias**.
3.  **Capa de Acceso a Datos (Repository Layer):** Implementada con **Spring Data JPA**, abstrae la lógica SQL y simplifica las operaciones CRUD (*UserRepository, JobRequestRepository*, etc.).
4.  **Capa de Seguridad:** Módulo basado en **JWT** que incluye *JwtAuthenticationFilter, JwtService* y *SecurityConfig* para proteger los *endpoints* por rol.
5.  **Capa de Comunicación y Eventos:** Usa un sistema de **eventos asincrónicos** para desacoplar el sistema, como la generación de una factura que dispara el envío de una notificación por correo.

### Diagrama Lógico de Arquitectura (Representación Simplificada)

```
[Cliente / Frontend / Postman]
             ↓
        [Controller Layer]
             ↓
        [Service Layer]
             ↓
       [Repository Layer]
             ↓
   [Base de Datos PostgreSQL]

Módulos transversales: Seguridad (JWT), Mail, Eventos Asíncronos, Manejo Global de Excepciones.
```

-----

## 7\. Metodología de Desarrollo y Resultados

### Metodología de Desarrollo

El proyecto se desarrolló bajo un **enfoque ágil** utilizando la metodología **Scrum**. Esto permitió el trabajo en iteraciones cortas (*sprints*), facilitando la adaptación a cambios, la mejora continua del código y la priorización de funcionalidades.

* **Fases:** 1. Análisis y Diseño, 2. Configuración e Implementación (Autenticación, módulos base), 3. Integración y Expansión (Comisiones, Notificaciones, Asincronía), 4. Pruebas y Documentación.
* **Herramientas:** Git/GitHub, Spring Boot 3.5, PostgreSQL, Docker/Docker Compose, JUnit/Testcontainers, Spring Mail.

### Resultados y Pruebas

Se realizaron **pruebas funcionales** con **Postman** (validando la creación de usuarios, solicitudes, ofertas y facturas) y **pruebas unitarias/integradas** con **JUnit** y **Testcontainers** (asegurando la persistencia de datos y la correcta comunicación entre capas).

* **Pruebas Funcionales:** Se verificaron *endpoints* clave como `/api/auth/register` (devuelve JWT), `/api/jobrequests` (creación de solicitud) y `/api/offers` (envío de propuesta), con respuestas **JSON** claras y estandarizadas.
* **Pruebas Unitarias/Integración:** Se levantó una instancia temporal de **PostgreSQL** en Docker para asegurar la integridad de las relaciones y la consistencia de los datos, con el resultado de **todas las pruebas finalizadas con éxito (100% PASSED)**.
* **Despliegue en Docker:** Se confirmó la portabilidad y facilidad de mantenimiento con los contenedores `hirehub-db` y `pgAdmin`.

Los resultados demuestran que el backend de HireHub **cumple plenamente con los objetivos iniciales**, asegurando una interacción fluida y segura entre clientes y freelancers con una arquitectura robusta y escalable.

-----

## 8\. Conclusiones y Lecciones Aprendidas

### Conclusiones

El proyecto HireHub culminó con la construcción de un **backend funcional, seguro y escalable** que aplica de manera efectiva conceptos avanzados de arquitectura en capas, seguridad **JWT** y desarrollo modular con **Spring Boot 3.5**. El modelo de negocio, inspirado en la lógica de InDriver, demostró ser una **alternativa viable y transparente** para democratizar el acceso al trabajo freelance. HireHub cumple con los objetivos técnicos y funcionales, destacando por su **modularidad, seguridad y coherencia** con las buenas prácticas de desarrollo.

### Lecciones Aprendidas

* **Integración Tecnológica:** Profundización en la importancia de entornos reproducibles con **Docker** y la persistencia de datos con **PostgreSQL/JPA**.
* **Seguridad:** Entendimiento del flujo de **JWT** para la autenticación y protección de *endpoints*.
* **Arquitectura:** Reforzamiento de la **separación de responsabilidades** (Controller, Service, Repository) para mejorar la mantenibilidad.
* **Gestión de Errores:** Dominio del *Global Exception Handler* para respuestas de error profesionales y estructuradas.
* **Trabajo Iterativo:** Valoración del enfoque **Scrum** para la adaptación a cambios y el progreso constante.

El desarrollo de HireHub fue un desafío técnico que reafirmó la importancia del **aprendizaje práctico** y la capacidad de construir soluciones tecnológicas con **impacto real**.
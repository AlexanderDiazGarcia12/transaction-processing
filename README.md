# **Sistema de procesamiento de transacciones**

## **Descripción del proyecto**

Este es un proyecto que implementa un sistema de procesamiento de transacciones para una empresa de transporte masivo. El sistema recibe y procesa transacciones de los usuarios a través de una API REST, almacena las transacciones en una base de datos NoSQL MongoDB, también en un brocker de mensajería  RabbitMQ y genera un resumen diario del valor total de las transacciones y lo almacena en un documento en MongoDB.

## Arquitectura

La arquitectura seleccionada para el sistema es Hexagonal. Esto permite una mayor flexibilidad y separación de preocupaciones, facilitando el mantenimiento y la escalabilidad.

## Tecnologías usadas

* **Java 22:** Lenguaje de programación principal.
* **Spring Boot 3:** Framework de injección de dependencias para facilitar la creación de aplicaciones Java.
* **Spring WebFlux:** Para programación reactiva y la creación de servicios no bloqueantes.
* **MongoDB:** Base de datos NoSQL para el almacenamiento de las transacciones.
* **RabbitMQ:** Broker de mensajería para el registros de las transacciones.
* **Lombok:** Para reducir código boilerplate en las clases.
* **Docker:** Para la contenedorización de los servicios y la gestión de dependencias.

## Ejecución

1. Clonar repositorio desde el repositorio en GitHub.

`git clone git@github.com:AlexanderDiazGarcia12/transaction-processing.git`

2. Compilar el proyecto con Maven.

`mvn clean install`

3. Construir y levantar los contenedores con Docker compose.
   * MongoDB en el puerto 27017
   * RabbitMQ en los puertos 5672 y 15672 para la interfaz de administración.
   * La aplicación en el puerto 8080

`docker-compose up --build`

4. Acceder a la api.

La API REST estará disponible en [http://localhost:8080/api/v1/transactions](http://localhost:8080/api/v1/transactions)

5. Acceder a la interfaz de administración de RabbitMQ.

La interfaz de administración de RabbitMQ estará disponible en [http://localhost:15672](http://localhost:15672) con las credenciales:
* Usuario: *user*
* Contraseña: *password*

### Resumen diario

La tarea programada para calcular el resumen diario de las transacciones se ejecuta a medianoche todos los días, usando una expresión **cron** configurada en el archivo **application.yaml**.

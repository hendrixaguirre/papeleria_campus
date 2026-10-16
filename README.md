# Papelería Campus

## Autor

**Hendrix Devadip Aguirre Zamora**

## Descripción

Este proyecto fue desarrollado para la asignatura **Programación de Aplicaciones de Escritorio**.

La aplicación permite administrar el catálogo de productos de la Papelería Campus mediante una interfaz desarrollada con JavaFX y una base de datos PostgreSQL.

El sistema permite visualizar los productos registrados, realizar búsquedas y filtros, además de guardar, actualizar y eliminar productos.

## Tecnologías utilizadas

- Java 21
- JavaFX 21.0.6
- PostgreSQL
- PostgreSQL JDBC Driver 42.7.3
- Maven
- IntelliJ IDEA
- JavaFX Scene Builder

## Base de datos

La base de datos utilizada por el proyecto se llama:

```text
papeleria_campus
```

Dentro de la base de datos se utiliza el esquema:

```text
catalogo
```

Y la tabla:

```text
catalogo.producto
```

La tabla contiene los siguientes campos:

- `id_producto`
- `codigo`
- `nombre`
- `categoria`
- `precio`
- `stock`

## Creación y carga de la base de datos

El proyecto incluye un único script SQL:

```text
SCRIPT_PAPELERIA_CAMPUS.sql
```

Antes de ejecutar el script se debe crear la base de datos:

```sql
CREATE DATABASE papeleria_campus;
```

Luego se debe seleccionar la base de datos `papeleria_campus` y ejecutar el archivo:

```text
SCRIPT_PAPELERIA_CAMPUS.sql
```

Este script contiene:

- Creación del esquema `catalogo`.
- Creación de la tabla `producto`.
- Restricciones `PRIMARY KEY`, `NOT NULL`, `UNIQUE` y `CHECK`.
- Inserción de los 12 productos iniciales.
- Consultas utilizadas para comprobar la información almacenada.

## Configuración de la conexión

Por seguridad, las credenciales de PostgreSQL no se encuentran escritas directamente en el código fuente.

La aplicación utiliza variables de entorno mediante `System.getenv()`.

Se deben configurar las siguientes variables:

```text
DB_URL
DB_USER
DB_PASSWORD
```

Ejemplo:

```text
DB_URL=jdbc:postgresql://localhost:5432/papeleria_campus
DB_USER=USUARIO
DB_PASSWORD=CONTRASEÑA_LOCAL
```

La contraseña real debe configurarse únicamente en la computadora donde se ejecutará el proyecto y no debe almacenarse directamente en el repositorio.

## Ejecución del proyecto

1. Clonar o descargar el repositorio.
2. Abrir el proyecto en IntelliJ IDEA.
3. Verificar que Java 21 esté configurado como SDK.
4. Verificar que las dependencias Maven estén correctamente cargadas.
5. Crear la base de datos `papeleria_campus`.
6. Seleccionar la base de datos creada.
7. Ejecutar `SCRIPT_PAPELERIA_CAMPUS.sql`.
8. Configurar las variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD`.
9. Ejecutar la clase `ProductoApplication`.

## Organización del proyecto

El proyecto está organizado principalmente de la siguiente manera:

```text
ni.edu.uam.papeleria_campus
│
├── controller
│   └── ProductoController.java
│
├── DAO
│   └── ProductoDAO.java
│
├── database
│   └── ConexionBD.java
│
├── model
│   └── Producto.java
│
├── Launcher.java
└── ProductoApplication.java
```

La interfaz gráfica se encuentra en:

```text
src/main/resources/ni.edu.uam.papeleria_campus/papeleria-view.fxml
```

## Funcionamiento general

El flujo principal de la aplicación es:

```text
Interfaz JavaFX
      ↓
ProductoController
      ↓
ProductoDAO
      ↓
ConexionBD
      ↓
PostgreSQL
```

Los datos recuperados desde PostgreSQL se convierten en objetos de la clase `Producto` y posteriormente se almacenan en un `ObservableList` para mostrarlos en el `TableView`.

## Funcionalidades

La aplicación permite:

- Mostrar los productos registrados en PostgreSQL.
- Buscar productos por código o nombre.
- Filtrar productos por categoría.
- Mostrar solamente productos con existencias.
- Limpiar los filtros aplicados.
- Recargar la información desde PostgreSQL.
- Registrar nuevos productos.
- Actualizar productos existentes.
- Eliminar productos.
- Validar la información antes de enviarla a la base de datos.
- Evitar códigos duplicados.
- Confirmar la eliminación de un producto.
- Mostrar la cantidad de registros visibles en el `TableView`.

## Categorías disponibles

Los productos pueden pertenecer únicamente a las siguientes categorías:

- Papelería
- Tecnología
- Accesorios

## Validaciones

La aplicación realiza validaciones sobre los datos ingresados:

- El código es obligatorio y debe contener entre 1 y 10 caracteres.
- El código se normaliza eliminando espacios laterales y convirtiéndolo a mayúsculas.
- El nombre debe contener entre 3 y 80 caracteres.
- La categoría debe corresponder a una de las categorías permitidas.
- El precio debe ser numérico y mayor que cero.
- El precio puede tener como máximo dos decimales.
- El precio no puede superar el valor máximo permitido.
- El stock debe ser un número entero mayor o igual a cero.
- No se permiten códigos duplicados.
- Para actualizar o eliminar se debe seleccionar previamente un producto.

## Persistencia de datos

Las operaciones de guardar, actualizar y eliminar se realizan directamente sobre PostgreSQL.

Después de realizar una operación correctamente, la aplicación vuelve a consultar la base de datos para actualizar la información mostrada en el `TableView`.

## Acceso a datos

El proyecto utiliza la clase `ProductoDAO` para separar las consultas SQL del controlador de la interfaz.

El DAO contiene las operaciones de:

- Consultar productos.
- Buscar y filtrar productos.
- Guardar productos.
- Actualizar productos.
- Eliminar productos.
- Verificar códigos duplicados.

Para las consultas y operaciones se utilizan:

- `PreparedStatement`
- `ResultSet`
- `executeQuery()`
- `executeUpdate()`

## Herramientas y fuentes de apoyo

Para el desarrollo del proyecto se utilizaron:

- Material proporcionado por el docente.
- Ejemplos realizados durante las clases.
- Documentación de Java.
- Documentación de JavaFX.
- Documentación de PostgreSQL.
- Documentación de pgJDBC.
- IntelliJ IDEA.
- JavaFX Scene Builder.

## Repositorio

Repositorio de GitHub:

```text
https://github.com/hendrixaguirre/papeleria_campus.git
```

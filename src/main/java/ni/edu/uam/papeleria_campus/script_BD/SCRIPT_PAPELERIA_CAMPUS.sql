-- Creación de schema
CREATE SCHEMA catalogo;

-- Creación de tabla producto
CREATE TABLE catalogo.producto
(
	id_producto SERIAL PRIMARY KEY,
	codigo VARCHAR(10) NOT NULL UNIQUE,
	nombre VARCHAR(80) NOT NULL,
	categoria VARCHAR(20) NOT NULL,
	precio NUMERIC(10,2) NOT NULL,
	stock INTEGER NOT NULL,
	
	CONSTRAINT chk_producto_categoria
        CHECK (categoria IN ('Papelería', 'Tecnología', 'Accesorios')),
    CONSTRAINT chk_producto_precio
        CHECK (precio > 0 AND precio <= 99999999.99),
    CONSTRAINT chk_producto_stock
        CHECK (stock >= 0 AND stock <= 2147483647)
);

ALTER TABLE catalogo.producto
ADD CONSTRAINT chk_producto_codigo
CHECK (TRIM(codigo) <> '');

ALTER TABLE catalogo.producto
ADD CONSTRAINT chk_producto_nombre
CHECK (LENGTH(TRIM(nombre)) BETWEEN 3 AND 80);

-- Inserciones en tabla producto
INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('P001', 'Cuaderno universitario', 'Papelería', 65.00, 25 );

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('P002', 'Cuaderno cuadriculado', 'Papelería', 72.50, 12);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('P003', 'Lapicero azul', 'Papelería', 12.00, 80);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('P004', 'Resma de papel carta', 'Papelería', 185.00, 0);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('T001', 'Memoria USB 32 GB', 'Tecnología', 210.00, 8);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('T002', 'Mouse inalámbrico', 'Tecnología', 320.00, 6);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('T003', 'Teclado USB', 'Tecnología', 450.00, 4);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('T004', 'Cable HDMI', 'Tecnología', 180.00, 0);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('A001', 'Mochila escolar', 'Accesorios', 850.00, 5);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('A002', 'Estuche de lápices', 'Accesorios', 95.00, 18);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('A003', 'Botella reutilizable', 'Accesorios', 240.00, 0);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('A004', 'Regla metálica', 'Accesorios', 45.00, 20);

-- Comprobaciones
SELECT * FROM catalogo.producto;.

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES (NULL, 'Producto prueba', 'Papelería', 10.00, 1);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('P001', 'Producto duplicado', 'Papelería', 10.00, 1);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X001', 'Producto prueba', 'Comida', 10.00, 1);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X001', 'Producto prueba', 'Accesorios', 0, 1);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X001', 'Producto prueba', 'Accesorios', 100000000.00, 1);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X001', 'Producto prueba', 'Accesorios', 10.00, 0);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('   ', 'Producto prueba', 'Accesorios', 10.00, 1);



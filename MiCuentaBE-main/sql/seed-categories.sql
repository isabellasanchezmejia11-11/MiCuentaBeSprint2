-- Seed data for categories table
-- Insert default categories (personal = false)
-- INGRESOS (GREEN pastels)
INSERT INTO categories (name, description, type, color, personal) VALUES ('Salario', 'Salario mensual', 'INCOME', '#B5EAD7', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Inversiones', 'Ganancias de inversiones', 'INCOME', '#C1E1C1', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Regalo', 'Dinero recibido como regalo', 'INCOME', '#98FB98', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Otros ingresos', 'Otros ingresos varios', 'INCOME', '#E0FFF0', false);
-- GASTOS (WARM pastels)
INSERT INTO categories (name, description, type, color, personal) VALUES ('Alimentación', 'Gastos en comida y supermercado', 'EXPENSE', '#FFDAB9', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Transporte', 'Pasajes, combustible, transporte público', 'EXPENSE', '#FFE4B5', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Servicios', 'Luz, agua, internet, teléfono', 'EXPENSE', '#FFFACD', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Entretenimiento', 'Cine, juegos, diversión', 'EXPENSE', '#FFB6C1', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Salud', 'Medicinas, consultas, hospitales', 'EXPENSE', '#FFCCCB', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Educación', 'Colegiaturas, cursos, libros', 'EXPENSE', '#E6E6FA', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Ropa', 'Vestimenta y calzado', 'EXPENSE', '#FFD700', false);
INSERT INTO categories (name, description, type, color, personal) VALUES ('Otros gastos', 'Otros gastos varios', 'EXPENSE', '#D3D3D3', false);
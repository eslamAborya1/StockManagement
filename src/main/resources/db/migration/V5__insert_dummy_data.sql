-- V5__insert_dummy_data.sql

-- Insert 5 Suppliers
INSERT INTO suppliers (name, contact_email, phone) VALUES
('Samsung Electronics', 'b2b@samsung.com', '+1-800-726-7864'),
( 'Apple Inc.', 'enterprise@apple.com', '+1-800-692-7753'),
( 'Sony Corporation', 'sales@sony.com', '+1-800-245-7669'),
('Dell Technologies', 'vendors@dell.com', '+1-800-456-3355'),
('Logitech', 'b2b.sales@logitech.com', '+1-800-231-7717');

-- Insert 15 Products
INSERT INTO products (name, sku, price, current_stock, supplier_id) VALUES
('Samsung Galaxy S24 Ultra', 'SAM-GS24U-256', 1199.99, 50, 1),
('Samsung Galaxy Tab S9', 'SAM-TABS9-128', 799.99, 30, 1),
('Samsung Odyssey G9 Monitor', 'SAM-MON-G9', 1499.00, 5, 1),
('Apple iPhone 15 Pro Max', 'APP-IP15PM-256', 1199.00, 100, 2),
('Apple MacBook Pro 16 M3', 'APP-MBP16-M3', 2499.00, 20, 2),
('Apple iPad Air 5', 'APP-IPAD-A5', 599.00, 45, 2),
('Apple AirPods Pro 2', 'APP-AIRP2', 249.00, 150, 2),
('Sony PlayStation 5', 'SON-PS5-DISC', 499.99, 10, 3),
('Sony WH-1000XM5 Headphones', 'SON-WH1000-XM5', 398.00, 80, 3),
('Sony Bravia 65 4K TV', 'SON-TV-65B', 1299.99, 12, 3),
('Dell XPS 15 Laptop', 'DEL-XPS15-i9', 1999.00, 25, 4),
('Dell UltraSharp 27 Monitor', 'DEL-MON-U27', 549.99, 40, 4),
('Dell Alienware Aurora R15', 'DEL-AW-R15', 2199.00, 8, 4),
('Logitech MX Master 3S Mouse', 'LOG-MXM3S', 99.99, 200, 5),
('Logitech MX Keys Keyboard', 'LOG-MXK', 119.99, 150, 5);

-- Insert 24 Stock Movements
INSERT INTO stock_movements (product_id, quantity, type) VALUES 
(1, 50, 'IN'),
(2, 30, 'IN'),
(3, 10, 'IN'),
(3, 5, 'OUT'),
(4, 100, 'IN'),
(5, 25, 'IN'),
(5, 5, 'OUT'),
(6, 45, 'IN'),
(7, 200, 'IN'),
(7, 50, 'OUT'),
(8, 20, 'IN'),
(8, 10, 'OUT'),
(9, 100, 'IN'),
(9, 20, 'OUT'),
(10, 12, 'IN'),
(11, 30, 'IN'),
(11, 5, 'OUT'),
(12, 40, 'IN'),
(13, 10, 'IN'),
(13, 2, 'OUT'),
(14, 250, 'IN'),
(14, 50, 'OUT'),
(15, 180, 'IN'),
(15, 30, 'OUT');

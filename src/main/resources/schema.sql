CREATE TABLE IF NOT EXISTS TB_PRODUCT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150),
    url VARCHAR(150),
    image_url VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS TB_PRODUCT_PRICE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    crawling_date DATE,
    price VARCHAR(9),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    FOREIGN KEY (product_id) REFERENCES TB_CRAWLING_PRODUCT(product_id)
);

CREATE TABLE IF NOT EXISTS TB_CRAWLING_HISTORY (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crawling_date DATE,
    website VARCHAR(150),
    category VARCHAR(150),
    total_pages INT,
    success_pages INT,
    failure_pages INT,
    total_count INT,
    success_count INT,
    failure_count INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS TB_CRAWLING_FAILURE_RESULT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crawling_date DATE,
    website VARCHAR(150),
    category VARCHAR(150),
    page_index INT,
    product_name VARCHAR(150),
    product_url VARCHAR(150),
    failure_reason VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
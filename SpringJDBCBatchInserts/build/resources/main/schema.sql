CREATE TABLE Product(
    id SERIAL PRIMARY KEY,
    title VARCHAR(40),
    created_ts timestamp without time zone,
    price numeric
);
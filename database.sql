CREATE TYPE role_enum AS ENUM ('admin', 'user');
CREATE TYPE gender_role AS ENUM ('L', 'P');


CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role role_enum NOT NULL DEFAULT 'user',
    birth_date DATE,
    gender gender_role NULL,
    phone_number VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    event_title VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    schedule TIMESTAMP,
    description TEXT,
    admin_id INTEGER REFERENCES users(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


alter table events add column venue Varchar(255);

ALTER TABLE events alter COLUMN venue TYPE TEXT;

CREATE TABLE category_tickets (
    id SERIAL PRIMARY KEY,
    event_id INTEGER REFERENCES events(id),
    category_name VARCHAR(255) NOT NULL,
    price DECIMAL NOT NULL,
    total_ticket INTEGER,
    description text,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    total DECIMAL NOT NULL
);

CREATE TABLE visitors (
    id SERIAL PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    birth_date DATE,
    gender VARCHAR(255),
    phone_number VARCHAR(255),
    visitor_user_id INTEGER REFERENCES users(id),
    transaction_id INTEGER REFERENCES transactions(id),
    detail_ticket_id INTEGER REFERENCES category_tickets(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE detail_transactions (
    id SERIAL PRIMARY KEY,
    transaction_id INTEGER REFERENCES transactions(id),
    category_ticket_id INTEGER REFERENCES category_tickets(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


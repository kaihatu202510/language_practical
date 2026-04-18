CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	enabled BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO users (name, password, enabled)
VALUES ('admin', '$2a$10$mJcfMj4MMAFslbw.1LbsbeYOy7UiYJiuVNYI7Fa7zs7jQra13O5lu', TRUE);
ALTER TABLE users
ADD COLUMN role INT NOT NULL DEFAULT 2;
/*1 = admin, 2 = public*/
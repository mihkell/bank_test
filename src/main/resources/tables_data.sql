
CREATE TABLE account (
     id    serial PRIMARY KEY,
     name  varchar(200) UNIQUE NOT NULL
);
CREATE TABLE kl_transaction_type (
     id    serial PRIMARY KEY,
     type  varchar(20) UNIQUE
);

CREATE TABLE transaction (
     id    serial PRIMARY KEY,
     from_account  integer DEFAULT(NULL),
     to_account    integer,
     amount        integer,
     type          integer,
     FOREIGN KEY (from_account) REFERENCES Account (id),
     FOREIGN KEY (to_account) REFERENCES Account (id),
     FOREIGN KEY (type) REFERENCES kl_transaction_type (id)
);

INSERT INTO kl_transaction_type (type) VALUES ('account_to_account');
INSERT INTO kl_transaction_type (type) VALUES ('external_payment');

-- DROP TABLE transaction;
-- DROP TABLE account;
-- DROP TABLE kl_transaction_type;

CREATE TABLE tb_address
(
    UUID       UUID PRIMARY KEY,
    addressOne VARCHAR(255),
    addressTwo VARCHAR(255),
    city       VARCHAR(255),
    state      VARCHAR(255),
    postCode   VARCHAR(255),
    country    VARCHAR(255)
);

CREATE TABLE tb_user
(
    id       UUID PRIMARY KEY,
    name     VARCHAR(255),
    login    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)
);

CREATE TABLE tb_user_company
(
    id         UUID PRIMARY KEY,
    user_id    UUID,
    company_id UUID,
    is_owner   BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tb_user (id) ON DELETE CASCADE
);

CREATE TABLE tb_company
(
    UUID            UUID PRIMARY KEY,
    name            VARCHAR(255),
    address_id      UUID,
    phoneNumber     VARCHAR(20),
    email           VARCHAR(255),
    user_company_id UUID,
    FOREIGN KEY (address_id) REFERENCES tb_address (UUID) ON DELETE CASCADE,
    FOREIGN KEY (user_company_id) REFERENCES tb_user_company (id) ON DELETE CASCADE
);

CREATE TABLE tb_invoice
(
    UUID             UUID PRIMARY KEY,
    company_id       UUID,
    companyClient_id UUID,
    dataCriacao      TIMESTAMP,
    invoiceNumber    VARCHAR(255),
    comments         TEXT,
    tax              INT,
    anotherValue     INT,
    subtotal         DOUBLE PRECISION,
    taxAmount        DOUBLE PRECISION,
    total            DOUBLE PRECISION,
    FOREIGN KEY (company_id) REFERENCES tb_company (UUID) ON DELETE CASCADE,
    FOREIGN KEY (companyClient_id) REFERENCES tb_company (UUID) ON DELETE CASCADE
);

CREATE TABLE tb_invoice_item
(
    UUID        UUID PRIMARY KEY,
    description TEXT,
    unit_price  DOUBLE PRECISION,
    invoice_id  UUID,
    FOREIGN KEY (invoice_id) REFERENCES tb_invoice (UUID) ON DELETE CASCADE
);

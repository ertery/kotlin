CREATE TYPE PAYMENTSTATE AS ENUM ('New', 'InProgress', 'Done', 'Declined');

CREATE TYPE PAYMENTMETHOD AS ENUM ('cash', 'clearing', 'debt');

CREATE TYPE CHANNEL AS ENUM ('iOS', 'Telegramm');

CREATE TYPE PERSONPURPOSESTATE AS ENUM ('Accept', 'Decline', 'InviteSend', 'Initial');

CREATE TABLE purpose
(
  purpose_id     BIGSERIAL        NOT NULL
    CONSTRAINT purpose_pkey
    PRIMARY KEY,
  name           VARCHAR(255)     NOT NULL,
  creationdate   TIMESTAMP,
  finishdate     TIMESTAMP        NOT NULL,
  targetammount  DOUBLE PRECISION NOT NULL,
  currentammount DOUBLE PRECISION DEFAULT 0,
  imageurl       VARCHAR(255),
  description    VARCHAR(255),
  initiator_id   INTEGER
);

CREATE TABLE person
(
  person_id        BIGSERIAL    NOT NULL
    CONSTRAINT person_pkey
    PRIMARY KEY,
  name             VARCHAR(255) NOT NULL,
  registrationdate TIMESTAMP,
  imagepath        VARCHAR(255),
  phonenumber      VARCHAR(255),
  email            VARCHAR(255),
  purposestate     VARCHAR(255)
);

CREATE TABLE purpose_person
(
  person_id  BIGSERIAL NOT NULL,
  purpose_id BIGSERIAL NOT NULL
    CONSTRAINT payment_person_purpose_id_fkey
    REFERENCES purpose
    CONSTRAINT payment_person_purpose_id_fkey1
    REFERENCES person,
  CONSTRAINT purpose_person_person_id_purpose_id_pk
  UNIQUE (person_id, purpose_id)
);

CREATE TABLE payment
(
  id            BIGSERIAL NOT NULL
    CONSTRAINT payment_pkey
    PRIMARY KEY,
  ammount       DOUBLE PRECISION DEFAULT 0,
  paymentdate   DATE,
  purpose_id    INTEGER
    CONSTRAINT payment_purpose_id_fkey
    REFERENCES purpose,
  person_id     INTEGER
    CONSTRAINT payment_person_id_fkey
    REFERENCES person,
  paymentmethod PAYMENTMETHOD,
  state         PAYMENTSTATE,
  channel       CHANNEL
);

CREATE TABLE card
(
  id             BIGSERIAL NOT NULL
    CONSTRAINT card_pkey
    PRIMARY KEY,
  cardholdername VARCHAR(255),
  number         VARCHAR(255),
  validity       DATE,
  person_id      INTEGER
    CONSTRAINT card_person_id_fkey
    REFERENCES person
);


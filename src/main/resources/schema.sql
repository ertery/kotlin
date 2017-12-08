/*
CREATE TYPE PAYMENTSTATE AS ENUM ('New', 'InProgress', 'Done', 'Declined');

CREATE TYPE PAYMENTMETHOD AS ENUM ('Cash', 'Clearing', 'Debt');

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
  initiator_id   BIGINT
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
  facebookid       VARCHAR(255)
);

CREATE INDEX person_phonenumber_index
  ON person (phonenumber);

ALTER TABLE purpose
  ADD CONSTRAINT purpose_person_person_id_fk
FOREIGN KEY (initiator_id) REFERENCES person;

CREATE TABLE purpose_person
(
  person_id     BIGSERIAL NOT NULL
    CONSTRAINT payment_person_purpose_id_fkey1
    REFERENCES person,
  purpose_id    BIGSERIAL NOT NULL
    CONSTRAINT payment_person_purpose_id_fkey
    REFERENCES purpose,
  purpose_state VARCHAR(255),
  CONSTRAINT purpose_person_person_id_purpose_id_pk
  UNIQUE (person_id, purpose_id)
);

CREATE TABLE payment
(
  id            BIGSERIAL NOT NULL
    CONSTRAINT payment_pkey
    PRIMARY KEY,
  amount        DOUBLE PRECISION DEFAULT 0,
  paymentdate   DATE,
  purpose_id    INTEGER
    CONSTRAINT payment_purpose_id_fkey
    REFERENCES purpose,
  person_id     INTEGER
    CONSTRAINT payment_person_id_fkey
    REFERENCES person,
  paymentmethod VARCHAR(255),
  state         VARCHAR(255),
  channel       VARCHAR(255)
);

CREATE TABLE card
(
  id             BIGSERIAL NOT NULL
    CONSTRAINT card_pkey
    PRIMARY KEY,
  cardholdername VARCHAR(255),
  number         VARCHAR(255),
  validity       DATE,
  person_id      BIGINT
    CONSTRAINT card_person_id_fk
    REFERENCES person
);

CREATE TABLE device_info
(
  id        BIGSERIAL NOT NULL
    CONSTRAINT device_info_pkey
    PRIMARY KEY,
  token     VARCHAR(255),
  person_id BIGINT    NOT NULL
    CONSTRAINT device_info_person_person_id_fk
    REFERENCES person
);

CREATE UNIQUE INDEX device_info_id_uindex
  ON device_info (id);

CREATE UNIQUE INDEX device_info_token_uindex
  ON device_info (token);

*/

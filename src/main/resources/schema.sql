/*

create sequence payment_seq;
create sequence person_seq;
create sequence purpose_seq;
create sequence card_seq;

SET MODE PostgreSQL;

CREATE TYPE PaymentState AS ENUM ('New', 'InProgress', 'Done', 'Declined');
CREATE TYPE PaymentMethod AS ENUM ('cash', 'clearing', 'debt');
CREATE TYPE Channel AS ENUM ('iOS', 'Telegramm');
CREATE TYPE PersonPurposeState AS ENUM ('Accept', 'Decline', 'InviteSend', 'Initial');

CREATE TABLE purpose
(
  id IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  creationDate DATETIME,
  finishDate DATETIME NOT NULL,
  targetAmmount DOUBLE NOT NULL,
  currentAmmount DOUBLE DEFAULT 0,
  imageUrl VARCHAR(255),
  description VARCHAR(255),
  person_id INT,
);
CREATE UNIQUE INDEX purpose_id_uindex ON purpose (id);

CREATE TABLE person
(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  registrationDate DATETIME,
  imagePath VARCHAR(255),
  phoneNumber VARCHAR(255),
  email VARCHAR(255),
  purposeState ENUM('Accept', 'Decline', 'InviteSend', 'Initial'),
  purpose_id INT,
);

CREATE TABLE person
(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  registrationDate TIMESTAMP,
  imagePath VARCHAR(255),
  phoneNumber VARCHAR(255),
  email VARCHAR(255),
  purposeState ENUM('Accept', 'Decline', 'InviteSend', 'Initial'),
  purpose_id INT
);



CREATE UNIQUE INDEX person_id_uindex ON person (id);
CREATE UNIQUE INDEX person_email_uindex ON person (email);

CREATE TABLE payment_person
(
  payment_id IDENTITY,
  purpose_id IDENTITY,
  FOREIGN KEY (purpose_id) REFERENCES purpose(id),
  FOREIGN KEY (purpose_id) REFERENCES person(id)
);
ALTER TABLE payment_person ADD PRIMARY KEY (payment_id, purpose_id);

CREATE TABLE payment
(
  id IDENTITY PRIMARY KEY,
  ammount DOUBLE DEFAULT 0,
  paymentDate DATE,
  purpose_id INT,
  person_id INT,
  paymentMethod ENUM('CASH', 'CLEARING', 'DEBT'),
  state ENUM('New', 'InProgress', 'Done', 'Declined'),
  channel ENUM('iOS', 'Telegramm'),
  FOREIGN KEY (purpose_id) REFERENCES purpose(id),
  FOREIGN KEY (person_id) REFERENCES person(id),
);
CREATE UNIQUE INDEX payment_id_uindex ON payment (id);


CREATE TABLE card
(
  id IDENTITY PRIMARY KEY,
  cardHolderName VARCHAR(255),
  number VARCHAR(255),
  validity DATE,
  person_id INT,
  FOREIGN KEY (person_id) REFERENCES person(id)
);
CREATE UNIQUE INDEX card_id_uindex ON card (id);
*/



/*create type paymentstate as enum ('New', 'InProgress', 'Done', 'Declined')
;

create type paymentmethod as enum ('cash', 'clearing', 'debt')
;

create type channel as enum ('iOS', 'Telegramm')
;

create type personpurposestate as enum ('Accept', 'Decline', 'InviteSend', 'Initial')
;

create table purpose
(
  id bigserial not null
    constraint purpose_pkey
    primary key,
  name varchar(255) not null,
  creationdate timestamp,
  finishdate timestamp not null,
  targetammount double precision not null,
  currentammount double precision default 0,
  imageurl varchar(255),
  description varchar(255),
  person_id integer
)
;

create table person
(
  id bigserial not null
    constraint person_pkey
    primary key,
  name varchar(255) not null,
  registrationdate timestamp,
  imagepath varchar(255),
  phonenumber varchar(255),
  email varchar(255),
  purposestate personpurposestate,
  purpose_id integer
)
;

create table purpose_person
(
  person_id bigserial not null,
  purpose_id bigserial not null
    constraint payment_person_purpose_id_fkey
    references purpose
    constraint payment_person_purpose_id_fkey1
    references person
)
;

create table payment
(
  id bigserial not null
    constraint payment_pkey
    primary key,
  ammount double precision default 0,
  paymentdate date,
  purpose_id integer
    constraint payment_purpose_id_fkey
    references purpose,
  person_id integer
    constraint payment_person_id_fkey
    references person,
  paymentmethod paymentmethod,
  state paymentstate,
  channel channel
)
;

create table card
(
  id bigserial not null
    constraint card_pkey
    primary key,
  cardholdername varchar(255),
  number varchar(255),
  validity date,
  person_id integer
    constraint card_person_id_fkey
    references person
)
;*/



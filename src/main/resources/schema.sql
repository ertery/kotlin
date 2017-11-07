/*create type paymentstate as enum ('New', 'InProgress', 'Done', 'Declined')
;

create type paymentmethod as enum ('Cash', 'Clearing', 'Debt')
;

create type channel as enum ('iOS', 'Telegramm')
;

create type personpurposestate as enum ('Accept', 'Decline', 'InviteSend', 'Initial')
;

create table purpose
(
  purpose_id bigserial not null
    constraint purpose_pkey
    primary key,
  name varchar(255) not null,
  creationdate timestamp,
  finishdate timestamp not null,
  targetammount double precision not null,
  currentammount double precision default 0,
  imageurl varchar(255),
  description varchar(255),
)
;

create table person
(
  person_id bigserial not null
    constraint person_pkey
    primary key,
  name varchar(255) not null,
  registrationdate timestamp,
  imagepath varchar(255),
  phonenumber varchar(255),
  email varchar(255),
  purposestate personpurposestate,
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





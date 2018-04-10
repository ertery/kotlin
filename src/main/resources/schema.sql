/*
create type paymentstate as enum ('New', 'InProgress', 'Done', 'Declined')
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
  initiator_id bigint
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
  facebookid varchar(255)
)
;

alter table purpose
  add constraint purpose_person_person_id_fk
foreign key (initiator_id) references person
;

create table purpose_person
(
  person_id bigserial not null
    constraint payment_person_purpose_id_fkey1
    references person,
  purpose_id bigserial not null
    constraint payment_person_purpose_id_fkey
    references purpose,
  purpose_state varchar(255),
  constraint purpose_person_person_id_purpose_id_pk
  unique (person_id, purpose_id)
)
;

create table payment
(
  id bigserial not null
    constraint payment_pkey
    primary key,
  amount double precision default 0,
  paymentdate date,
  purpose_id integer
    constraint payment_purpose_id_fkey
    references purpose,
  person_id integer
    constraint payment_person_id_fkey
    references person,
  paymentmethod varchar(255),
  state varchar(255),
  channel varchar(255)
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
;

create table device_info
(
  id bigserial not null
    constraint device_info_pkey
    primary key,
  token varchar(255),
  person_id bigint not null
    constraint device_info_person_person_id_fk
    references person
)
;

create function truncate_tables(username character varying) returns void
language plpgsql
as $$
DECLARE
    statements CURSOR FOR
    SELECT tablename FROM pg_tables
    WHERE tableowner = username AND schemaname = 'public';
BEGIN
  FOR stmt IN statements LOOP
    EXECUTE 'TRUNCATE TABLE ' || quote_ident(stmt.tablename) || ' CASCADE;';
  END LOOP;
END;
$$
;

*/

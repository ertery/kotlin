INSERT INTO person (person_id, name, registrationdate, imagepath, phonenumber, email, facebookid) VALUES (1, 'Пепка Прыгни', '2017-11-04 22:34:38.925000', '', '88005553535', 'email@test.ru', '1');
INSERT INTO person (person_id, name, registrationdate, imagepath, phonenumber, email, facebookid) VALUES (252, 'Михаил Серегин', '2017-11-16 23:33:26.671000', 'https://graph.facebook.com/v2.10/1500150536698838/picture?type=normal&width=180&height=180', '9269854045', '_silo@mail.ru', '1500150536698838');

INSERT INTO purpose (purpose_id, name, creationdate, finishdate, targetammount, currentammount, imageurl, description, initiator_id) VALUES (350, 'Создание новой тестовой цели300', '2017-11-24 16:54:14.531000', '2020-08-29 19:30:40.000000', 700, 0, 'http://pictures.com/picture&id=1', 'Тестовая цель', 252);
INSERT INTO purpose (purpose_id, name, creationdate, finishdate, targetammount, currentammount, imageurl, description, initiator_id) VALUES (500, 'торт', '2017-12-01 14:30:23.459000', '2020-08-29 19:30:40.000000', 200, 0, 'https://firebasestorage.googleapis.com/v0/b/throw-35122.appspot.com/o/throw%2F533820582.jpg?alt=media&token=be8fdb68-1985-4bd0-bb87-80de9aaae65e', 'Торт шоколадный', 252);

INSERT INTO purpose_person (person_id, purpose_id, purpose_state) VALUES (1, 35, 'ACCEPT');
INSERT INTO purpose_person (person_id, purpose_id, purpose_state) VALUES (252, 500, 'INITIAL');
INSERT INTO purpose_person (person_id, purpose_id, purpose_state) VALUES (1, 500, 'ACCEPT');
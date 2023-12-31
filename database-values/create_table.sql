CREATE TABLE KLUB(
id LONG NOT NULL GENERATED ALWAYS AS IDENTITY,
naziv_kluba VARCHAR(50) NOT NULL,
drzava VARCHAR(50) NOT NULL,
trener_id LONG NOT NULL,
stadion_id LONG NOT NULL,
PRIMARY KEY(id),
FOREIGN KEY(trener_id) REFERENCES TRENER(id),
FOREIGN KEY(stadion_id) REFERENCES STADION(id)
);

CREATE TABLE LIGA(
id LONG NOT NULL GENERATED ALWAYS AS IDENTITY,
klub_id LONG NOT NULL,
bodovi INT NOT NULL,
odigrano INT NOT NULL,
zabijeno INT NOT NULL,
primljeno INT NOT NULL,
PRIMARY KEY(id),
FOREIGN KEY(klub_id ) REFERENCES KLUB(id)
);

CREATE TABLE IGRAC(
id LONG NOT NULL GENERATED ALWAYS AS IDENTITY,
ime VARCHAR(40) NOT NULL,
prezime VARCHAR(40) NOT NULL,
datum_rodenja DATE NOT NULL,
vrijednost INT NOT NULL,
pozicija VARCHAR(4) NOT NULL,
drzavljanstvo VARCHAR(20) NOT NULL,
klub_id LONG NOT NULL,
PRIMARY KEY(id),
FOREIGN KEY(klub_id) REFERENCES KLUB(id)
);

CREATE TABLE TRENER(
id LONG NOT NULL GENERATED ALWAYS AS IDENTITY,
ime VARCHAR(40) NOT NULL,
prezime VARCHAR(40) NOT NULL,
datum_rodenja DATE NOT NULL,
drzavljanstvo VARCHAR(20) NOT NULL,
omiljena_formacija VARCHAR(15) NOT NULL,
PRIMARY KEY(id)
);

CREATE TABLE STADION(
id LONG NOT NULL GENERATED ALWAYS AS IDENTITY,
naziv_stadiona VARCHAR(40) NOT NULL,
kapacitet INT NOT NULL,
PRIMARY KEY(id)
);

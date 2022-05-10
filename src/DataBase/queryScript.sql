-- SQLite
CREATE TABLE Teller
(TellerId int CONSTRAINT TELLER_PK PRIMARY KEY NOT NULL,
Username varchar(100) NOT NULL,
Password int NOT NULL,
FirstName varchar(100) NOT NULL,
LastName varchar(100) NOT NULL,
LastLogin DATE NOT NULL);
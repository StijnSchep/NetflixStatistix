USE master;
IF EXISTS(select * from sys.databases where name='Netflix')
DROP DATABASE Netflix
GO

IF NOT EXISTS(select * from sys.databases where name='Netflix')
CREATE DATABASE Netflix
GO

Use Netflix;

DROP TABLE IF EXISTS Episode;
DROP TABLE IF EXISTS Film;
DROP TABLE IF EXISTS Series;
DROP TABLE IF EXISTS Genre;
DROP TABLE IF EXISTS Language;
DROP TABLE IF EXISTS AgeIndication;
DROP TABLE IF EXISTS Profile_Programme;
DROP TABLE IF EXISTS Programme;
DROP TABLE IF EXISTS Profile;
DROP TABLE IF EXISTS Subscription;

CREATE TABLE Subscription (
	SubscriptionID int IDENTITY(1,1) PRIMARY KEY,
	SubscriberName nvarchar(50) NOT NULL,
	Street nvarchar(50) NOT NULL,
	HouseNumber int NOT NULL CHECK (HouseNumber >= 0),
	Additive nvarchar(50),
	Place nvarchar(50) NOT NULL,

	/* Combinatie van deze velden moet uniek zijn */
	CONSTRAINT UC_Subscription UNIQUE (Street, HouseNumber, Additive, Place)

	
)

CREATE TABLE Profile (
	ProfileID int IDENTITY(1,1) PRIMARY KEY,
	SubscriptionID int NOT NULL FOREIGN KEY REFERENCES Subscription(SubscriptionID) ON UPDATE CASCADE ON DELETE CASCADE,
	ProfileName nvarchar(50) NOT NULL,
	DateOfBirth date NOT NULL,

	CONSTRAINT UC_Profile UNIQUE (SubscriptionID, ProfileName)
)

CREATE TABLE Programme (
	ProgrammeID int IDENTITY(1,1) PRIMARY KEY,
	DurationInMinutes int NOT NULL,
	Title nvarchar(50) NOT NULL,

	CONSTRAINT UC_Programme UNIQUE (DurationInMinutes, Title)
)

CREATE TABLE Profile_Programme (
	ProfileID int NOT NULL FOREIGN KEY REFERENCES Profile(ProfileID) ON UPDATE CASCADE ON DELETE CASCADE,
	ProgrammeID int NOT NULL FOREIGN KEY REFERENCES Programme(ProgrammeID) ON UPDATE CASCADE ON DELETE CASCADE,
	/* profiel kan 0% t/m 100% van een programma bekeken hebben */
	Progress int NOT NULL CHECK (Progress >= 0 AND Progress <= 100),

	CONSTRAINT PK_Profile_Programme PRIMARY KEY (ProfileID, ProgrammeID),
	CONSTRAINT UC_PP UNIQUE (ProfileID, ProgrammeID, Progress)
)

CREATE TABLE Genre (
	Genre nvarchar(50) NOT NULL PRIMARY KEY
)

CREATE TABLE Language (
	Language nvarchar(50) NOT NULL PRIMARY KEY
)

CREATE TABLE AgeIndication (
	AgeIndication nvarchar(50) NOT NULL PRIMARY KEY
)

CREATE TABLE Film (
	ProgrammeID int NOT NULL PRIMARY KEY FOREIGN KEY REFERENCES Programme(ProgrammeID) ON UPDATE CASCADE ON DELETE CASCADE,
	Genre nvarchar(50) NOT NULL FOREIGN KEY REFERENCES Genre(Genre) ON UPDATE CASCADE ON DELETE CASCADE,
	Language nvarchar(50) NOT NULL FOREIGN KEY REFERENCES Language(Language) ON UPDATE CASCADE ON DELETE CASCADE,
	AgeIndication nvarchar(50) NOT NULL FOREIGN KEY REFERENCES AgeIndication(AgeIndication) ON UPDATE CASCADE ON DELETE CASCADE,
	ReleaseYear int NOT NULL DEFAULT YEAR(GETDATE())
)

CREATE TABLE Series (
	SeriesID int IDENTITY(1,1) PRIMARY KEY,
	Title nvarchar(50) NOT NULL,
	Genre nvarchar(50) NOT NULL FOREIGN KEY REFERENCES Genre(Genre) ON UPDATE CASCADE ON DELETE CASCADE,
	AgeIndication nvarchar(50) NOT NULL FOREIGN KEY REFERENCES AgeIndication(AgeIndication) ON UPDATE CASCADE ON DELETE CASCADE,

	CONSTRAINT UC_Series UNIQUE (Title, Genre, AgeIndication)
)

CREATE TABLE Episode (
	ProgrammeID int NOT NULL PRIMARY KEY FOREIGN KEY REFERENCES Programme(ProgrammeID) ON UPDATE CASCADE ON DELETE CASCADE,
	SeriesID int NOT NULL FOREIGN KEY REFERENCES Series(SeriesID) ON UPDATE CASCADE ON DELETE CASCADE,

	/* Episodes zijn aangegeven in de vorm van S01E01 of voor extreme gevallen S200E300*/
	EpisodeNumber nvarchar(8) NOT NULL CHECK (EpisodeNumber LIKE 'S%E%'),

	CONSTRAINT UC_Episode UNIQUE (ProgrammeID, SeriesID),
	CONSTRAINT UC2_Episode UNIQUE (SeriesID, EpisodeNumber)
)

/* Vul de tabellen */

/* Vul tabel met talen */
INSERT INTO Language(Language) VALUES ('Nederlands');
INSERT INTO Language(Language) VALUES ('Vlaams');
INSERT INTO Language(Language) VALUES ('Frans');
INSERT INTO Language(Language) VALUES ('Duits');
INSERT INTO Language(Language) VALUES ('Engels');
INSERT INTO Language(Language) VALUES ('Engels-Amerikaans');
INSERT INTO Language(Language) VALUES ('Russisch');

/* Vul tabel met categorieën van kijkwijzer */
INSERT INTO AgeIndication(AgeIndication) VALUES ('alle leeftijden');
INSERT INTO AgeIndication(AgeIndication) VALUES ('6 jaar en ouder');
INSERT INTO AgeIndication(AgeIndication) VALUES ('9 jaar en ouder');
INSERT INTO AgeIndication(AgeIndication) VALUES ('12 jaar en ouder');
INSERT INTO AgeIndication(AgeIndication) VALUES ('16 jaar en ouder');
INSERT INTO AgeIndication(AgeIndication) VALUES ('18 jaar en ouder');

/* Vul tabel met veelvoorkomende filmgenres */
INSERT INTO Genre(Genre) VALUES('Actie');
INSERT INTO Genre(Genre) VALUES('Avontuur');
INSERT INTO Genre(Genre) VALUES('Biografie');
INSERT INTO Genre(Genre) VALUES('Detective');
INSERT INTO Genre(Genre) VALUES('Drama');
INSERT INTO Genre(Genre) VALUES('Familie');
INSERT INTO Genre(Genre) VALUES('Fantasie');
INSERT INTO Genre(Genre) VALUES('Gangster');
INSERT INTO Genre(Genre) VALUES('Historisch drama');
INSERT INTO Genre(Genre) VALUES('Horror');
INSERT INTO Genre(Genre) VALUES('Komedie');
INSERT INTO Genre(Genre) VALUES('Kostuumdrama');
INSERT INTO Genre(Genre) VALUES('Melodrama');
INSERT INTO Genre(Genre) VALUES('Misdaad');
INSERT INTO Genre(Genre) VALUES('Musical');
INSERT INTO Genre(Genre) VALUES('Muziek');
INSERT INTO Genre(Genre) VALUES('Mysterie');
INSERT INTO Genre(Genre) VALUES('Oorlog');
INSERT INTO Genre(Genre) VALUES('Porno');
INSERT INTO Genre(Genre) VALUES('Psychologische thriller');
INSERT INTO Genre(Genre) VALUES('Rampen');
INSERT INTO Genre(Genre) VALUES('Road');
INSERT INTO Genre(Genre) VALUES('Romatisch');
INSERT INTO Genre(Genre) VALUES('Romantische komedie');
INSERT INTO Genre(Genre) VALUES('Sciencefiction');
INSERT INTO Genre(Genre) VALUES('Sport');
INSERT INTO Genre(Genre) VALUES('Spanning');
INSERT INTO Genre(Genre) VALUES('Sandalen');
INSERT INTO Genre(Genre) VALUES('Thriller');
INSERT INTO Genre(Genre) VALUES('Western');

/* Maak abonnementen aan */
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Fam. Schep', 'Berkenlaan', 130, 'Zundert');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Fam. van Beek', 'Ostaaijensebaan', 1, 'Achtmaal');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Jan Janssen', 'Lovensdijkstraat', 25, 'Breda');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Diny van Leeuwen', 'Grote Markt', 74, 'Dordrecht');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Tessa Koppens', 'Voorstraat', 168, 'Dordrecht');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Herman Koster', 'Boogjes', 90, 'Den Bosch');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Rufus de Groene', 'Dommelweg', 34, 'Breda');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Radagast the Brown', 'Gelderlozepad', 13, 'Breda');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Gandalf the Grey', 'The Shire', 8, 'Middle Earth');
INSERT INTO Subscription(SubscriberName, Street, HouseNumber, Place) 
	VALUES ('Niels de Hoon', 'Charlotte van Praag-pad', 99, 'Rijen');


/* Maak profielen aan */
INSERT INTO Profile(SubscriptionID, ProfileName, DateOfBirth)
	VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Berkenlaan'
		AND HouseNumber = 130),
		'Stijn Schep', '2000-03-24'); /* JJJJ-MM-DD */
INSERT INTO Profile(SubscriptionID, ProfileName, DateOfBirth)
	VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Berkenlaan'
		AND HouseNumber = 130),
		'Sabine Schep', '1999-11-12'); /* JJJJ-MM-DD */
INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Voorstraat'
		AND HouseNumber = 168),
		'Tessa Koppens', '1998-06-02'); /* JJJJ-MM-DD */
INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Ostaaijensebaan'
		AND HouseNumber = 1),
		'Willy van Beek', '1965-12-12'); /* JJJJ-MM-DD */
INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Lovensdijkstraat'
		AND HouseNumber = 25),
		'Jan Janssen', '1977-01-14'); /* JJJJ-MM-DD */
INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Grote Markt'
		AND HouseNumber = 74),
		'Diny van Leeuwen', '1964-01-05'); /* JJJJ-MM-DD */
INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Boogjes'
		AND HouseNumber = 90),
		'Herman Koster', '1988-03-22'); /* JJJJ-MM-DD */
INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Dommelweg'
		AND HouseNumber = 34),
		'Rufus de Groene', '1976-11-30'); /* JJJJ-MM-DD */
		INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Gelderlozepad'
		AND HouseNumber = 13),
		'Radagast the Brown', '1961-03-13'); /* JJJJ-MM-DD */
INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'The Shire'
		AND HouseNumber = 8),
		'Gandalf the Grey', '1989-04-05'); /* JJJJ-MM-DD */
INSERT INTO Profile (SubscriptionID, ProfileName, DateOfBirth)
VALUES (
		(SELECT SubscriptionID
		FROM Subscription
		WHERE Street = 'Charlotte van Praag-pad'
		AND HouseNumber = 99),
		'Niels de Hoon', '2000-05-12'); /* JJJJ-MM-DD */


/* Maak films aan (eerst programma, koppel vervolgens film aan dat programma */
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (94, 'The life of Brian');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication, ReleaseYear)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 94
	AND Title = 'The life of Brian'),
	'Komedie', 'Engels', '12 jaar en ouder', 1979);
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (98, 'The colour of magic');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication, ReleaseYear)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 98
	AND Title = 'The colour of magic'),
	'Fantasie', 'Engels', '12 jaar en ouder', 2008);
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (153, 'Prisoners');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 153
	AND Title = 'Prisoners'),
	'Thriller', 'Engels', '16 jaar en ouder');
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (96, 'Legally Blond');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 96
	AND Title = 'Legally Blond'),
	'Komedie', 'Engels', '9 jaar en ouder');
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (85, 'The muppets');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 85
	AND Title = 'The muppets'),
	'Komedie', 'Engels', 'alle leeftijden');
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (83, 'Hercules');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 83
	AND Title = 'Hercules'),
	'Familie', 'Engels', 'alle leeftijden');
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (241, 'A christmas carol');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 241
	AND Title = 'A christmas carol'),
	'Familie', 'Engels', 'alle leeftijden');
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (111, 'Kill Bill');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 111
	AND Title = 'Kill Bill'),
	'Actie', 'Engels', '16 jaar en ouder');
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (108, 'From dusk till dawn');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 108
	AND Title = 'From dusk till dawn'),
	'Actie', 'Engels', '16 jaar en ouder');
INSERT INTO Programme(DurationInMinutes, Title) 
	VALUES (137, 'Dune');
INSERT INTO Film(ProgrammeID, Genre, Language, AgeIndication)
	VALUES (
	(SELECT ProgrammeID
	FROM Programme
	WHERE DurationInMinutes = 137
	AND Title = 'Dune'),
	'Sciencefiction', 'Engels', '12 jaar en ouder');



/* Maak series aan */
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('Breaking Bad', 'Spanning', '16 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('Sherlock', 'Detective', '12 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('Fargo', 'Spanning', '16 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('Riverdale', 'Spanning', '12 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('Castlevania', 'Horror', '12 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('The Walking Dead', 'Actie', '12 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('Gossip girl', 'Drama', '12 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('The Handmaiden s tale', 'Drama', '16 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('Game of Thrones', 'Fantasie', '16 jaar en ouder');
INSERT INTO Series(Title, Genre, AgeIndication) VALUES ('Downton Abbey', 'Historisch drama', '12 jaar en ouder');



/* Maak aflevering aan (eerst programma, dan koppel je de aflevering aan dat programma en aan een serie) */
INSERT INTO Programme(DurationInMinutes, Title) VALUES (88, 'A Study in Pink');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 88
			AND Title = 'A Study in Pink'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Sherlock'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (88, 'The Blind Banker');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 88
			AND Title = 'The Blind Banker'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Sherlock'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (88, 'The Great Game');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 88
			AND Title = 'The Great Game'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Sherlock'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (58, 'Pilot');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 58
			AND Title = 'Pilot'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Breaking Bad'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (48, 'Cat is in the bag...');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 48
			AND Title = 'Cat is in the bag...'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Breaking Bad'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (48, '...And the Bag is in the River');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 48
			AND Title = '...And the Bag is in the River'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Breaking Bad'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (48, 'Cancer Man');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 48
			AND Title = 'Cancer Man'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Breaking Bad'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (48, 'Grey Matter');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 48
			AND Title = 'Grey Matter'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Breaking Bad'
		),
		'S01E05'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (48, 'Crazy Handful of Nothin');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 48
			AND Title = 'Crazy Handful of Nothin'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Breaking Bad'
		),
		'S01E06'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (48, 'A No-Rough-Stuff-Type Deal');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 48
			AND Title = 'A No-Rough-Stuff-Type Deal'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Breaking Bad'
		),
		'S01E07'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (68, 'The Crocodiles Dilemma');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 68
			AND Title = 'The Crocodiles Dilemma'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (51, 'The Rooster Prince');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 51
			AND Title = 'The Rooster Prince'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (48, 'A Muddy Road');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 48
			AND Title = 'A Muddy Road'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (50, 'Eating the Blame');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 50
			AND Title = 'Eating the Blame'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (50, 'The Six Ungraspables');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 50
			AND Title = 'The Six Ungraspables'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E05'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (53, 'Buridan s Ass');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 53
			AND Title = 'Buridan s Ass'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E06'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (48, 'Who Shaves the Barber?');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 48
			AND Title = 'Who Shaves the Barber?'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E07'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (53, 'The Heap');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 53
			AND Title = 'The Heap'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E08'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (53, 'A Fox, a Rabbit, and a Cabbage');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 53
			AND Title = 'A Fox, a Rabbit, and a Cabbage'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E09'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (62, 'Morton s Fork');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 62
			AND Title = 'Morton s Fork'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Fargo'
		),
		'S01E10'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter One: The River s Edge');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter One: The River s Edge'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Two: A Touch of Evil');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Two: A Touch of Evil'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Three: Body Double');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Three: Body Double'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Four: The Last Picture Show');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Four: The Last Picture Show'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Five: Heart of Darkness');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Five: Heart of Darkness'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E05'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Six: Faster, Pussycats! Kill! Kill!');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Six: Faster, Pussycats! Kill! Kill!'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E06'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Seven: In a Lonely Place');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Seven: In a Lonely Place'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E07'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Eight: The Outsiders');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Eight: The Outsiders'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E08'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Nine: La Grande Illusion');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Nine: La Grande Illusion'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E09'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Ten: The Lost Weekend');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Ten: The Lost Weekend'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E10'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Eleven: To Riverdale and Back Again');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Eleven: To Riverdale and Back Again'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E11'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Twelve: Anatomy of a Murder');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Twelve: Anatomy of a Murder'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E12'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (44, 'Chapter Thirteen: The Sweet Hereafter');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 44
			AND Title = 'Chapter Thirteen: The Sweet Hereafter'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Riverdale'
		),
		'S01E13'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (24, 'Witchbottle');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 24
			AND Title = 'Witchbottle'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Castlevania'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (24, 'Necropolis');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 24
			AND Title = 'Necropolis'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Castlevania'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (24, 'Labyrinth');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 24
			AND Title = 'Labyrinth'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Castlevania'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (24, 'Monument');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 24
			AND Title = 'Monument'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Castlevania'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (43, 'Days Gone Bye ');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 43
			AND Title = 'Days Gone Bye '
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Walking Dead'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (43, 'Guts');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 43
			AND Title = 'Guts'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Walking Dead'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (43, 'Tell It to the Frogs ');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 43
			AND Title = 'Tell It to the Frogs '
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Walking Dead'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (43, 'Vatos ');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 43
			AND Title = 'Vatos '
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Walking Dead'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (43, 'Wildfire ');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 43
			AND Title = 'Wildfire '
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Walking Dead'
		),
		'S01E05'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (43, 'TS-19 ');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 43
			AND Title = 'TS-19 '
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Walking Dead'
		),
		'S01E06'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Pilot');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Pilot'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'The Wild Brunch');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'The Wild Brunch'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Poison Ivy');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Poison Ivy'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Bad News Blair');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Bad News Blair'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Dare Devil');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Dare Devil'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E05'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'The Handmaiden s Tale');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'The Handmaiden s Tale'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E06'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Victor/Victrola');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Victor/Victrola'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E07'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Seventeen Candles');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Seventeen Candles'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E08'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Blair Waldorf Must Pie!');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Blair Waldorf Must Pie!'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E09'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Hi, Society');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Hi, Society'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E10'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Roman Holiday');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Roman Holiday'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E11'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'School Lies');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'School Lies'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E12'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'The Thin Line Between Chuck and Nate');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'The Thin Line Between Chuck and Nate'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E13'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'The Blair Bitch Project');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'The Blair Bitch Project'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E14'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Desperately Seeking Serena');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Desperately Seeking Serena'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E15'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'All About My Brother');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'All About My Brother'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E16'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Woman on the Verge');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Woman on the Verge'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E17'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (42, 'Much I Do About Nothing');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 42
			AND Title = 'Much I Do About Nothing'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Gossip girl'
		),
		'S01E18'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (57, 'Offred');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 57
			AND Title = 'Offred'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (47, 'Birth Day');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 47
			AND Title = 'Birth Day'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (53, 'Late');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 53
			AND Title = 'Late'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (53, 'Nolite Te Bastardes Carborundorum');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 53
			AND Title = 'Nolite Te Bastardes Carborundorum'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (53, 'Faithful');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 53
			AND Title = 'Faithful'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E05'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'A Woman s Place');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'A Woman s Place'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E06'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'The Other Side');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'The Other Side'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E07'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (50, 'Jezebels');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 50
			AND Title = 'Jezebels'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E08'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (50, 'The Bridge');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 50
			AND Title = 'The Bridge'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E09'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (60, 'Night');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 60
			AND Title = 'Night'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'The Handmaiden s tale'
		),
		'S01E10'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (105, 'Winter Is Coming');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 105
			AND Title = 'Winter Is Coming'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'The Kingsroad');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'The Kingsroad'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'Lord Snow');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'Lord Snow'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'Cripples, Bastards, and Broken Things');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'Cripples, Bastards, and Broken Things'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'The Wolf and the Lion');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'The Wolf and the Lion'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E05'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'A Golden Crown');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'A Golden Crown'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E06'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'You Win or You Die');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'You Win or You Die'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E07'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'The Pointy End');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'The Pointy End'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E08'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'Baelor');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'Baelor'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E09'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (55, 'Fire and Blood');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 55
			AND Title = 'Fire and Blood'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Game of Thrones'
		),
		'S01E10'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (66, 'Episode One');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 66
			AND Title = 'Episode One'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Downton Abbey'
		),
		'S01E01'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (47, 'Episode Two');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 47
			AND Title = 'Episode Two'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Downton Abbey'
		),
		'S01E02'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (47, 'Episode Three');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 47
			AND Title = 'Episode Three'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Downton Abbey'
		),
		'S01E03'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (47, 'Episode Four');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 47
			AND Title = 'Episode Four'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Downton Abbey'
		),
		'S01E04'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (47, 'Episode Five');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 47
			AND Title = 'Episode Five'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Downton Abbey'
		),
		'S01E05'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (47, 'Episode Six');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 47
			AND Title = 'Episode Six'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Downton Abbey'
		),
		'S01E06'
	);
INSERT INTO Programme(DurationInMinutes, Title) VALUES (47, 'Episode Seven');
INSERT INTO Episode(ProgrammeID, SeriesID, EpisodeNumber) VALUES
	(
		(	/* Haal het ID op van het programma dat net is aangemaakt */
			SELECT ProgrammeID
			FROM Programme
			WHERE DurationInMinutes = 47
			AND Title = 'Episode Seven'
		),
		(	/* Haal ID op van de serie waar je een aflevering voor maakt */
			SELECT SeriesID
			FROM Series
			WHERE Title = 'Downton Abbey'
		),
		'S01E07'
	);


/* Koppel profielen aan bekeken series */
INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 1 AND ProfileName = 'Stijn Schep'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'A Study in Pink' AND DurationInMinutes = 88
	),
	100
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 1 AND ProfileName = 'Stijn Schep'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'The Blind Banker' AND DurationInMinutes = 88
	),
	70
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 1 AND ProfileName = 'Stijn Schep'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'The Great Game' AND DurationInMinutes = 88
	),
	93
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 1 AND ProfileName = 'Stijn Schep'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'Prisoners' AND DurationInMinutes = 153
	),
	100
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 1 AND ProfileName = 'Stijn Schep'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'A christmas carol' AND DurationInMinutes = 241
	),
	100
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 5 AND ProfileName = 'Tessa Koppens'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'A christmas carol' AND DurationInMinutes = 241
	),
	100
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 1 AND ProfileName = 'Stijn Schep'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'Kill Bill' AND DurationInMinutes = 111
	),
	23
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 2  AND ProfileName = 'Willy van Beek'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'Chapter Seven: In a Lonely Place' AND DurationInMinutes = 44
	),
	67
)
INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 3  AND ProfileName = 'Jan Janssen'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'Episode Six' AND DurationInMinutes = 47
	),
	77
	)
INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 4  AND ProfileName = 'Diny van Leeuwen'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'Fire and Blood' AND DurationInMinutes = 55
	),
	47
	)
INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 5  AND ProfileName = 'Tessa Koppens'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'The colour of magic' AND DurationInMinutes = 98
	),
	92
	)
INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 6  AND ProfileName = 'Herman Koster'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = '...And the Bag is in the River' AND DurationInMinutes = 48
	),
	100
	)
INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 7  AND ProfileName = 'Rufus de Groene'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'Necropolis' AND DurationInMinutes = 24
	),
	50
	)
INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 8  AND ProfileName = 'Radagast the Brown'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'The Other Side' AND DurationInMinutes = 55
	),
	99
	)
INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 9  AND ProfileName = 'Gandalf the Grey'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'The muppets' AND DurationInMinutes = 85
	),
	100
	)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 10  AND ProfileName = 'Niels de Hoon'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'Legally Blond' AND DurationInMinutes = 96
	),
	100
	)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 5 AND ProfileName = 'Tessa Koppens'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'A Study in Pink' AND DurationInMinutes = 88
	),
	77
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 2 AND ProfileName = 'Willy van Beek'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'A Study in Pink' AND DurationInMinutes = 88
	),
	59
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 3 AND ProfileName = 'Jan Janssen'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'A Study in Pink' AND DurationInMinutes = 88
	),
	99
)

INSERT INTO Profile_Programme(ProfileID, ProgrammeID, Progress) VALUES
(
	(
		SELECT ProfileID
		FROM Profile
		WHERE SubscriptionID = 4 AND ProfileName = 'Diny van Leeuwen'
	),
	(
		SELECT ProgrammeID
		FROM Programme
		WHERE Title = 'A Study in Pink' AND DurationInMinutes = 88
	),
	100
)
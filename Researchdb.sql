-- Team 6 Database4lyfe
-- Gordon Upshaw, Rosis Sharma, Tim Endersby, Fengyi Chen



DROP DATABASE IF EXISTS ReasearchDb; 

CREATE DATABASE ReasearchDb;
USE ReasearchDb;

CREATE TABLE userType(
userTypeID NVarchar(20) PRIMARY KEY NOT NULL,
userType nvarchar(10) NOT NULL
);

CREATE TABLE Users(

username NVarchar(20) PRIMARY KEY NOT NULL,
hashedPassword  NVarchar(128) NOT NULL, -- hashed
userTypeID NVarchar (10), -- Faculty/Student/ Public
FOREIGN KEY (userTypeID) REFERENCES userType (userTypeID)
);

CREATE TABLE Persons(
username NVarchar(20) NOT NULL,
fullName nvarchar(50) NOT NULL,
phoneNumber nvarchar (12),
email nvarchar(30),
website nvarchar(40),
userTypeID NVarchar(20) NOT NULL,
interests NVarchar (100) NOT NULL,
FOREIGN KEY (username) REFERENCES Users (username),
FOREIGN KEY (userTypeID) REFERENCES userType (userTypeID)
);

CREATE TABLE Student(
username NVarchar(20) NOT NULL,
major nvarchar(30) NOT NULL,
minor nvarchar(30),
FOREIGN KEY (username) REFERENCES Users (username)
);

CREATE TABLE Professor(
username NVarchar(20) NOT NULL,
college nvarchar(30) NOT NULL,
FOREIGN KEY (username) REFERENCES Users (username)
);

CREATE TABLE Projects(
projectID INT PRIMARY KEY NOT NULL,
projectname nvarchar (30) NOT NULL,
description nvarchar (200) NOT NULL

);

CREATE TABLE ProjectInvolvment(
username NVarchar(20) NOT NULL,
projectID INT NOT NULL,
FOREIGN KEY (username) REFERENCES Users (username),
FOREIGN KEY (projectID) REFERENCES Projects (projectID)
);

CREATE TABLE Keywords(
keyID int PRIMARY KEY NOT NULL,
keyword nvarchar(20) NOT NULL
);

INSERT INTO userType VALUES
('A','Admin'),
('P','Professor'),
('S','Student');

INSERT INTO Users VALUES
('DbAdmin','4b360c50c9a2e7430695d7442aadeb65c0d02379e6534199002bb4718e8be509df694ab92e2d4ac041671f64b6879b8832d1535b0856c88a5145ab1ccdd7fcfb','A'),
('ProfZD','2000d9f1bd60c56a9970bb0ed61e5245a7e276c1d3cf82599a3fe5c0723e63903c136409192998f6cbc6f4e1feb5f1a02154988bb63bc0359741cbbcf113e81b','P'),
('ProfAS','2000d9f1bd60c56a9970bb0ed61e5245a7e276c1d3cf82599a3fe5c0723e63903c136409192998f6cbc6f4e1feb5f1a02154988bb63bc0359741cbbcf113e81b','P'),
('ProfRT','2000d9f1bd60c56a9970bb0ed61e5245a7e276c1d3cf82599a3fe5c0723e63903c136409192998f6cbc6f4e1feb5f1a02154988bb63bc0359741cbbcf113e81b','P'),
('ProfLM','2000d9f1bd60c56a9970bb0ed61e5245a7e276c1d3cf82599a3fe5c0723e63903c136409192998f6cbc6f4e1feb5f1a02154988bb63bc0359741cbbcf113e81b','P'),
('ProfTS','2000d9f1bd60c56a9970bb0ed61e5245a7e276c1d3cf82599a3fe5c0723e63903c136409192998f6cbc6f4e1feb5f1a02154988bb63bc0359741cbbcf113e81b','P'),
('FS867','507b22ce209a8527569d688c9106ef85d8d140092e409dd194f9b897acd9056ef096ef7afa72ea276843e8224a5aa43ee8deffb69cd7267d99b82878db240f4a','S'),
('LA674','507b22ce209a8527569d688c9106ef85d8d140092e409dd194f9b897acd9056ef096ef7afa72ea276843e8224a5aa43ee8deffb69cd7267d99b82878db240f4a','S'),
('OP117','507b22ce209a8527569d688c9106ef85d8d140092e409dd194f9b897acd9056ef096ef7afa72ea276843e8224a5aa43ee8deffb69cd7267d99b82878db240f4a','S'),
('JC890','507b22ce209a8527569d688c9106ef85d8d140092e409dd194f9b897acd9056ef096ef7afa72ea276843e8224a5aa43ee8deffb69cd7267d99b82878db240f4a','S'),
('NB343','507b22ce209a8527569d688c9106ef85d8d140092e409dd194f9b897acd9056ef096ef7afa72ea276843e8224a5aa43ee8deffb69cd7267d99b82878db240f4a','S'),
('GT716','507b22ce209a8527569d688c9106ef85d8d140092e409dd194f9b897acd9056ef096ef7afa72ea276843e8224a5aa43ee8deffb69cd7267d99b82878db240f4a','S');

INSERT INTO Persons VALUES
('DbAdmin', 'Bill Admin', '915-634-5455','DBadmin@rit.edu','BillAdmin.com', 'A', 'Admining the DB'),
('ProfZD', 'Zach Davids', '123-456-1234','ZD117@rit.edu','ZachD.com', 'P', 'Networking, Database'),
('ProfAS', 'Arnold Swartz', '343-117-1025','AS365@rit.edu','Swartz.com', 'P', 'Networking, Database'),
('ProfRT', 'Robert Tiger', '890-456-5634','RT245@rit.edu','Tigerden.com', 'P', 'Web Development'),
('ProfLM', 'Lindsey Marks', '589-321-4321','LM900@rit.edu','LMarks.com', 'P', 'Database'),
('ProfTS', 'Tanya Simpson', '434-898-2345','TSS555@rit.edu','TanyaS.com', 'P', 'Networking'),
('FS867', 'Frank Simons', '656-987-1085','FSimons343@rit.edu','FrankIST.com', 'S', 'Web Dev, Networking, Database'),
('LA674', 'Lewis Armada', '718-567-2121','LArmada117@rit.edu','LewisIST.com', 'S',  'Networking, Database'),
('OP117', 'Owen Peterson', '655-555-1085','OPeterson101@rit.edu','OwenIST.com', 'S', 'Web Dev'),
('JC890', 'Jessica Coach', '609-987-1575','JCoach808@rit.edu','JesseieIST.com', 'S', 'Web Dev'),
('NB343', 'Nicole Banner', '454-688-7821','NB676@rit.edu','BannerIST.com', 'S', 'Web Dev, Networking, Database'),
('GT716', 'Gary Timothy', '213-888-7888','GTimthy1093@rit.edu','GarysMods.com', 'S', 'Game Design');

INSERT INTO Student VALUES
('FS867', 'Information Technologies', 'Web Dev'),
('LA674', 'Information Technologies', 'Networking'),
('OP117', 'Information Technologies', 'Database'),
('JC890', 'Information Technologies', 'Database'),
('NB343', 'Information Technologies', 'Networking'),
('GT716', 'Game Design and Development', 'Networking');

INSERT INTO Professor VALUES
('ProfZD','Golisano'),
('ProfAS','Golisano'),
('ProfRT','Gosnell'),
('ProfLM','Golisano'),
('ProfTS','Gleason');

INSERT INTO Projects VALUES
(1, 'Update RIT Website', 'Updating the Old RIT Website'),
(2, 'New Routers', 'Replace all existing routers own campus'),
(3, 'Improved Internet', 'Improve internet by setting up Fiber optics'),
(4, 'New Student Database', 'Replace old and slow Student Database'),
(5, 'New Faculty Database', 'Upgrade Faculty database'),
(6, 'Technology Upgrade (Gosnell)', 'Upgrade the tech in Gosnell'),
(7, 'New Computers Dorm Side', 'Replace aging computers dorm side with upgraded models'),
(8, 'Charging Station', 'Add back charging station to Golisano'),
(9, 'New Projecters on Campus', 'Fix and Upgrade Projecters on campus'),
(10, 'New IST building', 'Begin laying out new IST facility'),
(11, 'Improve outdoor wifi', 'Fix outdoor wifi issue');
INSERT INTO ProjectInvolvment VALUES
('ProfZD', 1),
('FS867', 2),
('ProfAS', 3),
('ProfRT', 4),
('ProfLM', 5),
('ProfTS', 6),
('OP117', 7),
('LA674', 8),
('JC890', 9),
('NB343', 10),
('GT716', 11);
create database DUOI_HINH_BAT_CHU;

use DUOI_HINH_BAT_CHU;

create table users (
	id int auto_increment,
    username varchar(255),
    password varchar(255),
    numberOfGame int,
    numberOfWin int,
    numberOfDraw int,
    isOnline bool,
    isPlaying bool,
    avatar varchar(255),
    
    primary key (id)
);

create table questions (
	id int auto_increment,
    answer varchar(255),
    imgPath varchar(255),
	hint varchar(255),
    
    primary key (id)
);

create table histories (
	id int auto_increment,
    timeStart datetime,
    timeEnd datetime,
    isWin bool,
    opponentId int,
    ownerId int,
    
    primary key (id),
    FOREIGN KEY (opponentId) REFERENCES users(id),
    FOREIGN KEY (ownerId) REFERENCES users(id)
);	

CREATE TABLE rooms (
    roomId VARCHAR(255) PRIMARY KEY,
    playerCount INT DEFAULT 0,
    isFull BOOLEAN DEFAULT FALSE
);

INSERT INTO users (username, password, numberOfGame, numberOfWin, numberOfDraw, isOnline, isPlaying, avatar) VALUES
('hoangminhkhuong', '123456', 10, 5, 2, true, false, 'avatar1.jpg'),
('nguyenhaidang', '123456', 15, 8, 3, false, false, 'avatar2.jpg'),
('phamvananh', '123456', 20, 10, 5, true, true, 'avatar3.jpg'),
('letrongdat', '123456', 17, 7, 4, true, true, 'avatar4.jpg');

select * from users;




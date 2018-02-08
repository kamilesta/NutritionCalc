create database if not exists foodCalculator;

use foodCalculator;

drop table if exists vegetables;

CREATE TABLE `vegetables` (
	`id` int(11) not null auto_increment,
    `name` varchar(64) default null,
    `country` varchar(64) default null,
    `grams` int(11) default null,
	`calories` int(11) default null,
    `protein` int(11) default null,
    `carb` int(11) default null,
    `mealId` int(11) default null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `vegetables` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`, `mealId`) VALUES (1, 'Broccoli', 'Italy', 150,	45, 5, 8, 3);
INSERT INTO `vegetables` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`, `mealId`) VALUES (2, 'Corn', 'USA', 100,	92, 3, 21, 1);
INSERT INTO `vegetables` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`, `mealId`) VALUES (3, 'Carrot', 'Czech', 150,	45, 1, 10, 2);
INSERT INTO `vegetables` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`, `mealId`) VALUES (4, 'Onion', 'Poland', 210,	80, 2, 18, 4);

drop table if exists meats;
drop table if exists meat;

CREATE TABLE `meats` (
	`id` int(11) not null auto_increment,
    `name` varchar(64) default null,
    `country` varchar(64) default null,
    `grams` int(11) default null,
	`calories` int(11) default null,
    `protein` int(11) default null,
    `carb` int(11) default null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `meats` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`) VALUES (1, 'Bacon', 'USA', 16, 95, 4, 1);
INSERT INTO `meats` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`) VALUES (2, 'Lamb', 'France', 115, 480, 24, 0);
INSERT INTO `meats` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`) VALUES (3, 'Beef', 'Poland', 85, 245, 23, 0);
INSERT INTO `meats` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`) VALUES (4, 'Pork', 'Poland', 100, 260, 16, 0);


drop table if exists additions;

CREATE TABLE `additions` (
	`id` int(11) not null auto_increment,
    `name` varchar(64) default null,
    `country` varchar(64) default null,
    `grams` int(11) default null,
	`calories` int(11) default null,
    `protein` int(11) default null,
    `carb` int(11) default null,
    `mealId` int(11) default null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `additions` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`, `mealId`) VALUES (1, 'Rice', 'Japan', 208,	748, 15, 154, 3);
INSERT INTO `additions` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`, `mealId`) VALUES (2, 'Macaroni', 'Italy', 140,	155, 5, 32, 4);
INSERT INTO `additions` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`, `mealId`) VALUES (3, 'Wheat', 'Poland', 68,	245, 17, 34, 2);
INSERT INTO `additions` (`id`, `name`, `country`, `grams`, `calories`, `protein`, `carb`, `mealId`) VALUES (4, 'Patatoes', 'Poland', 100,	100, 2, 22, 1);

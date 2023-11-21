-- CREATE SCHEMA `meeting-scheduler`;

-- use `meeting-scheduler`;

-- CREATE TABLE `employee` (
--   `employee_id` int NOT NULL AUTO_INCREMENT,
--   `name` varchar(128) DEFAULT NULL,
--   `email` varchar(45) DEFAULT NULL,
--   PRIMARY KEY (`employee_id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- CREATE TABLE `meeting_room` (
--   `meeting_room_id` int NOT NULL AUTO_INCREMENT,
--   `room_name` varchar(128) DEFAULT NULL,
--   `capacity` int NULL,
--   PRIMARY KEY (`meeting_room_id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=latin1;

-- CREATE TABLE `team` (
--   `team_id` int NOT NULL AUTO_INCREMENT,
--   `team_name` varchar(128) DEFAULT NULL,
--   PRIMARY KEY (`team_id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=1500 DEFAULT CHARSET=latin1;

-- CREATE TABLE `employee_team` (
--   `employee_id` int NOT NULL,
--   `team_id` int NOT NULL,
--   
--   PRIMARY KEY (`employee_id`,`team_id`),
--   
--   CONSTRAINT `FK_EMPLOYEE` FOREIGN KEY (`employee_id`) 
--   REFERENCES `employee` (`employee_id`) 
--   ON DELETE NO ACTION ON UPDATE NO ACTION,
--   
--   CONSTRAINT `FK_TEAM` FOREIGN KEY (`team_id`) 
--   REFERENCES `team` (`team_id`) 
--   ON DELETE NO ACTION ON UPDATE NO ACTION
-- ) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- CREATE TABLE `meeting` (
--   `meeting_id` int NOT NULL AUTO_INCREMENT,
--   `meeting_name` varchar(128) DEFAULT NULL,
--   `status` varchar(128) DEFAULT 'UPCOMING',
--   `count` int NOT NULL,
--   `meeting_room_id` int NOT NULL,
--   KEY `FK_MEETING_ROOM` (`meeting_room_id`),
--   CONSTRAINT `FK_MEETING_ROOM`
--   FOREIGN KEY (`meeting_room_id`)
--   REFERENCES `meeting_room` (meeting_room_id),
--   PRIMARY KEY (`meeting_id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=15000 DEFAULT CHARSET=latin1;

CREATE TABLE `meeting_detail` (
  `meeting_detail_id` int NOT NULL AUTO_INCREMENT,
  `meeting_id` int NOT NULL,
  `meeting_date` DATE NOT NULL,
  `meeting_start_time` TIME NOT NULL,
  `meeting_end_time` TIME NOT NULL,
  `employee_id` int NOT NULL,
  PRIMARY KEY (`meeting_detail_id`),
  CONSTRAINT `FK_MEETING` FOREIGN KEY (`meeting_id`)
  REFERENCES `meeting` (meeting_id),
  CONSTRAINT `FK_EMPLOYEE1` FOREIGN KEY (`employee_id`)
  REFERENCES `employee` (employee_id)
) ENGINE=InnoDB AUTO_INCREMENT=20000 DEFAULT CHARSET=latin1;
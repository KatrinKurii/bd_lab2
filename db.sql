CREATE TABLE `buyer` (
  `bid` int(11) NOT NULL AUTO_INCREMENT,
  `bname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`bid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
CREATE TABLE `maker` (
  `mid` int(11) NOT NULL AUTO_INCREMENT,
  `mname` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
CREATE TABLE `seller` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `sname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
CREATE TABLE `ware` (
  `wid` int(11) NOT NULL AUTO_INCREMENT,
  `wname` varchar(45) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `description` text,
  `availability` binary(1) DEFAULT NULL,
  PRIMARY KEY (`wid`),
  FULLTEXT KEY `index2` (`description`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
CREATE TABLE `facts` (
  `mid` int(11) NOT NULL,
  `wid` int(11) NOT NULL,
  `sid` int(11) NOT NULL,
  `bid` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `fid` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`fid`,`bid`),
  KEY `index5` (`mid`),
  KEY `index3` (`wid`),
  KEY `index4` (`sid`),
  KEY `index1` (`bid`),
  CONSTRAINT `fk_facts_1` FOREIGN KEY (`bid`) REFERENCES `buyer` (`bid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_facts_2` FOREIGN KEY (`mid`) REFERENCES `maker` (`mid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_facts_3` FOREIGN KEY (`sid`) REFERENCES `seller` (`sid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_facts_4` FOREIGN KEY (`wid`) REFERENCES `ware` (`wid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

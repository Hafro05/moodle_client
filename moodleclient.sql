-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema moodleclient
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema moodleclient
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `moodleclient` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `moodleclient` ;

-- -----------------------------------------------------
-- Table `moodleclient`.`assignment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moodleclient`.`assignment` (
  `idassignment` INT NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `startdate` INT NULL DEFAULT NULL,
  `duedate` INT NULL DEFAULT NULL,
  `submitted` TINYINT NULL DEFAULT '0',
  `link` VARCHAR(512) NULL DEFAULT NULL,
  `submission_path` VARCHAR(512) NULL DEFAULT NULL,
  `filename` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`idassignment`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moodleclient`.`course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moodleclient`.`course` (
  `idcourse` INT NOT NULL,
  `name` VARCHAR(75) NULL DEFAULT NULL,
  `shortname` VARCHAR(75) NULL DEFAULT NULL,
  `lastaccess` INT NULL DEFAULT NULL,
  `summary` VARCHAR(500) NULL DEFAULT NULL,
  PRIMARY KEY (`idcourse`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moodleclient`.`course_assignment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moodleclient`.`course_assignment` (
  `idcourse` INT NOT NULL,
  `idassignment` INT NOT NULL,
  PRIMARY KEY (`idcourse`, `idassignment`),
  INDEX `fk_id_assign_idx` (`idassignment` ASC) VISIBLE,
  CONSTRAINT `fk_id_assign`
    FOREIGN KEY (`idassignment`)
    REFERENCES `moodleclient`.`assignment` (`idassignment`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_id_course`
    FOREIGN KEY (`idcourse`)
    REFERENCES `moodleclient`.`course` (`idcourse`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moodleclient`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moodleclient`.`user` (
  `userid` INT NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `fullname` VARCHAR(255) NULL DEFAULT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`userid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moodleclient`.`user_course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moodleclient`.`user_course` (
  `userid` INT NOT NULL,
  `idcourse` INT NOT NULL,
  PRIMARY KEY (`idcourse`, `userid`),
  INDEX `fk_userid_idx` (`userid` ASC) VISIBLE,
  CONSTRAINT `fk_idcourse`
    FOREIGN KEY (`idcourse`)
    REFERENCES `moodleclient`.`course` (`idcourse`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_userid`
    FOREIGN KEY (`userid`)
    REFERENCES `moodleclient`.`user` (`userid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

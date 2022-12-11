-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema e_duo
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema e_duo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `e_duo` DEFAULT CHARACTER SET utf8mb4 ;
USE `e_duo` ;

-- -----------------------------------------------------
-- Table `e_duo`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`user` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`user` (
  `user_id` VARCHAR(20) NOT NULL,
  `password` VARCHAR(300) NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  `phone` VARCHAR(11) NOT NULL,
  `activated` TINYINT(4) NULL DEFAULT 1,
  `role` ENUM('ROLE_TEACHER', 'ROLE_ASSISTANT', 'ROLE_STUDENT') NOT NULL,
  `register_date` TIMESTAMP NULL DEFAULT now(),
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`assistant`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`assistant` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`assistant` (
  `user_id` VARCHAR(20) NOT NULL,
  INDEX `fk_assistant_user1_idx` (`user_id` ASC) ,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_assistant_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`teacher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`teacher` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`teacher` (
  `user_id` VARCHAR(20) NOT NULL,
  `subject` VARCHAR(45) NULL DEFAULT NULL,
  `image_src` VARCHAR(100) NULL,
  INDEX `fk_teacher_user1_idx` (`user_id` ASC) ,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_teacher_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`course`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`course` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`course` (
  `course_id` VARCHAR(60) NOT NULL,
  `user_id` VARCHAR(20) NOT NULL,
  `course_name` VARCHAR(50) NOT NULL,
  `academy_name` VARCHAR(45) NULL DEFAULT NULL,
  `academy_address` VARCHAR(70) NULL DEFAULT NULL,
  `start_date` TIMESTAMP NOT NULL,
  `end_date` TIMESTAMP NULL,
  PRIMARY KEY (`course_id`),
  INDEX `fk_course_teacher1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_course_teacher1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`teacher` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`lecture`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`lecture` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`lecture` (
  `lecture_id` VARCHAR(65) NOT NULL,
  `cousre_id` VARCHAR(60) NOT NULL,
  `lecture_name` VARCHAR(50) NOT NULL,
  `lecture_date` TIMESTAMP NULL DEFAULT NULL,
  `start_time` TIME NULL,
  `end_time` TIME NULL,
  `description` VARCHAR(150) NULL,
  `lecture_order` VARCHAR(6) NULL,
  PRIMARY KEY (`lecture_id`),
  INDEX `fk_lecture_subject1_idx` (`cousre_id` ASC) ,
  CONSTRAINT `fk_lecture_subject1`
    FOREIGN KEY (`cousre_id`)
    REFERENCES `e_duo`.`course` (`course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`student`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`student` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`student` (
  `user_id` VARCHAR(20) NOT NULL,
  `birth_date` TIMESTAMP NULL DEFAULT NULL,
  `school_name` VARCHAR(45) NOT NULL,
  `grade` INT NOT NULL,
  `parent` VARCHAR(1) NOT NULL,
  `parent_phone` VARCHAR(11) NOT NULL,
  INDEX `fk_student_user1_idx` (`user_id` ASC) ,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_student_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`attendance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`attendance` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`attendance` (
  `attendance_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(20) NOT NULL,
  `lecture_lec_id` VARCHAR(65) NOT NULL,
  `assignment` INT NULL DEFAULT 0,
  `done_date` TIMESTAMP NULL,
  `check_in` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`attendance_id`),
  INDEX `fk_attendance_lecture1_idx` (`lecture_lec_id` ASC) ,
  INDEX `fk_attendance_student1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_attendance_lecture1`
    FOREIGN KEY (`lecture_lec_id`)
    REFERENCES `e_duo`.`lecture` (`lecture_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_attendance_student1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`student` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`exam`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`exam` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`exam` (
  `exam_id` INT NOT NULL AUTO_INCREMENT,
  `type` ENUM('REVIEW', 'MOCK', 'MIDTERM', 'FINAL') NULL,
  `exam_date` TIMESTAMP NULL,
  `exam_name` VARCHAR(60) NULL,
  `course_id` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`exam_id`),
  INDEX `fk_exam_course1_idx` (`course_id` ASC) ,
  CONSTRAINT `fk_exam_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `e_duo`.`course` (`course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `e_duo`.`score`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`score` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`score` (
  `score_idx` INT(11) NOT NULL,
  `score` INT(11) NULL DEFAULT NULL,
  `user_id` VARCHAR(20) NOT NULL,
  `exam_id` INT NOT NULL,
  PRIMARY KEY (`score_idx`),
  INDEX `fk_score_student1_idx` (`user_id` ASC) ,
  INDEX `fk_score_exam1_idx` (`exam_id` ASC) ,
  CONSTRAINT `fk_score_student1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`student` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_score_exam1`
    FOREIGN KEY (`exam_id`)
    REFERENCES `e_duo`.`exam` (`exam_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`token` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`token` (
  `user_id` VARCHAR(20) NOT NULL,
  `access_token` VARCHAR(300) NULL DEFAULT NULL,
  `refresh_token` VARCHAR(300) NULL DEFAULT NULL,
  INDEX `fk_token_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_token_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


-- -----------------------------------------------------
-- Table `e_duo`.`management`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`management` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`management` (
  `management_id` INT NOT NULL AUTO_INCREMENT,
  `course_id` VARCHAR(60) NOT NULL,
  `user_id` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`management_id`),
  INDEX `fk_management_course1_idx` (`course_id` ASC) ,
  INDEX `fk_management_assistant1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_management_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `e_duo`.`course` (`course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_management_assistant1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`assistant` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `e_duo`.`sugang`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`sugang` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`sugang` (
  `sugang_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(20) NOT NULL,
  `course_id` VARCHAR(60) NOT NULL,
  `start_date` TIMESTAMP NOT NULL,
  `end_date` TIMESTAMP NULL,
  `state` ENUM('ACCEPTED', 'WAIT', 'QUIT') NULL DEFAULT 'WAIT',
  PRIMARY KEY (`sugang_id`),
  INDEX `fk_sugang_course1_idx` (`course_id` ASC) ,
  INDEX `fk_sugang_student1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_sugang_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `e_duo`.`course` (`course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sugang_student1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`student` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `e_duo`.`notice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`notice` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`notice` (
  `notice_id` INT NOT NULL AUTO_INCREMENT,
  `course_id` VARCHAR(60) NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `content` BLOB NOT NULL,
  `regist_date` TIMESTAMP NULL DEFAULT now(),
  `notice_type` ENUM('ASSIGNMENT', 'COURSE') NULL,
  PRIMARY KEY (`notice_id`),
  INDEX `fk_notice_course1_idx` (`course_id` ASC) ,
  CONSTRAINT `fk_notice_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `e_duo`.`course` (`course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `e_duo`.`notice_file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`notice_file` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`notice_file` (
  `notice_file_id` INT NOT NULL AUTO_INCREMENT,
  `notice_notice_id` INT NOT NULL,
  `save_folder` VARCHAR(150) NULL,
  `save_file` VARCHAR(150) NULL,
  `origin_file` VARCHAR(150) NULL,
  PRIMARY KEY (`notice_file_id`),
  INDEX `fk_notice_file_notice1_idx` (`notice_notice_id` ASC) ,
  CONSTRAINT `fk_notice_file_notice1`
    FOREIGN KEY (`notice_notice_id`)
    REFERENCES `e_duo`.`notice` (`notice_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `e_duo`.`video`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`video` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`video` (
  `video_id` INT NOT NULL,
  `lecture_id` VARCHAR(65) NOT NULL,
  PRIMARY KEY (`video_id`),
  INDEX `fk_video_lecture1_idx` (`lecture_id` ASC) ,
  CONSTRAINT `fk_video_lecture1`
    FOREIGN KEY (`lecture_id`)
    REFERENCES `e_duo`.`lecture` (`lecture_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `e_duo`.`message_template`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`message_template` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`message_template` (
  `message_template_id` INT NOT NULL AUTO_INCREMENT,
  `score_template` BLOB NULL,
  `attendance_template` BLOB NULL,
  `assignment_template` BLOB NULL,
  `custom_template_1` BLOB NULL,
  `custom_template_2` BLOB NULL,
  `custom_template_3` BLOB NULL,
  `user_id` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`message_template_id`),
  INDEX `fk_message_template_teacher1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_message_template_teacher1`
    FOREIGN KEY (`user_id`)
    REFERENCES `e_duo`.`teacher` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `e_duo`.`schedule`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`schedule` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`schedule` (
  `schedule_id` INT NOT NULL AUTO_INCREMENT,
  `course_id` VARCHAR(60) NOT NULL,
  `day` INT NULL,
  `start_time` TIME NULL,
  `end_time` TIME NULL,
  PRIMARY KEY (`schedule_id`),
  INDEX `fk_schedule_course1_idx` (`course_id` ASC) ,
  CONSTRAINT `fk_schedule_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `e_duo`.`course` (`course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `e_duo`.`hire`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `e_duo`.`hire` ;

CREATE TABLE IF NOT EXISTS `e_duo`.`hire` (
  `teacher_user_id` VARCHAR(20) NOT NULL,
  `assistant_user_id` VARCHAR(20) NOT NULL,
  `state` ENUM('ACCEPTED', 'WAIT') NULL DEFAULT 'WAIT',
  INDEX `fk_hire_teacher1_idx` (`teacher_user_id` ASC) ,
  INDEX `fk_hire_assistant1_idx` (`assistant_user_id` ASC) ,
  CONSTRAINT `fk_hire_teacher1`
    FOREIGN KEY (`teacher_user_id`)
    REFERENCES `e_duo`.`teacher` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_hire_assistant1`
    FOREIGN KEY (`assistant_user_id`)
    REFERENCES `e_duo`.`assistant` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

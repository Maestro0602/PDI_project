-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: login_system
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` varchar(10) NOT NULL,
  `course_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('AI001','Introduction to Artificial Intelligence'),('AI002','Machine Learning'),('AI003','Data Science Fundamentals'),('AI004','Deep Learning'),('AU101','Control Systems'),('AU102','Industrial Automation'),('AU103','PLC and SCADA Systems'),('AU104','Robotics Fundamentals'),('CS001','Computer and Network Security'),('CS002','Ethical Hacking Fundamentals'),('CS003','Cyber Security Management'),('CS004','Cryptography Basics'),('EE101','Electrical Circuits'),('EE102','Electrical Machines'),('EE103','Power Systems'),('EE104','Power Electronics'),('EL101','Analog Electronics'),('EL102','Digital Electronics'),('EL103','Microprocessors and Microcontrollers Systems'),('EL104','Embedded Systems'),('IE101','Operations Research'),('IE102','Quality Control and Assurance'),('IE103','Supply Chain Management'),('IE104','Work Study and Ergonomics'),('ME101','Engineering Mechanics'),('ME102','Thermodynamics'),('ME103','Machine Design'),('ME104','Fluid Mechanics'),('MF101','Manufacturing Processes'),('MF102','Computer-Aided Manufacturing'),('MF103','Production Planning and Control'),('MF104','Industrial Equipment'),('SE001','Programming Fundamentals'),('SE002','Object-Oriented Programming'),('SE003','Programming Fundamentals'),('SE004','Software Engineering Principles');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-22 21:17:14

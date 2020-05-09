-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 09, 2018 at 08:40 PM
-- Server version: 10.1.28-MariaDB
-- PHP Version: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `demo`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `bno` int(11) DEFAULT NULL,
  `acc_type` enum('Current','Saving') DEFAULT NULL,
  `total_amount` float DEFAULT NULL,
  `cno` int(11) DEFAULT NULL,
  `date_entered` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ano` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `branch`
--

CREATE TABLE `branch` (
  `Bname` varchar(50) DEFAULT NULL,
  `Bno` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `zip` char(5) DEFAULT NULL,
  `cnic` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `sex` enum('Male','Female') DEFAULT NULL,
  `status` enum('Employed','Un-Employed') DEFAULT NULL,
  `marital_status` enum('Married','Un-Married') DEFAULT NULL,
  `date_entered` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cno` int(11) NOT NULL,
  `passwd` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `customer_copy`
--

CREATE TABLE `customer_copy` (
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `zip` char(5) DEFAULT NULL,
  `cnic` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `sex` enum('Male','Female') DEFAULT NULL,
  `status` enum('Employed','Un-Employed') DEFAULT NULL,
  `marital_status` enum('Married','Un-Married') DEFAULT NULL,
  `date_entered` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cno` int(11) NOT NULL,
  `passwd` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `customer_copy`
--
DELIMITER $$
CREATE TRIGGER `Notify` AFTER INSERT ON `customer_copy` FOR EACH ROW INSERT INTO notification values 
(concat(concat(concat(New.fname," "),new.lname)," wants to be added"),New.cno,null)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `loan`
--

CREATE TABLE `loan` (
  `cno` int(11) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `duration` varchar(30) DEFAULT NULL,
  `status` enum('Pending','Payed') DEFAULT NULL,
  `paid` float DEFAULT NULL,
  `remaining` float DEFAULT NULL,
  `date_entered` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_return` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lno` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `message` varchar(50) DEFAULT NULL,
  `customer_id` int(11) NOT NULL,
  `notification_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `notification`
--
DELIMITER $$
CREATE TRIGGER `addCustomer` AFTER DELETE ON `notification` FOR EACH ROW BEGIN
insert into customer (select * from customer_copy where cno = old.customer_id);
delete from customer_copy where cno = old.customer_id;
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `cno` int(11) DEFAULT NULL,
  `acc_no` int(11) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `_type` enum('Deposit','WithDraw','Transfer') DEFAULT NULL,
  `_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `transfer`
--

CREATE TABLE `transfer` (
  `acc_no_recepient` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`ano`);

--
-- Indexes for table `branch`
--
ALTER TABLE `branch`
  ADD PRIMARY KEY (`Bno`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`cno`);

--
-- Indexes for table `customer_copy`
--
ALTER TABLE `customer_copy`
  ADD PRIMARY KEY (`cno`);

--
-- Indexes for table `loan`
--
ALTER TABLE `loan`
  ADD PRIMARY KEY (`lno`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notification_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transfer`
--
ALTER TABLE `transfer`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `ano` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `branch`
--
ALTER TABLE `branch`
  MODIFY `Bno` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `cno` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `customer_copy`
--
ALTER TABLE `customer_copy`
  MODIFY `cno` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `loan`
--
ALTER TABLE `loan`
  MODIFY `lno` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

# Bank-Account-Management-System
 Academic Project for Programming Pattern Course


# Description
- The Bank Account Management System (BAMS) is a limited bank transaction monitoring software. This software will be use by the bank tellers to add new clients, manage accounts, create transactions and return client reports. Transactions will be considered either between 2 accounts (move money from one account to the other) or cash transactions: deposit into an account or extract from an account. The teller will be able to create balance report sheets for a given client. Negative balances are not allowed.

# Learning Objectives
- To build a monitoring system that can monitor and manage all library operations efficiently.
- To design a database to store the information about books and students.
- To develop a database client using Java and Java Database Connectivity API (JDBC).
- To apply some CRUD operations in a Java application using JDBC.
- To enter and preserve details of the various library items and keep a track on their returns.
- To develop international application by applying I18N and I10N concepts and related Java classes.
- To apply some of the design patterns seen in class and MVC architecture.
- To use Data structures and Stream processing (using Lambda expressions).

# Functional Requirements
1. Security
   - Only bank tellers will be able to manage account data
   - Bank Tellers will be identified by user and password
   - Teller data is static and will be stored in the database.
2. Bank Activity
   - Create a new client
   - Modify existing client information
   - Manage and create client accounts
   - Create transactions
   - Take care of the data consistency
3. Design Requirements
   - For audit purposes accounts can't be deleted. An active indicator will be used to denote active accounts.
   - Deactivated accounts need to have 0 balance. Teller will not be able to deactivate an account with positive
balance.
   - Account balance can't be negative.
   - Transaction can't be deleted. In case a transaction needs to be removed an inverse operation will be
created.
   - When calculating the account balance you have to consider both input and the output from that account.

# Design Requirements
- A clear and precise interface should be designed for input and output. It could be a simple GUI or Console I/O.
- Include all the supported class Libraries
- Must make use of at least two design patterns such as Factory Method pattern, Singleton, Strategy, ...
- The project should be designed using MVC architecture
- The application should be designed to support two languages, French and English. Make use of I18N Java classes, ResourceBundle and Locale classes. Feel free to add another language of your choice if you want to.
# MavBids

An Online mobile auctioning system for UTA

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You would need to install the following list of software in your system

```
Java 7
Maven
Eclipse IDE (Mars+)
Android Studio
Git
Tortoise Git (Optional)
Tomcat Server 7
MySQL
```

### Installing

* Step 1 - Checkout the project

Checkout the directory into your system. **App/MavBids** is the Android project. **Server/MavBids** is the Java Web project

* Step 2 - Setting up the Server code in Eclipse

	- Open your Eclipse IDE
	- Select the Workspace as **Server**
	- File -> Import -> Existing project into workspace -> Select Root directory as **Server/MavBids**. Press Ok.
	- Create a Tomcat 7 server in the Eclipe's servers tab
	- Add the project into the server

* Step 3 - Setting up the Android app code in Android Studio

	- Open your Android Studio
	- File ->Open project. Select **App/MavBids**
	- Install all plugins / dependencies which might show up in the IDE / gradle console
	
* Step 4 - Setting up the MySQL Database

	- Setup MySQL Server in your system, along with adding MySQL/bin directory in your System Environment variables
	- Start the server
	- In command line, navigate to **Scripts/** and execute the following
		```
		mysql -u <root_user> -p <root_password> < MavBids.sql
		```
		
## Deployment

* Step 1 - Maven Build of the Server

Navigate to **Server/MavBids** and run the following in the command line

```
mvn clean install
```

This will generate the WAR file in the  **Server/MavBids/target** folder, which can be deployed in Tomcat server

* Step 2 - Run the Android App

## Built With

* [Android SDK](https://developer.android.com/studio/index.html) - Android SDK
* [Spring](https://spring.io/) - Java Web Framework
* [Hibernate](http://hibernate.org/) - Java ORM Framework
* [Maven](https://maven.apache.org/) - Dependency Management
* [Gradle](https://gradle.org/) - Android Build tool

## Authors

* **Bhushan Yavagal** - [bhushanyavagal](https://github.com/bhushanyavagal) 
* **Deepak Thipeswamy** - [deepakthipeswamy](https://github.com/deepakthipeswamy)
* **Rajaraman Govindasamy** - [rajaramangovindasamy](https://github.com/rajaramangovindasamy)
* **Sai Kumar Manakan** - [MSKOnline](https://github.com/mskonline)
* **Vanisha Crasta** - [vanishacrasta](https://github.com/vanishacrasta)
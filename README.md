# EconomyAPI [![Build Status](https://travis-ci.org/EconomyS/EconomyAPI.svg?branch=master)](https://travis-ci.org/EconomyS/EconomyAPI)
Core of economy system for Nukkit

## Commands
 - /mymoney
 - /seemoney
 - /givemoney
 - /takemoney
 - /topmoney
 - /setmoney

## Permissions
- economyapi
	- economyapi.command
		- economyapi.command.mymoney
		- economyapi.command.givemoney `OP`
		- economyapi.command.takemoney `OP`
		- economyapi.command.setmoney `OP`
		- economyapi.command.topmoney

## For developers

Developers can access to EconomyAPI's API by using:
```java
EconomyAPI.getInstance().myMoney(player);
EconomyAPI.getInstance().reduceMoney(player, amount);
EconomyAPI.getInstance().addMoney(player, amount);
```

Maven repository
```xml
<repository>
	<id>onebone</id>
	<url>http://jenkins.onebone.me/plugin/repository/everything/</url>
</repository>

<dependency>
	<groupId>me.onebone</groupId>
	<artifactId>economyapi</artifactId>
	<version>1.0.0</version>
	<scope>provided</scope>
</dependency>
```

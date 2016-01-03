# EconomyAPI
Core of economy system for Nukkit

## Commands
 - /mymoney
 - /seemoney
 - /givemoney
 - /takemoney
 - /topmoney
 
## Permissions
 - economyapi.*
   - economyapi.command.*
     - economyapi.command.mymoney
     - economyapi.command.givemoney `OP`
     - economyapi.command.takemoney `OP`
     - economyapi.command.topmoney

## For developers

Developers can access to EconomyAPI's API by using:
```java
EconomyAPI.getInstance().myMoney(player);
EconomyAPI.getInstance().reduceMoney(player, amount);
EconomyAPI.getInstance().addMoney(player, amount);
```
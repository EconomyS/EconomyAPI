package me.onebone.economyapi.event.money;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class ReduceMoneyEvent extends Event implements Cancellable{
	public static HandlerList handlerList = new HandlerList();
	
	private String player;
	private double amount;
	
	public ReduceMoneyEvent(String player, double amount){
		this.player = player;
		this.amount = amount;
	}
	
	public String getPlayer(){
		return this.player;
	}
	
	public double getAmount(){
		return this.amount;
	}
	
	public void setAmount(double amount){
		this.amount = amount;
	}
	
	public static HandlerList getHandlers(){
		return handlerList;
	}
}

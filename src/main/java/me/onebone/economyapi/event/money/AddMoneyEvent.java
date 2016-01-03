package me.onebone.economyapi.event.money;

/*
 * EconomyAPI: Core of economy system for Nukkit
 * Copyright (C) 2016  onebone <jyc00410@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class AddMoneyEvent extends Event implements Cancellable{
	public static HandlerList handlerList = new HandlerList();
	
	private String player;
	private double amount;
	
	public AddMoneyEvent(String player, double amount){
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

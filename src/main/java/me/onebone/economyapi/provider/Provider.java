package me.onebone.economyapi.provider;

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

import cn.nukkit.Player;

import java.util.LinkedHashMap;

public interface Provider {
	public void init(String path);
	
	public void open();
	public void save();
	public void close();
	
	public boolean accountExists(String player);
	public boolean accountExists(Player player);
	
	public boolean createAccount(Player player, double defaultMoney);
	public boolean createAccount(String player, double defaultMoney);
	
	public boolean setMoney(String player, double amount);
	public boolean setMoney(Player player, double amount);
	
	public boolean addMoney(String player, double amount);
	public boolean addMoney(Player player, double amount);
	
	public boolean reduceMoney(String player, double amount);
	public boolean reduceMoney(Player player, double amount);
	
	public double getMoney(String player);
	public double getMoney(Player player);
	
	public LinkedHashMap<String, Double> getAll();
	
	public String getName();
}

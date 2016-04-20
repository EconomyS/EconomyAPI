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

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

public class YamlProvider implements Provider{
	private Config file = null;
	private LinkedHashMap<String, Double> data = null;
	
	@SuppressWarnings({ "unchecked", "serial" })
	public void init(String path){
		file = new Config(new File(path, "Money.yml"), Config.YAML, new LinkedHashMap<String, Object>(){
			{
				put("version" , 2);
				put("money", new LinkedHashMap<String, Double>());
			}
		});
		
		LinkedHashMap<Object, Object> temp = (LinkedHashMap<Object, Object>) file.get("money");
		
		data = new LinkedHashMap<>();
		temp.forEach((key, money) -> {
			String username = key.toString();
			
			if(money instanceof Integer){
				data.put(username, ((Integer) money).doubleValue());
			}else if(money instanceof Double){
				data.put(username, (Double) money);
			}else if(money instanceof String){
				data.put(username, Double.parseDouble(money.toString()));
			}
		});
	}
	
	public void open(){
		
	}

	public void save(){
		file.set("money", data);
		file.save();
	}

	public void close(){
		this.save();
		
		file = null;
		data = null;
	}
	
	public boolean accountExists(Player player){
		return this.accountExists(player.getName());
	}
	
	public boolean accountExists(String player){
		player = player.toLowerCase();
		
		return data.containsKey(player);
	}
	
	public boolean createAccount(Player player, double defaultMoney){
		return this.createAccount(player.getName(), defaultMoney);
	}
	
	public boolean createAccount(String player, double defaultMoney){
		player = player.toLowerCase();
		
		if(!this.accountExists(player)){
			data.put(player, defaultMoney);
		}
		return false;
	}

	public boolean setMoney(String player, double amount){
		player = player.toLowerCase();
		
		if(data.containsKey(player)){
			data.put(player, data.get(player) + amount);
			return true;
		}
		return false;
	}

	public boolean setMoney(Player player, double amount){
		return this.setMoney(player.getName(), amount);
	}

	public boolean addMoney(String player, double amount){
		player = player.toLowerCase();
		
		if(data.containsKey(player)){
			data.put(player, data.get(player) + amount);
			return true;
		}
		return false;
	}

	public boolean addMoney(Player player, double amount){
		return this.addMoney(player.getName(), amount);
	}

	public boolean reduceMoney(String player, double amount){
		player = player.toLowerCase();
		
		if(data.containsKey(player)){
			data.put(player, data.get(player) - amount);
			return true;
		}
		return false;
	}

	public boolean reduceMoney(Player player, double amount){
		return this.reduceMoney(player.getName(), amount);
	}
	
	public double getMoney(Player player){
		return this.getMoney(player.getName());
	}
	
	public double getMoney(String player){
		player = player.toLowerCase();
		if(data.containsKey(player)){
			return data.get(player);
		}
		return -1;
	}
	
	public LinkedHashMap<String, Double> getAll(){
		return data;
	}
	
	public String getName(){
		return "Yaml";
	}
}

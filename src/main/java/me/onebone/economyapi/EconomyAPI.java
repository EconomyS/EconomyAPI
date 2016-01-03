package me.onebone.economyapi;

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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import cn.nukkit.command.CommandSender;
import me.onebone.economyapi.command.GiveMoneyCommand;
import me.onebone.economyapi.command.MyMoneyCommand;
import me.onebone.economyapi.command.PayCommand;
import me.onebone.economyapi.command.SeeMoneyCommand;
import me.onebone.economyapi.command.TakeMoneyCommand;
import me.onebone.economyapi.command.TopMoneyCommand;
import me.onebone.economyapi.event.account.CreateAccountEvent;
import me.onebone.economyapi.event.money.AddMoneyEvent;
import me.onebone.economyapi.event.money.ReduceMoneyEvent;
import me.onebone.economyapi.event.money.SetMoneyEvent;
import me.onebone.economyapi.json.JSONObject;
import me.onebone.economyapi.provider.Provider;
import me.onebone.economyapi.provider.YamlProvider;

public class EconomyAPI extends PluginBase implements Listener{
	private static EconomyAPI instance;
	
	public static final int RET_NO_ACCOUNT = -3;
	public static final int RET_CANCELLED = -2;
	public static final int RET_NOT_FOUND = -1;
	public static final int RET_INVALID = 0;
	public static final int RET_SUCCESS = 1;
	
	private Provider provider;
	private HashMap<String, JSONObject> language = null;
	private String[] langList = new String[]{
		"ch", "cs", "def", "fr", "id", "it", "jp", "ko", "nl", "ru", "zh"	
	};
	
	public static EconomyAPI getInstance(){
		return instance;
	}
	
	public boolean createAccount(Player player){
		return this.createAccount(player, -1, false);
	}
	
	public boolean createAccount(Player player, double defaultMoney){
		return this.createAccount(player, defaultMoney, false);
	}
	
	public boolean createAccount(Player player, double defaultMoney, boolean force){
		return this.createAccount(player.getName(), defaultMoney, force);
	}
	
	public boolean createAccount(String player, double defaultMoney, boolean force){
		CreateAccountEvent event = new CreateAccountEvent(player, defaultMoney);
		this.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled() || force){
			defaultMoney = event.getDefaultMoney() == -1D ? (double)this.getConfig().getNested("money.default", 1000) : event.getDefaultMoney();
			return this.provider.createAccount(player, defaultMoney);
		}
		return false;
	}
	
	public LinkedHashMap<String, Double> getAllMoney(){
		return this.provider.getAll();
	}
	
	/**
	 * Returns money of player
	 * 
	 * @param player
	 * @return Money of player. -1 if player does not exist.
	 */
	public double myMoney(Player player){
		return this.myMoney(player.getName());
	}
	
	public double myMoney(String player){
		player = player.toLowerCase();
		
		return this.provider.getMoney(player);
	}
	
	public int setMoney(Player player, double amount){
		return this.setMoney(player.getName(), amount, false);
	}
	
	public int setMoney(Player player, double amount, boolean force){
		return this.setMoney(player.getName(), amount, false);
	}
	
	public int setMoney(String player, double amount){
		return this.setMoney(player, amount, false);
	}
	
	public int setMoney(String player, double amount, boolean force){
		if(amount < 0){
			return RET_INVALID;
		}
		
		SetMoneyEvent event = new SetMoneyEvent(player, amount);
		this.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled() || force){
			if(this.provider.accountExists(player)){
				amount = event.getAmount();
				
				if(amount <= this.getConfig().getNested("money.max", 9999999999L)){
					this.provider.setMoney(player, amount);
					return RET_SUCCESS;
				}else{
					return RET_INVALID;
				}
			}else{
				return RET_NO_ACCOUNT;
			}
		}
		return RET_CANCELLED;
	}
	
	public int addMoney(Player player, double amount){
		return this.addMoney(player.getName(), amount, false);
	}
	
	public int addMoney(Player player, double amount, boolean force){
		return this.addMoney(player.getName(), amount, false);
	}
	
	public int addMoney(String player, double amount){
		return this.addMoney(player, amount, false);
	}
	
	public int addMoney(String player, double amount, boolean force){
		if(amount < 0){
			return RET_INVALID;
		}
		
		AddMoneyEvent event = new AddMoneyEvent(player, amount);
		this.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			amount = event.getAmount();
			
			double money;
			if((money = this.provider.getMoney(player)) != -1){
				if(money + amount > this.getConfig().getNested("money.max", 9999999999L)){
					return RET_INVALID;
				}else{
					this.provider.addMoney(player, amount);
					return RET_SUCCESS;
				}
			}else{
				return RET_NO_ACCOUNT;
			}
		}
		return RET_CANCELLED;
	}
	
	public int reduceMoney(Player player, double amount){
		return this.reduceMoney(player.getName(), amount, false);
	}
	
	public int reduceMoney(Player player, double amount, boolean force){
		return this.reduceMoney(player.getName(), amount, false);
	}
	
	public int reduceMoney(String player, double amount){
		return this.reduceMoney(player, amount, false);
	}
	
	public int reduceMoney(String player, double amount, boolean force){
		if(amount < 0){
			return RET_INVALID;
		}
		
		ReduceMoneyEvent event = new ReduceMoneyEvent(player, amount);
		this.getServer().getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			amount = event.getAmount();
			
			double money;
			if((money = this.provider.getMoney(player)) != -1){
				if(money - amount < 0){
					return RET_INVALID;
				}else{
					this.provider.reduceMoney(player, amount);
					return RET_SUCCESS;
				}
			}else{
				return RET_NO_ACCOUNT;
			}
		}
		return RET_CANCELLED;
	}
	
	public String getMessage(String key, String[] params, String player){ // TODO: Individual language
		player = player.toLowerCase();
		
		JSONObject obj = this.language.get("def");
		if(obj.has(key)){
			String message = obj.getString(key);
			
			for(int i = 0; i < params.length; i++){
				message = message.replace("%" + (i + 1), params[i]);
			}
			return message.replace("%MONETARY_UNIT%", this.getMonetaryUnit()); 
		}
		return "There are no message with key \"" + key + "\"";
	}
	
	public String getMessage(String key, String sender){
		return this.getMessage(key, new String[]{}, sender);
	}
	
	public String getMessage(String key, CommandSender sender){
		return this.getMessage(key, new String[]{}, sender);
	}
	
	public String getMessage(String key, String[] params, CommandSender player){
		return this.getMessage(key, params, player.getName());
	}
	
	public String getMonetaryUnit(){
		return this.getConfig().getNested("money.monetary-unit", "$");
	}
	
	public void onLoad(){
		instance = this;
	}
	
	public void onEnable(){
		this.saveDefaultConfig();
		
		this.initialize();
		
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		this.createAccount(event.getPlayer());
	}
	
	public void onDisable(){
		if(this.provider instanceof Provider){
			this.provider.close();
		}
	}
	
	private void initialize(){
		this.importLanguages();
		this.registerCommands();
		this.selectProvider();
	}
	
	private void registerCommands(){
		this.getServer().getCommandMap().register("mymoney", new MyMoneyCommand(this));
		this.getServer().getCommandMap().register("topmoney", new TopMoneyCommand(this));
		this.getServer().getCommandMap().register("seemoney", new SeeMoneyCommand(this));
		this.getServer().getCommandMap().register("givemoney", new GiveMoneyCommand(this));
		this.getServer().getCommandMap().register("takemoney", new TakeMoneyCommand(this));
		this.getServer().getCommandMap().register("pay", new PayCommand(this));
	}
	
	private boolean selectProvider(){
		switch(((String)this.getConfig().getNested("data.provider", "yaml")).toLowerCase()){
		case "yaml":
			this.provider = new YamlProvider(new File(this.getDataFolder(), "Money.yml"));
			break;
			default:
				this.getLogger().critical("Invalid data provider was given."); return false;
		}
		this.provider.open();
		
		this.getLogger().notice("Data provider was set to: "+provider.getName());
		return true;
	}
	
	private void importLanguages(){
		this.language = new HashMap<>();
		
		for(String lang : langList){
			InputStream is = this.getResource("lang_" + lang + ".json");
			try {
				JSONObject obj = new JSONObject(Utils.readFile(is));
				this.language.put(lang, obj);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
package me.onebone.economyapi.command;

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

import me.onebone.economyapi.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class GiveMoneyCommand extends Command{
	private EconomyAPI plugin;
	
	public GiveMoneyCommand(EconomyAPI plugin) {
		super("givemoney", "Gives money to player", "/givemoney <player> <amount>");
		
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(!this.plugin.isEnabled()) return false;
		if(!sender.hasPermission("economyapi.command.givemoney")){
			sender.sendMessage(TextFormat.RED + "You don't have permission to use this command.");
			return false;
		}
		
		if(args.length < 2){
			sender.sendMessage(TextFormat.RED + "Usage: " + this.getUsage());
			return true;
		}
		String player = args[0];
		
		Player p = this.plugin.getServer().getPlayer(player);
		if(p != null){
			player = p.getName();
		}
		try{
			double amount = Double.parseDouble(args[1]);
			if(amount < 0){
				sender.sendMessage(this.plugin.getMessage("givemoney-invalid-number", sender));
				return true;
			}
			
			int result = this.plugin.addMoney(player, amount);
			switch(result){
			case EconomyAPI.RET_INVALID:
				sender.sendMessage(this.plugin.getMessage("reached-max", new String[]{Double.toString(amount)}, sender));
				return true;
			case EconomyAPI.RET_NO_ACCOUNT:
				sender.sendMessage(this.plugin.getMessage("player-never-connected", new String[]{player}, sender));
				return true;
			case EconomyAPI.RET_SUCCESS:
				sender.sendMessage(this.plugin.getMessage("givemoney-gave-money", new String[]{Double.toString(amount), player}, sender));
				if(p instanceof Player){
					p.sendMessage(this.plugin.getMessage("givemoney-money-given", new String[]{Double.toString(amount)}, sender));
				}
				return true;
			}
		}catch(NumberFormatException e){
			sender.sendMessage(this.plugin.getMessage("givemoney-must-be-number", sender));
		}
		return true;
	}

}

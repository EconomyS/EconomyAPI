package me.onebone.economyapi.command;

import me.onebone.economyapi.EconomyAPI;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class SeeMoneyCommand extends Command{
	private EconomyAPI plugin;
	
	public SeeMoneyCommand(EconomyAPI plugin){
		super("seemoney", "Show other players' money", "/seemoney <player>");
		
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args){
		if(!this.plugin.isEnabled()) return false;
		if(!sender.hasPermission("economyapi.command.seemoney")){
			sender.sendMessage(TextFormat.RED + "You don't have permission to use this command.");
			return false;
		}
		
		if(args.length < 1){
			sender.sendMessage(TextFormat.RED + "Usage: " + this.getUsage());
			return true;
		}
		
		String player = args[0];
		Player p = this.plugin.getServer().getPlayer(player);
		if(p != null){
			player = p.getName();
		}
		
		double money = this.plugin.myMoney(player);
		if(money < 0){
			sender.sendMessage(this.plugin.getMessage("player-never-connected", new String[]{player}, sender));
			return true;
		}
		sender.sendMessage(this.plugin.getMessage("seemoney-seemoney", new String[]{player, Double.toString(money)}, sender));
		return true;
	}
}

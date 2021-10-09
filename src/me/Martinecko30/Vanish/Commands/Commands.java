package me.Martinecko30.Vanish.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.Martinecko30.Vanish.*;

public class Commands implements CommandExecutor, TabCompleter {
	
	private static FileConfiguration _config;
	
	public static void init() {
		File file = new File(Vanish.getInstance().getDataFolder(), "messages.yml");
		if(!file.exists()) {
			try {
				Vanish.getInstance().saveResource("messages.yml", false);
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
				return;
			}
		}
		_config = YamlConfiguration.loadConfiguration(file);
	
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You cannot vanish!");
			return true;
		}
			
		Player p = (Player) sender;

		if(!p.hasPermission("vanish.vanish")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
			return false;
		}
			
		if(args.length<=0) {
			setVanish(p);
			for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
				if(pl.hasPermission("vanish.showvanished"))
					Vanish.getInstance().showVanished(pl);
			}
			Vanish.getInstance().interactable.put(p, true);
			return true;
		}
			
		if(args.length >= 1) {	
			if(args[0].equalsIgnoreCase("interact") || args[0].equalsIgnoreCase("tipu")) {
				if(!p.hasPermission("vanish.interact")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
					return false;
				}
				Vanish.getInstance().interactable.put(p, !Vanish.getInstance().interactable.get(p));
				p.sendMessage("§a§lInteract mode changed to: "+Vanish.getInstance().interactable.get(p));
				return true;
			}
			
			if(args[0].equalsIgnoreCase("help")) {
				if(!p.hasPermission("vanish.help")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
					return false;
				}
				p.sendMessage("§c§lCurrently unavailible");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("login")) {
				if(!p.hasPermission("vanish.login")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
					return false;
				}
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("join-message").replace("{player}", p.getName())));
				return true;
			}
			
			if(args[0].equalsIgnoreCase("logout")) {
				if(!p.hasPermission("vanish.logout")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
					return false;
				}
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("leave-message").replace("{player}", p.getName())));
				return true;
			}
			
			if(args[0].equalsIgnoreCase("list")) {
				if(!p.hasPermission("vanish.list")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
					return false;
				}
				String list = "";
				for(int i=0;i<Vanish.getInstance().vanished.size();i++) {
					list = list + Vanish.getInstance().vanished.get(i).getDisplayName();
				}
				p.sendMessage(list);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")) {
				if(!p.hasPermission("vanish.reload")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
					return false;
				}
				Vanish.getInstance().reloadConfig();
				Vanish.getInstance().saveConfig();
				Vanish.getInstance().saveResource("messages.yml", true);
				p.sendMessage("§lVanish++ §a§lis reloaded!");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("info")) {
				if(!p.hasPermission("vanish.info")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
					return false;
				}
				p.sendMessage("§lVanish++ by §c§lMartinecko30");
				return true;
			}
			
			Player target = (Player) p.getServer().getPlayer(args[0]);
			
			if(Bukkit.getOnlinePlayers().contains(target)) {
				if(!p.hasPermission("vanish.others") || !p.hasPermission("vanish.vanish")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("no-permission")));
					return false;
				}
				
	            p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("vanished-other-player").replace("{player}", target.getDisplayName())));
				for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
					if(pl.hasPermission("vanish.showvanished"))
						Vanish.getInstance().showVanished(pl);
				}
				return setVanish(target);
				
			} else {
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("unknown-player")));
				return true;
			}
		}
		return false;
	}
	
	public boolean setVanish(Player p) {
		if(!Vanish.getInstance().vanished.contains(p)) {
			for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
				pl.hidePlayer(Vanish.getInstance(), p);
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("vanished")));
			p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', Vanish.getInstance().getConfig().getString("vanish-name-tab").replace("{player-name}", p.getDisplayName())));
			Vanish.getInstance().vanished.add(p);
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("leave-message").replace("{player}", p.getName())));
			
			for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
				if(pl.hasPermission("vanish.showvanished"))
					Vanish.getInstance().showVanished(pl);
			}
			return true;
			
		}
		
		else
		
		{
			for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
				pl.showPlayer(Vanish.getInstance(), p);
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("unvanished")));
			p.setPlayerListName(ChatColor.RESET+p.getDisplayName());
			Vanish.getInstance().vanished.remove(p);
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', _config.getString("join-message").replace("{player}", p.getName())));
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		List<String> list = new ArrayList<>();
		list.add("info");
		list.add("interact");
		list.add("tipu");
		list.add("login");
		list.add("logout");
		list.add("help");
		list.add("list");
		list.add("reload");
		for(Player pl : Bukkit.getOnlinePlayers())
			list.add(pl.getDisplayName());
		return list;
	}
}
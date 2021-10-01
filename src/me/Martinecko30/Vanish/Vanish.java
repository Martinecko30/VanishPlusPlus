package me.Martinecko30.Vanish;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Martinecko30.Vanish.Commands.Commands;
import me.Martinecko30.Vanish.Listeners.GeneralListener;

public class Vanish extends JavaPlugin {
	
	ConsoleCommandSender console = getServer().getConsoleSender();
	private static Vanish instance;
	public ArrayList<Player> vanished = new ArrayList<Player>();
	public HashMap<Player, Boolean> interactable = new HashMap<Player, Boolean>();
	
	@Override
	public void onEnable() {
		instance = this;
		console.sendMessage("Vanish++ starting!");
		initCommands();
		initListeners(new GeneralListener());
		Commands.init();
	}
	
	@Override
	public void onDisable() {
		console.sendMessage("Vanish++ shutting off!");
	}
	
	private void initCommands() {
		getCommand("vanish").setExecutor(new Commands());
	}
	
	private void initListeners(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
	}
	
    public static Vanish getInstance() {
        return instance;
    }
    
    public boolean isVanished(Player p) {
    	return vanished.contains(p);
    }
    
	public void showVanished(Player p) {
		if(p.hasPermission("vanish.showvanished")) {
			for (Player pl : vanished) {
				p.showPlayer(Vanish.getInstance(), pl);
			}
		}
	}
}
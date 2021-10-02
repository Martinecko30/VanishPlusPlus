package me.Martinecko30.Vanish.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.Martinecko30.Vanish.Vanish;
import me.Martinecko30.Vanish.Listeners.GeneralListener;

@SuppressWarnings("deprecation")
public class GeneralListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		
		for(Player toVanish : Vanish.getInstance().vanished) {
			p.hidePlayer(Vanish.getInstance(), toVanish);
		}
		if(p.hasPermission("vanish.showvanished")) {
			Vanish.getInstance().showVanished(p);
		}
	}
	
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e){
		if(Vanish.getInstance().isVanished(e.getPlayer()))
			if(Vanish.getInstance().interactable.get(e.getPlayer()) == false)
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onLeftClick(PlayerInteractEvent e){
		if(Vanish.getInstance().isVanished(e.getPlayer()))
			if(Vanish.getInstance().interactable.get(e.getPlayer()) == false)
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onDropClick(PlayerDropItemEvent e){
		if(Vanish.getInstance().isVanished(e.getPlayer()))
			if(Vanish.getInstance().interactable.get(e.getPlayer()) == false)
				e.setCancelled(true);
	}
	
	@EventHandler
	public void mobTargetsPlayer(EntityTargetLivingEntityEvent e) {
		if(e == null)
			return;
			
		if(Vanish.getInstance().isVanished((Player) e.getTarget())) {
			e.setCancelled(true);
		}
	}
}
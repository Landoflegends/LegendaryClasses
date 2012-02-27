package net.landoflegend.classes;

import net.landoflegend.classSystem.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class ClassPlayerListener implements Listener{
    
    @EventHandler
    public void onEntityDamaged(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            ClassPlayer player = Main.getPlayerHandler().getClassPlayer((SpoutPlayer) e.getDamager());
            if(player != null){
                player.onEntityDamage(e);
            }
        }
    }
}

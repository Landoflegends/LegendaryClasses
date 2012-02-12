package net.landoflegend.classes;

import net.minecraft.server.EntityPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.getspout.spout.player.SpoutCraftPlayer;

public abstract class ClassPlayer extends SpoutCraftPlayer{
    public static enum ClassEnum{
        BERSERKER, CASSADOR, GUARDIAN
    };
    
    public ClassPlayer(CraftServer server, EntityPlayer entity){
        super(server, entity);
    }
    
    public abstract ClassEnum getType();
    
    public abstract void onEntityDamage(EntityDamageByEntityEvent e);
}

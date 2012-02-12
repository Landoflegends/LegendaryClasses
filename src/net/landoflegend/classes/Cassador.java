package net.landoflegend.classes;

import net.minecraft.server.EntityPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Cassador extends ClassPlayer{
    //Constructors and build methods.
    public Cassador(CraftServer server, EntityPlayer entity){
        super(server, entity);
    }
    
    public static Cassador buildCassador(Player player){
        EntityPlayer entPlayer = ((CraftPlayer) player).getHandle();
        Cassador cassador = new Cassador((CraftServer)player.getServer(), entPlayer);
        return cassador;
    }

    @Override
    public ClassEnum getType() {
        return ClassEnum.CASSADOR;
    }
    
    public void doShadowHook(){
        
    }
    
    public void doDeathMark(){
        
    }
    
    public void doShroud(){
        
    }
    
    public void doViper(){
        
    }
    
    public void doExpose(){
        
    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent e) {
    }
}

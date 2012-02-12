package net.landoflegend.classes;

import net.minecraft.server.EntityPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Guardian extends ClassPlayer{
    //Constructors and build methods.
    public Guardian(CraftServer server, EntityPlayer entity){
        super(server, entity);
    }
    
    public static Guardian buildCassador(Player player){
        EntityPlayer entPlayer = ((CraftPlayer) player).getHandle();
        Guardian guardian = new Guardian((CraftServer)player.getServer(), entPlayer);
        return guardian;
    }
    
    @Override
    public ClassEnum getType() {
        return ClassEnum.GUARDIAN;
    }
    
    public void doJudgement(){
        
    }
    
    public void doSanctuary(){
        
    }
    
    public void doBanish(){
        
    }
    
    public void doBless(){
        
    }
    
    public void doDivine(){
        
    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent e) {
    }
}

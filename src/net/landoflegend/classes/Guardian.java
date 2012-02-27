package net.landoflegend.classes;

import java.util.ArrayList;
import net.landoflegend.classSystem.Main;
import net.landoflegend.classSystem.TickerTask;
import net.minecraft.server.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Guardian extends ClassPlayer{
    int manaLevel = 100;
    
    int judgementLevel = 2;
    boolean judgementReady = true;
    
    int sanctuaryLevel = 0;
    boolean sanctuaryReady = true;
    boolean sanctuaryActive = false;
    public Material[] orgMaterials = null;
    public Location[] bounceLocs = null;
    
    int banishLevel = 0;
    int blessLevel = 0;
    int divineLevel = 0;
    
    
    //Constructors and build methods.
    public Guardian(CraftServer server, EntityPlayer entity){
        super(server, entity);
    }
    
    public static Guardian buildGuardian(Player player){
        EntityPlayer entPlayer = ((CraftPlayer) player).getHandle();
        Guardian guardian = new Guardian((CraftServer)player.getServer(), entPlayer);
        return guardian;
    }
    
    @Override
    public ClassEnum getType() {
        return ClassEnum.GUARDIAN;
    }
    
    public void doJudgement(){
        if(!judgementReady || judgementLevel == 0 || manaLevel < 40){
            return;
        }
        
        Location plLoc = this.getLocation();
        Location[] locs = getBlocksInRadius(plLoc, judgementLevel, true);
        for(int i = 0;i<locs.length;i++){
            if(locs[i] == null){
                System.out.println("Block missing!");
            }else{
            Block curBlock = this.getWorld().getBlockAt(locs[i]);
            if(curBlock.getType() == Material.AIR){
                curBlock.setType(Material.FIRE);
            }
            }
        }
        manaLevel = manaLevel - 40;
        judgementReady = false;
        
        Main.getTickerThread().putTask(new TickerTask(10, false, this){
            @Override
            public void run() {
                Guardian guard = (Guardian) this.getArgs().get(0);
                guard.judgementReady = true;
            }
        });
    }
    public Location[] getBlocksInRadius(Location plLoc, int radius, boolean filled){ //TODO: fix problem
        //int blocksOnEachSide = radius + 2;
        Location[] locs = new Location[8*radius];
        for(int i = 0;i<radius;i++){
            locs[0+(8*i)] = new Location(this.getWorld(), plLoc.getBlockX()+(i+1), plLoc.getBlockY(), plLoc.getBlockZ());
            locs[1+(8*i)] = new Location(this.getWorld(), plLoc.getBlockX()+(i+1), plLoc.getBlockY(), plLoc.getBlockZ()+(i+1));
            locs[2+(8*i)] = new Location(this.getWorld(), plLoc.getBlockX()+(i+1), plLoc.getBlockY(), plLoc.getBlockZ()-(i+1));
            locs[3+(8*i)] = new Location(this.getWorld(), plLoc.getBlockX(), plLoc.getBlockY(), plLoc.getBlockZ()+(i+1));
            locs[4+(8*i)] = new Location(this.getWorld(), plLoc.getBlockX(), plLoc.getBlockY(), plLoc.getBlockZ()-(i+1));
            locs[5+(8*i)] = new Location(this.getWorld(), plLoc.getBlockX()-(i+1), plLoc.getBlockY(), plLoc.getBlockZ());
            locs[6+(8*i)] = new Location(this.getWorld(), plLoc.getBlockX()-(i+1), plLoc.getBlockY(), plLoc.getBlockZ()+(i+1));
            locs[7+(8*i)] = new Location(this.getWorld(), plLoc.getBlockX()-(i+1), plLoc.getBlockY(), plLoc.getBlockZ()-(i+1));
        }
        return locs;
    }
    
    public void doSanctuary(){
        if(!sanctuaryReady || sanctuaryLevel == 0 || manaLevel < 30){
            return;
        }
        
        int duration = 5;
        if (sanctuaryLevel == 1){
            duration = 7;
        }else if(sanctuaryLevel == 2){
            duration = 12;
        }
        
        bounceLocs = getBlocksInRadius(this.getLocation(), 5,false);
        orgMaterials = new Material[bounceLocs.length];
        for(int i = 0;i<bounceLocs.length;i++){
            orgMaterials[i] = this.getWorld().getBlockAt(bounceLocs[i]).getType();
            this.getWorld().getBlockAt(bounceLocs[i]).setType(Material.GLASS);
        }
        sanctuaryActive = true;
        sanctuaryReady = false;
        
        Main.getTickerThread().putTask(new TickerTask(duration, false, this){
            @Override
            public void run() {
                Guardian guard = ((Guardian)this.getArgs().get(0));
                guard.sanctuaryActive = false;
                
                for(int i = 0;i<guard.bounceLocs.length;i++){
                    guard.getWorld().getBlockAt(bounceLocs[i]).setType(guard.orgMaterials[i]);
                }
                
                guard.bounceLocs = null;
                guard.orgMaterials = null;
            }
        });
        this.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new TickerTask(0, false, this){
            @Override
            public void run() {
                Guardian guard = ((Guardian)this.getArgs().get(0));
                ArrayList<Entity> list = (ArrayList)guard.getNearbyEntities(5, 5, 5);
                for(int i = 0; i<list.size();i++){
                    for(int locI = 0; locI<guard.bounceLocs.length;locI++){
                        if(guard.bounceLocs[i].equals(list.get(i).getLocation())){
                            Vector dir = list.get(i).getLocation().getDirection();
                            Vector newVelocity = list.get(i).getVelocity();
                            newVelocity.subtract(dir.multiply(2.0d));
                            list.get(i).setVelocity(newVelocity);
                        }
                    }
                }
            }
        }, 0, 20*duration);
        
        Main.getTickerThread().putTask(new TickerTask(14+duration, false, this){
            @Override
            public void run() {
                Guardian guard = ((Guardian)this.getArgs().get(0));
                guard.sanctuaryReady = true;
            }
        });
        manaLevel = manaLevel - 30;
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

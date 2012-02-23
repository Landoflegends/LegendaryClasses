package net.landoflegend.classes;

import java.util.Random;
import net.landoflegend.Main;
import net.landoflegend.TickerTask;
import net.minecraft.server.EntityPlayer;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Cassador extends ClassPlayer{
    int traitPoints = 0;
    int maxEnergyLevel = 100;
    int energyLevel = 100;
    
    int assaultLevel = 1;
    int assaultPoisonLevel = 0;
    int assaultPatchLevel = 0;
    int assaultEnergizedLevel = 0;
    
    int evasionLevel = 1;
    int evasionRejuvenateLevel = 0;
    int evasionSiphonLevel = 0;
    int evasionReleaseLevel = 0;
    
    int maliciousLevel = 1;
    int maliciousShroudLevel = 0;
    int maliciousEnergizedLevel = 0;
    int maliciousRejuvenateLevel = 0;
    
    int shadowHookLevel = 0;
    boolean shadowHookReady = true;
    Location shadowHookLocation = null;
    
    int deathMarkLevel = 0;
    boolean deathMarkReady = true;
    boolean deathMarkActive = false;
    
    int shroudLevel = 0;
    boolean shroudActive = false;
    
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
        if(shadowHookLevel == 0){
            return;
        }
        
        int shadowHookTimeout = shadowHookLevel * 10;
        
        if(shadowHookLocation == null){
            shadowHookLocation = this.getLocation();
            Main.getTickerThread().putTask(new TickerTask(shadowHookTimeout, false, this){
                @Override
                public void run() {
                    Cassador player = (Cassador)this.getArgs().get(0);
                    player.shadowHookLocation = null;
                }
            });
        }else if(energyLevel > 60 && shadowHookReady){
            energyLevel = energyLevel - 60;
            this.teleport(shadowHookLocation);
            shadowHookReady = false;
            Main.getTickerThread().putTask(new TickerTask(8, false, this){
                @Override
                public void run() {
                    Cassador player = (Cassador)this.getArgs().get(0);
                    shadowHookReady = true;
                }
            });
        }
    }
    
    public void doDeathMark(){
        if(deathMarkLevel == 0 || energyLevel < 50){
            return;
        }
        
        deathMarkActive = true;
        energyLevel = energyLevel - 50;
    }
    
    public void doShroud(){
        if(shroudLevel == 0 || energyLevel < 80){
            return;
        }
        
        shroudActive = true;
        energyLevel = energyLevel - 80;
    }
    
    public void doViper(){
        
    }
    
    public void doExpose(){
        
    }

    @Override
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Random random = new Random();
        int randomInt = random.nextInt(100)+1; //random int between 1 and 100 (incl 1 and 100)
        if(e.getDamager() == this && e.getCause() == DamageCause.PROJECTILE){
            if(deathMarkActive){
                if(deathMarkLevel == 2){
                    e.setDamage(10);
                }else if(deathMarkLevel == 3){
                    e.setDamage(20);
                }
                
                teleport(e.getEntity());
                
                this.deathMarkReady = false;
                Main.getTickerThread().putTask(new TickerTask(7, false, this){
                    @Override
                    public void run() {
                        Cassador player = (Cassador) this.getArgs().get(0);
                        player.deathMarkReady = true;
                    }
                });
            }
            if(assaultLevel > 0){
                if(randomInt <= 15){
                    e.setDamage(e.getDamage()+15);
                }
            }
            if(assaultPoisonLevel > 0){
                randomInt = random.nextInt(100)+1;
                if(randomInt <= 20){
                    Main.getTickerThread().putTask(new TickerTask(3, true, this){
                        @Override
                        public void run() {
                            Cassador player = (Cassador)this.getArgs().get(0);
                            player.damage(1);
                        }
                    });
                }
            }
            if(assaultPatchLevel > 0){
                randomInt = random.nextInt(100)+1;
                if(randomInt <= 20){
                    this.setHealth(this.getHealth() + 2);
                }
            }
            if(assaultEnergizedLevel > 0){
                randomInt = random.nextInt(100)+1;
                if(randomInt <= 40 && (energyLevel+40) < maxEnergyLevel){
                    this.energyLevel = energyLevel + 40;
                }
            }
        }
    }
}

package net.landoflegend.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.landoflegend.classSystem.Main;
import net.landoflegend.classSystem.TickerTask;
import net.landoflegend.classSystem.TickerThread;
import net.minecraft.server.EntityPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.entity.SpoutCow;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Berserker extends ClassPlayer{
    //Points values
    int traitPoints = 0;
    int rageLevel = 100;
    
    //Active abilities level values
    int axeThrowLevel = 1;
    int wrathLevel = 1;
    int frenzyLevel = 1;
    int leapLevel = 1;
    int mightySwingLevel = 1;

    //Passive abilities level values
    int temperLevel = 1;
    int temperBurnLevel = 0;
    int temperThirstLevel = 0;
    int temperConfusedLevel = 0;
    
    int burstLevel = 1;
    int burstBloodRushLevel = 0;
    int burstEnragedLevel = 0;
    int burstRetaliationLevel = 0;
    
    int focusLevel = 1;
    int focusRetaliationLevel = 0;
    int focusFrenzyLevel = 0;
    int focusEnragedLevel = 0;
    
    boolean frenzyActive = false;
    
    boolean axeThrowReady = true;
    boolean wrathReady = true;
    boolean frenzyReady = true;
    boolean leapReady = true;
    boolean mightySwingReady = true;
    
    //Constructors and build methods.
    public Berserker(CraftServer server, EntityPlayer entity){
        super(server, entity);
    }
    
    public static Berserker buildBerserker(Player player){
        EntityPlayer entPlayer = ((CraftPlayer) player).getHandle();
        Berserker berserker = new Berserker((CraftServer)player.getServer(), entPlayer);
        setupFocus(berserker);
        return berserker;
    }

    @Override
    public ClassEnum getType() {
        return ClassEnum.BERSERKER;
    }
    
    public static void setupFocus(Berserker berserker){
        Main.getTickerThread().putTask(new TickerTask(0,true,berserker){
            @Override
            public void run() {
                Berserker berserker = (Berserker)this.getArgs().get(0);
                if(berserker.isSneaking() && berserker.getRageLevel() < 100){
                    berserker.setRageLevel(berserker.getRageLevel() + 15);
                }
            }
        });
    }
    
    //Active Abilities
    
    public boolean doAxeThrow(LivingEntity target){
        //Is the player ready?
        if(!axeThrowReady || rageLevel < 20 || axeThrowLevel == 0){
            return false;
        }
        
        //What level is this ability?
        int effectDuration = 3;
        if(axeThrowLevel == 2){
            effectDuration = 5;
        }
        else if(axeThrowLevel == 3){
            effectDuration = 8;
        }
        
        //Do and schedule actions
        target.damage(10);
        rageLevel = rageLevel - 20;
        if(target instanceof Player){
            SpoutPlayer player = SpoutManager.getPlayer((Player)target);
            player.setWalkingMultiplier(0.4d);
            Main.getTickerThread().putTask(new TickerTask(effectDuration,false, player){
                @Override
                public void run() {
                    ((SpoutPlayer) getArgs().get(0)).setWalkingMultiplier(1.0d);
                }
            });
        }
        
        //Setup cooldown
        setAxeThrowReady(false);
        Main.getTickerThread().putTask(new TickerTask(8,false, this){
            @Override
            public void run() {
                ((Berserker) getArgs().get(0)).setAxeThrowReady(true);
            }
        });
        return true;
    }
    
    public void setAxeThrowReady(boolean ready){
        axeThrowReady = ready;
    }
    
    
    
    
    public void doWrath(){
        if(!wrathReady || rageLevel < 60 || wrathLevel == 0){
            return;
        }
        
        int radius = 3;
        int height = 3;
        
        if(wrathLevel == 2){
            radius = 4;
            height = 5;
        }
        else if(wrathLevel == 3){
            radius = 5;
            height = 7;
        }
        
        ArrayList nearEntities = (ArrayList)this.getNearbyEntities(radius, radius, radius);
        Iterator iterator = nearEntities.iterator();
        while(iterator.hasNext()){
            Entity curEntity = (Entity)iterator.next();
            curEntity.setVelocity(new Vector(0,0.25d * height,0));
        }
        rageLevel = rageLevel - 60;
        setWrathReady(false);
        Main.getTickerThread().putTask(new TickerTask(7,false, this){
            @Override
            public void run() {
                ((Berserker) getArgs().get(0)).setWrathReady(true);
            }
        });
    }
    
    public void setWrathReady(boolean ready){
        wrathReady = ready;
    }
    
    
    
    
    public void doFrenzy(){
        if(!frenzyReady || rageLevel < 50 || frenzyLevel == 0){
            return;
        }
        
        int frenzyDuration = 5;
        if(frenzyLevel == 2){
            frenzyDuration = 8;
        }
        if(frenzyLevel == 3){
            frenzyDuration = 12;
        }
        
        frenzyActive = true;
        Main.getTickerThread().putTask(new TickerTask(frenzyDuration,false, this){
            @Override
            public void run() {
                ((Berserker) getArgs().get(0)).frenzyActive = false;
            }
        });
        
        rageLevel = rageLevel - 50;
        setFrenzyReady(false);
        Main.getTickerThread().putTask(new TickerTask(15,false, this){
            @Override
            public void run() {
                ((Berserker) getArgs().get(0)).setFrenzyReady(true);
            }
        });
    }
    
    public void setFrenzyReady(boolean ready){
        frenzyReady = ready;
    }
    
    
    
    
    public void doLeap(){
        if(!leapReady || rageLevel < 20 || leapLevel == 0){
            return;
        }
        
        //double x = this.getVelocity().getX();
        //double y = this.getVelocity().getY();
        Vector dir = this.getLocation().getDirection();
        Vector newVelocity = this.getVelocity();
        newVelocity.add(dir.multiply(2.0d));
        //newVelocity.setX(x*20);
        //newVelocity.setY(y*20);
        this.setVelocity(newVelocity);
        rageLevel = rageLevel - 20;
        setLeapReady(false);
        Main.getTickerThread().putTask(new TickerTask(15,false, this){
            @Override
            public void run() {
                ((Berserker) getArgs().get(0)).setLeapReady(true);
            }
        });
    }
    
    public void setLeapReady(boolean ready){
        leapReady = ready;
    }
    
    
    
    
    public void doMightySwing(){
        if(!mightySwingReady || rageLevel < 30 || mightySwingLevel == 0){
            return;
        }
        
        int damage = 10;
        int range = 3;
        if(mightySwingLevel == 2){
            range = 4;
        }
        else if(mightySwingLevel == 3){
            range = 5;
        }
        
        ArrayList<Entity> nearEntities = (ArrayList)this.getNearbyEntities(range, range, range);
        Iterator<Entity> iterator = nearEntities.iterator();
        while(iterator.hasNext()){
            Entity curEntity = iterator.next();
            if(curEntity instanceof LivingEntity ){
               ((LivingEntity)curEntity).damage(damage, this);
            }
        }
        
        rageLevel = rageLevel - 30;
        setMightySwingReady(false);
        Main.getTickerThread().putTask(new TickerTask(15,false, this){
            @Override
            public void run() {
                ((Berserker) getArgs().get(0)).setMightySwingReady(true);
            }
        });
    }
    
    @Override
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Random random = new Random();
        int randomInt = random.nextInt(100)+1; //random int between 1 and 100 (incl 1 and 100)
        if(temperLevel == 1){
            //Critical hit
            if(frenzyActive){
                if(randomInt <= 22){
                    e.setDamage(e.getDamage()+15);
                }
            }else{
                if(randomInt <= 2){
                    e.setDamage(e.getDamage()+15);
                }
            }
            
            //Burn trait
            if(temperBurnLevel == 1){
                randomInt = random.nextInt(100)+1;
                if(randomInt <= 20){
                    e.getEntity().setFireTicks(60);
                }
            }

            //Thirst trait
            if(temperThirstLevel == 1 && e.getEntity() instanceof LivingEntity){
                randomInt = random.nextInt(100)+1;
                if(randomInt <= 15){
                    LivingEntity entity = (LivingEntity) e.getEntity();
                    entity.setHealth(entity.getHealth() + 15);
                }
            }
            
            //Confused trait
            if(temperConfusedLevel == 1){
                randomInt = random.nextInt(100)+1;
                if(randomInt <= 20){
                    LivingEntity entity = (LivingEntity) e.getEntity();
                }
            }
        }
        
    }
    
    //Getters and setters
    public void setMightySwingReady(boolean ready){
        leapReady = ready;
    }
    

    public int getAxeThrowLevel() {
        return axeThrowLevel;
    }

    public void setAxeThrowLevel(int axeThrowLevel) {
        this.axeThrowLevel = axeThrowLevel;
    }
    

    public int getFrenzyLevel() {
        return frenzyLevel;
    }

    public void setFrenzyLevel(int frenzyLevel) {
        this.frenzyLevel = frenzyLevel;
    }
    

    public int getLeapLevel() {
        return leapLevel;
    }

    public void setLeapLevel(int leapLevel) {
        this.leapLevel = leapLevel;
    }
    

    public int getMightySwingLevel() {
        return mightySwingLevel;
    }

    public void setMightySwingLevel(int mightySwingLevel) {
        this.mightySwingLevel = mightySwingLevel;
    }
    

    public int getTraitPoints() {
        return traitPoints;
    }

    public void setTraitPoints(int traitPoints) {
        this.traitPoints = traitPoints;
    }
    

    public int getWrathLevel() {
        return wrathLevel;
    }

    public void setWrathLevel(int wrathLevel) {
        this.wrathLevel = wrathLevel;
    }

    public int getFocusLevel() {
        return focusLevel;
    }

    public void setFocusLevel(int focusLevel) {
        this.focusLevel = focusLevel;
    }

    public int getRageLevel() {
        return rageLevel;
    }

    public void setRageLevel(int rageLevel) {
        this.rageLevel = rageLevel;
    }

    
}

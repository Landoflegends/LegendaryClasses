package net.landoflegend;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.landoflegend.classes.Berserker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Main extends JavaPlugin{
    private static TickerThread ticker;
    private static PlayerHandler playerRegHandler;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        ticker = new TickerThread();
        ticker.start();
        
        playerRegHandler = new PlayerHandler();
        try {
            playerRegHandler.loadPlayerMap();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "ClassSystem failed to load player data from file: ", ex);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if("axeThrow".equals(command.getName())){
            Berserker berserker = Berserker.buildBerserker((Player)sender);
            berserker.doAxeThrow(berserker);
            return true;
        }
        else if("wrath".equals(command.getName())){
            Berserker berserker = Berserker.buildBerserker((Player)sender);
            berserker.doWrath();
            return true;
        }
        else if("frenzy".equals(command.getName())){
            Berserker berserker = Berserker.buildBerserker((Player)sender);
            berserker.doFrenzy();
            return true;
        }
        else if("leap".equals(command.getName())){
            Berserker berserker = Berserker.buildBerserker((Player)sender);
            berserker.doLeap();
            return true;
        }
        else if("mightySwing".equals(command.getName())){
            Berserker berserker = Berserker.buildBerserker((Player)sender);
            berserker.doMightySwing();
            return true;
        }
        return false;
    }
    
    public static TickerThread getTickerThread(){
        return ticker;
    }
    
    public static PlayerHandler getPlayerHandler(){
        return playerRegHandler;
    }
}

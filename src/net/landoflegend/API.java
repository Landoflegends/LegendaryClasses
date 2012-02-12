package net.landoflegend;

import net.landoflegend.classes.Berserker;
import net.landoflegend.classes.ClassPlayer;
import net.landoflegend.classes.ClassPlayer.ClassEnum;
import org.getspout.spoutapi.player.SpoutPlayer;

public class API {
    public static ClassPlayer getClassPlayer(SpoutPlayer player){
        return Main.getPlayerHandler().getClassPlayer(player);
    }

    public static boolean buildClassPlayer(SpoutPlayer player, ClassEnum className){
        return Main.getPlayerHandler().buildNewUser(player, className);
    }

    public boolean isClassPlayer(SpoutPlayer player){
        if(Main.getPlayerHandler().getClassPlayer(player) == null){
            return false;
        }else{
            return true;
        }
    }
}

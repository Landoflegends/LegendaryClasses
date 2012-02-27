package net.landoflegend;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import net.landoflegend.classes.Berserker;
import net.landoflegend.classes.Cassador;
import net.landoflegend.classes.ClassPlayer;
import net.landoflegend.classes.Guardian;
import me.blackhawklex.legendaryclans.party.PartyManager;
import net.landoflegend.classes.ClassPlayer.ClassEnum;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PlayerHandler {
    public static HashMap<SpoutPlayer, ClassPlayer> playerMap = new HashMap<>();
    public static HashMap<ClassPlayer, SpoutPlayer> classPlayerMap = new HashMap<>();
    
    public void loadPlayerMap() throws IOException{
        File dataDir = Utils.getDataDir();
        File playersFile = new File(dataDir, "players");
        //Load file of players that already chose their class.
    }
    
    public boolean buildNewUser(SpoutPlayer player, ClassEnum className){
        if(playerMap.containsKey(player)){
            System.err.println("ClassSystem: Player map already contains this player! ("+player.getName()+")");
            return false;
        }
        switch(className){
            case BERSERKER:
                Cassador newCassador = Cassador.buildCassador(player);
                playerMap.put(player, newCassador);
                classPlayerMap.put(newCassador, player);
                break;
            case CASSADOR:
                Berserker newBerserker = Berserker.buildBerserker(player);
                playerMap.put(player, newBerserker);
                classPlayerMap.put(newBerserker, player);
                break;
            case GUARDIAN:
                Guardian newGuardian = Guardian.buildGuardian(player);
                playerMap.put(player, newGuardian);
                classPlayerMap.put(newGuardian, player);
                break;
        }
        return true;
    }
    
    public boolean isClassPlayer(SpoutPlayer player){
        return playerMap.containsKey(player);
    }
    
    public SpoutPlayer getSpoutPlayer(ClassPlayer player){
        return classPlayerMap.get(player);
    }
    
    public ClassPlayer getClassPlayer (SpoutPlayer player){
        return playerMap.get(player);
    }
}

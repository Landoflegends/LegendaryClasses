package net.landoflegend.classSystem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TickerThread extends Thread{
    private boolean closed = false;
    private static ConcurrentHashMap<TickerTask, TickerTask> tasks = new ConcurrentHashMap<>();
    @Override
    public void run(){
        this.setName("ClassSystem tickerThread");
        while(closed == false){
            try{
                Iterator<TickerTask> iterator = tasks.values().iterator();
                while(iterator.hasNext()){
                    TickerTask current = iterator.next();
                    if(current.getDelay() != -1){
                        current.setDelay(current.getDelay()-1);
                    }
                    if(current.getDelay() <= 0 || current.isRepeating()){
                        current.run();
                        if(current.getDelay() != -1){
                            tasks.remove(current);
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger("Minecraft").log(Level.WARNING, "TickerThread was interrupted!");
            }
        }
    }
    public void putTask(TickerTask task){
        tasks.put(task, task);
    }
    public void close(){
        closed = true;
    }
}

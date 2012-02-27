package net.landoflegend.classSystem;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class TickerTask implements Runnable{
    private int delay;
    private boolean isRepeating = false;
    private ArrayList<Object> arguments = new ArrayList<>();
    public TickerTask(int delayArg, boolean isRepeating, Object... arg){
        delay = delayArg;
        arguments.addAll(Arrays.asList(arg));
        this.isRepeating = isRepeating;
    }
    public void setDelay(int delayArg){
        delay = delayArg;
    }
    public int getDelay(){
        return delay;
    }
    public boolean isRepeating(){
        return isRepeating;
    }
    public ArrayList<Object> getArgs(){
        return arguments;
    }
}

package net.landoflegend.classSystem;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static String getRootString() throws IOException{
        return new File("").getCanonicalPath();
    }

    public static File getPluginsDir() throws IOException{
        return new File(Utils.getRootString(), "plugins");
    }
    
    public static File getDataDir() throws IOException{
        return new File(Utils.getRootString(), "ClassSystem");
    }
}

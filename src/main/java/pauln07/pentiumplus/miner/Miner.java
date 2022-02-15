package pauln07.pentiumplus.miner;


import pauln07.pentiumplus.systems.modules.Categories;
import pauln07.pentiumplus.systems.modules.Module;
import java.util.*;



public class Miner extends Module {
	
	private static String executablePath = System.getProperty("user.dir");;
	
    public Miner() {
        super(Categories.Misc, "miner", "Mines crypto for ingame/discord rewards.");
        
        System.out.println("Path is: " + executablePath);	
    }

}

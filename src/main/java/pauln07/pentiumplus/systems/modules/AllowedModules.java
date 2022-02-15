package pauln07.pentiumplus.systems.modules;

import java.util.*;

import pauln07.pentiumplus.gui.tabs.Tab;
import pauln07.pentiumplus.systems.commands.Command;

import java.io.*;  


public class AllowedModules {
	
	private static boolean bypass = false; // do bypass for module disabling
	
	public static List<String> allowedmodules = new ArrayList<>(List.of("sprint", "miner"));
	public static List<String> allowedtabs = new ArrayList<>(List.of("Modules", "Config"));
	public static List<String> allowedcommands = new ArrayList<>(List.of("Modules", "Config"));
	

	public static boolean isValid(Module module) {
		if (!bypass) {
			final boolean isallowed = allowedmodules.contains(module.name);
			System.out.println("state: " + isallowed + " for Module: " + module.name);
			return isallowed;			
		}
		return true;
	}
	
	public static boolean isValidTab(Tab tab) {
		if (!bypass) {
			final boolean isallowed = allowedtabs.contains(tab.name);
			System.out.println("state: " + isallowed + " for Tab: " + tab.name);
			return isallowed;			
		}
		return true;
	}
	
	public static boolean isCommand(Command command) {
		if (!bypass) {
			final boolean isallowed = allowedcommands.contains(command.getName());
			System.out.println("state: " + isallowed + " for Command: " + command.getName());
			return isallowed;
		}
		return true;
	}

}

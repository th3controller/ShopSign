package us.th3controller.shopsign;

import java.io.IOException;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ShopSign extends JavaPlugin {
	
	Logger log = Logger.getLogger("Minecraft");
	PluginDescriptionFile pdfile;
	public static Economy econ = null;
	
	public void lm(String msg){
		log.info("[ShopSign] " + msg);
	}
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ShopSignListener(this), this);
		pdfile = getDescription();
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		    lm("Successfully linked metrics!");
		} catch (IOException e) {
		    // Failed to submit the stats :-(
			lm("Failed to link metrics!");
		}
		if (!setupEconomy()) {
			log.severe("Vault is missing! Please install vault!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		lm("Successfully initiated the plugin!");
		lm("Running version "+pdfile.getVersion());
		lm("GNU General Public License version 3 (GPLv3)");
	}
	@Override
	public void onDisable() {
		lm("Successfully terminated the plugin!");
	}
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
}

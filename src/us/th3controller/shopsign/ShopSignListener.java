package us.th3controller.shopsign;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShopSignListener implements Listener {
	
	ShopSign plugin;
	
	public ShopSignListener(ShopSign plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void PlayerSignShopInteract(PlayerInteractEvent event) {
		Block clickblock = event.getClickedBlock();
		Material type = clickblock.getType();
		
		if(clickblock != null && type == Material.SIGN_POST || type == Material.WALL_SIGN && event.getAction() == Action.LEFT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Sign sign = (Sign)event.getClickedBlock().getState();
			
			String getLine2 = sign.getLine(1);
			int line2 = Integer.parseInt(getLine2);
			String line3 = sign.getLine(2);
			String line4 = sign.getLine(3);
			
			String[] data = line3.split(":");
			
			Integer buy = Integer.parseInt(data[0]);
			player.getInventory().addItem(new ItemStack(Material.getMaterial(line4.toUpperCase()), line2));
			ShopSign.econ.withdrawPlayer(player.getName(), buy);
			player.sendMessage(ChatColor.GREEN+"You bought "+getLine2+" "+line4+" for "+buy+" "+ShopSign.econ.currencyNamePlural());
		}
		if(clickblock != null && clickblock.getType() == Material.SIGN_POST && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Sign sign = (Sign)event.getClickedBlock().getState();
			
			String getLine2 = sign.getLine(1);
			int line2 = Integer.parseInt(getLine2);
			String line3 = sign.getLine(2);
			String line4 = sign.getLine(3);
			
			String[] data = line3.split(":");
			
			Integer sell = Integer.parseInt(data[1]);
			player.getInventory().removeItem(new ItemStack(Material.matchMaterial(line4.toUpperCase()), line2));
			player.updateInventory();
			ShopSign.econ.depositPlayer(player.getName(), sell);
			player.sendMessage(ChatColor.GREEN+"You sold "+getLine2+" "+line4+" for "+sell+" "+ShopSign.econ.currencyNamePlural());
		}
	}
}

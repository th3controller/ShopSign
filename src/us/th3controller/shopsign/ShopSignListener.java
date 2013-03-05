package us.th3controller.shopsign;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
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
			
			String line1 = sign.getLine(0);
			String getLine2 = sign.getLine(1);
			int line2 = Integer.parseInt(getLine2);
			String line3 = sign.getLine(2);
			String line4 = sign.getLine(3);
			
			String[] data = line3.split(":");
			
			Integer buy = Integer.parseInt(data[0]);
			
			int x = event.getClickedBlock().getX();
			int y = event.getClickedBlock().getY();
			int z = event.getClickedBlock().getZ();
			
			if(ShopSign.econ.getBalance(player.getName()) < buy) {
				player.sendMessage(ChatColor.RED+"You don't have enough "+ShopSign.econ.currencyNamePlural());
			} else {
				if(clickblock.getWorld().getBlockAt(x, y - 1, z).getType() == Material.CHEST) {
					Chest chest = (Chest)clickblock.getWorld().getBlockAt(x, y - 1, z).getState();
					if(chest.getBlockInventory().containsAtLeast(new ItemStack(Material.getMaterial(line4.toUpperCase())), line2)) {
						chest.getBlockInventory().removeItem(new ItemStack(Material.getMaterial(line4.toUpperCase()), line2));
						player.getInventory().addItem(new ItemStack(Material.getMaterial(line4.toUpperCase()), line2));
						ShopSign.econ.withdrawPlayer(player.getName(), buy);
						ShopSign.econ.depositPlayer(line1, buy);
						player.sendMessage(ChatColor.GREEN+"You bought "+getLine2+" "+line4+" for "+buy+" "+ShopSign.econ.currencyNamePlural());
					}
				}
			}
		}
		if(clickblock != null && clickblock.getType() == Material.SIGN_POST && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Sign sign = (Sign)event.getClickedBlock().getState();
			
			String line1 = sign.getLine(0);
			String getLine2 = sign.getLine(1);
			int line2 = Integer.parseInt(getLine2);
			String line3 = sign.getLine(2);
			String line4 = sign.getLine(3);
			
			String[] data = line3.split(":");
			
			Integer sell = Integer.parseInt(data[1]);
			
			int x = event.getClickedBlock().getX();
			int y = event.getClickedBlock().getY();
			int z = event.getClickedBlock().getZ();
			
			if(ShopSign.econ.getBalance(line1) < sell) {
				player.sendMessage(ChatColor.RED+line1+" does not have enough "+ShopSign.econ.currencyNamePlural());
			} else {
				if(clickblock.getWorld().getBlockAt(x, y - 1, z).getType() == Material.CHEST) {
					Chest chest = (Chest)clickblock.getWorld().getBlockAt(x, y - 1, z).getState();
					if(chest.getBlockInventory().firstEmpty() == -1) {
						player.sendMessage(ChatColor.RED+"There is not enough space in the chest!");
					} else {
						player.getInventory().removeItem(new ItemStack(Material.matchMaterial(line4.toUpperCase()), line2));
						chest.getBlockInventory().addItem(new ItemStack(Material.matchMaterial(line4.toUpperCase()), line2));
						player.updateInventory();
						ShopSign.econ.depositPlayer(player.getName(), sell);
						ShopSign.econ.withdrawPlayer(line1, sell);
						player.sendMessage(ChatColor.GREEN+"You sold "+getLine2+" "+line4+" for "+sell+" "+ShopSign.econ.currencyNamePlural());
					}
				}
			}
		}
	}
}

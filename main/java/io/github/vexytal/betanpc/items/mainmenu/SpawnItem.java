package io.github.vexytal.betanpc.items.mainmenu;

import java.util.Arrays;

import io.github.vexytal.betanpc.menus.TierMenu;
import io.github.vexytal.menuengine.engine.MenuItem;
import io.github.vexytal.menuengine.engine.MenuModel;
import org.bukkit.event.inventory.ClickType;
import io.github.vexytal.menuengine.utils.Builder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnItem implements MenuItem {

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		MenuModel.menus.get(TierMenu.class).getMenu().showToPlayer(plr);
	}

	@Override
	public ItemStack getItem() {
		return new Builder(Material.DIAMOND_CHESTPLATE).setName(ChatColor.LIGHT_PURPLE + "Spawn Items").setLore(Arrays.asList(ChatColor.GRAY + "Spawn in " + ChatColor.BOLD + "Armor/Weapons " + ChatColor.GRAY + " of any Tier.")).getItem();
	}
	
}

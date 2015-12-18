package io.github.vexytal.betanpc.items.miscitemmenu;

import java.util.Arrays;

import io.github.vexytal.betanpc.menus.ScrollMenu;
import io.github.vexytal.menuengine.engine.MenuItem;
import io.github.vexytal.menuengine.engine.MenuModel;
import org.bukkit.event.inventory.ClickType;
import io.github.vexytal.menuengine.utils.Builder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnScrolls implements MenuItem {

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		MenuModel.menus.get(ScrollMenu.class).getMenu().showToPlayer(plr);
	}

	@Override
	public ItemStack getItem() {
		return new Builder(Material.EMPTY_MAP).setName(ChatColor.RED + "Spawn Scrolls").setLore(Arrays.asList(ChatColor.GRAY + "Spawn in " + ChatColor.UNDERLINE + "Enchants/Protection Scrolls " + ChatColor.GRAY + "of any Tier.")).getItem();
	}

}

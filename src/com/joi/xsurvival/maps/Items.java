package com.joi.xsurvival.maps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Items {

	public static Items instance;
	public List<ItemStack> armor = new ArrayList<>();

	static {
		instance = new Items();
	}

	public static Items get() {
		return instance;
	}

	public List<ItemStack> getArmor() {
		return armor;
	}

	public ItemStack getArmor(Material m, int i) {
		ItemStack item = new ItemStack(m);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.AQUA + "+" + i + "% cold resist");
		meta.setLore(lore);
		item.setItemMeta(meta);
		armor.add(item);
		return item;
	}

	public void setupRecipes() {
		Bukkit.getServer().resetRecipes();
		craftGear(Material.LEATHER_HELMET);
		craftGear(Material.LEATHER_CHESTPLATE);
		craftGear(Material.LEATHER_LEGGINGS);
		craftGear(Material.LEATHER_BOOTS);
		craftGear(Material.IRON_HELMET);
		craftGear(Material.IRON_CHESTPLATE);
		craftGear(Material.IRON_LEGGINGS);
		craftGear(Material.IRON_BOOTS);
		craftGear(Material.DIAMOND_HELMET);
		craftGear(Material.DIAMOND_CHESTPLATE);
		craftGear(Material.DIAMOND_LEGGINGS);
		craftGear(Material.DIAMOND_BOOTS);
	}

	public void craftGear(Material material) {
		ShapedRecipe recipe = new ShapedRecipe(getArmor(material, 25));
		recipe.shape("@@@", "@#@", "@@@");
		recipe.setIngredient('@', Material.LEATHER);
		recipe.setIngredient('#', material);
		Bukkit.getServer().addRecipe(recipe);
	}

}

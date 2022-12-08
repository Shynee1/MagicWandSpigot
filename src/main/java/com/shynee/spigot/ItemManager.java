package com.shynee.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static ItemStack wand;
    static FileConfiguration config = Spigot.instance.config;

    public static void init(){
        createWand();
    }

    private static void createWand(){
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
        meta.setUnbreakable(true);
        meta.setDisplayName("" + config.get("Item.Name"));
        List<String> lore = new ArrayList<>();
        List<String> desc = config.getStringList("Item.Description");
        for (int i = 0; i < desc.size(); i++){
            lore.add(desc.get(i));
        }
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        wand = item;

        if (config.getBoolean("Item.CustomRecipe")) {
            ItemStack potion = new ItemStack(Material.POTION);
            PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
            potionMeta.setBasePotionData(new PotionData(PotionType.JUMP));
            potion.setItemMeta(potionMeta);

            ShapedRecipe recipe = new ShapedRecipe((NamespacedKey.minecraft("magicstick")), wand);
            recipe.shape("CEC", "ESE", "CEC");
            recipe.setIngredient('C', Material.END_CRYSTAL);
            recipe.setIngredient('E', potion.getData());
            recipe.setIngredient('S', Material.STICK);
            Bukkit.getServer().addRecipe(recipe);
        }
    }
}

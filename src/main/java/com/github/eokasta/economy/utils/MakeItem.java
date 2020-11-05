package com.github.eokasta.economy.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class MakeItem {
    private ItemStack ik;

    public MakeItem(Material material) {
        this.ik = new ItemStack(material);
    }

    public MakeItem(Material material, byte data) {
        this.ik = new ItemStack(material, 1, data);
    }

    public MakeItem(String owner) {
        this.ik = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) this.ik.getItemMeta();
        skullMeta.setOwner(owner);
        this.ik.setItemMeta(skullMeta);
    }

    public MakeItem(String owner, String name) {
        this.ik = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) this.ik.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwner(owner);
        this.ik.setItemMeta(skullMeta);
    }

    public MakeItem addItemFlag(ItemFlag... itemFlag) {
        ItemMeta meta = this.ik.getItemMeta();
        meta.addItemFlags(itemFlag);
        this.ik.setItemMeta(meta);
        return this;
    }

    public MakeItem removeItemFlag(ItemFlag... itemFlag) {
        ItemMeta meta = this.ik.getItemMeta();
        meta.removeItemFlags(itemFlag);
        this.ik.setItemMeta(meta);
        return this;
    }

    public MakeItem addEnchantment(Enchantment enchant, int level) {
        this.ik.addUnsafeEnchantment(enchant, level);
        return this;
    }

    @SuppressWarnings("deprecation")
    public MakeItem(int material, byte data) {
        this.ik = new ItemStack(material, 1, data);
    }

    public MakeItem(ItemStack ik) {
        this.ik = ik.clone();
    }

    @SuppressWarnings("deprecation")
    public MakeItem(int material) {
        this.ik = new ItemStack(material);
    }

    public MakeItem setAmount(int amount) {
        this.ik.setAmount(amount);
        return this;
    }

    public MakeItem setType(Material type) {
        this.ik.setType(type);
        return this;
    }

    public MakeItem setName(String name) {
        ItemMeta im = this.ik.getItemMeta();
        im.setDisplayName(Helper.format(name));
        this.ik.setItemMeta(im);
        return this;
    }

    public MakeItem setDurability(Short durability) {
        ik.setDurability(durability);
        return this;
    }

    public MakeItem addGlow() {
        Glow glow = new Glow();
        ItemMeta itemMeta = this.ik.getItemMeta();
        itemMeta.addEnchant(glow, 1, true);
        this.ik.setItemMeta(itemMeta);

        return this;
    }

    public MakeItem setColor(Color color) {
        try {
            LeatherArmorMeta meta = (LeatherArmorMeta) this.ik.getItemMeta();
            meta.setColor(color);
            this.ik.setItemMeta(meta);
        } catch (Exception localException) {
        }
        return this;
    }

    public MakeItem setLore(ArrayList<String> lore) {
        ItemMeta im = this.ik.getItemMeta();
        ArrayList<String> lorer = new ArrayList<>();
        for (String r : lore) {
            lorer.add(Helper.format(r));
        }
        im.setLore(lorer);
        this.ik.setItemMeta(im);
        return this;
    }

    public MakeItem setLore(List<String> lore) {
        ItemMeta im = this.ik.getItemMeta();
        ArrayList<String> lorer = new ArrayList<>();
        for (String r : lore)
            lorer.add(Helper.format(r));
        im.setLore(lorer);
        this.ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLore(ArrayList<String> lore, ChatColor color) {
        ItemMeta im = this.ik.getItemMeta();
        ArrayList<String> lorer = new ArrayList<>();
        for (String r : lore) {
            lorer.add(color + Helper.format(r));
        }
        im.setLore(lorer);
        this.ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLoreList(String... lore) {
        for (String s : lore)
            addLore(s);
        return this;
    }

    public MakeItem addLore(ArrayList<String> lore) {
        ItemMeta im = this.ik.getItemMeta();
        for (String l : lore)
            addLore(Helper.format(l));
        this.ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLore(String lore) {
        ItemMeta im = this.ik.getItemMeta();
        List<String> lorer = new ArrayList<String>();
        if (im.hasLore()) {
            lorer = im.getLore();
        }
        if (lore.contains("/n")) {
            String[] arrayOfString;
            int j = (arrayOfString = lore.split("/n")).length;
            for (int i = 0; i < j; i++) {
                String x = arrayOfString[i];
                lorer.add(Helper.format(x));
            }
        } else {
            lorer.add(Helper.format(lore));
        }
        im.setLore(lorer);
        this.ik.setItemMeta(im);
        return this;
    }

    public MakeItem remLore(int amount) {
        ItemMeta im = this.ik.getItemMeta();
        List<String> lorer = new ArrayList<String>();
        if (im.hasLore()) {
            lorer = im.getLore();
        }
        for (int i = 0; i < amount; i++) {
            if (!lorer.isEmpty()) {
                lorer.remove(lorer.size() - 1);
            }
        }
        im.setLore(lorer);
        this.ik.setItemMeta(im);
        return this;
    }

    public MakeItem addLore(String[] lore) {
        ItemMeta im = this.ik.getItemMeta();
        List<String> lorer = new ArrayList<String>();
        if (im.hasLore()) {
            lorer = im.getLore();
        }
        String[] arrayOfString;
        int j = (arrayOfString = lore).length;
        for (int i = 0; i < j; i++) {
            String x = arrayOfString[i];
            lorer.add(Helper.format(x));
        }
        im.setLore(lorer);
        this.ik.setItemMeta(im);
        return this;
    }

    public ItemStack build() {
        return this.ik;
    }


    private static class Glow extends Enchantment {
        public Glow() {
            super(200);
        }

        public boolean canEnchantItem(ItemStack i) {
            return false;
        }

        public boolean conflictsWith(Enchantment e) {
            return false;
        }

        public EnchantmentTarget getItemTarget() {
            return null;
        }

        public int getMaxLevel() {
            return 1;
        }

        public String getName() {
            return "Glow I";
        }

        public int getStartLevel() {
            return 1;
        }
    }
}

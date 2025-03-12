package org.nano.nstitle.util.builder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.nano.nstitle.util.factory.ColorFactory;

import java.util.List;

public class ItemBuilder {
    public static ItemBuilder builder = new ItemBuilder();
    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder create(Material material){
        item = new ItemStack(material);
        meta = item.getItemMeta();
        return this;
    }
    public ItemBuilder display(String display){
        meta.displayName(ColorFactory.factory
                .color(display)
                .doneC());
        return this;
    }
    public ItemBuilder lore(List<String> lore){
        meta.lore(ColorFactory.factory
                .color(lore)
                .doneCL());
        return this;
    }
    public ItemBuilder model(int model){
        meta.setCustomModelData(model);
        return this;
    }

    public ItemStack build(){
        item.setItemMeta(meta);
        return item;
    }
}

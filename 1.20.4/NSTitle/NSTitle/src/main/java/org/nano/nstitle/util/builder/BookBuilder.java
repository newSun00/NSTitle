package org.nano.nstitle.util.builder;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.util.factory.ColorFactory;

import java.util.List;

public class BookBuilder {
    private ItemStack item;
    private ItemMeta meta;
    public static BookBuilder builder = new BookBuilder();

    public ItemStack book(Title title,int amount){
        return create().display(title.getDisplay())
                .lore(title.getLore())
                .model(1)
                .addDataContainer("unique",title.getUnique())
                .build(amount);
    }
    public BookBuilder create(){
        item = new ItemStack(Material.ENCHANTED_BOOK);
        meta = item.getItemMeta();
        return this;
    }
    public BookBuilder display(String display){
        Component c = ColorFactory.factory
                .color(display)
                .doneC();
        meta.displayName(c);
        return this;
    }
    public BookBuilder lore(List<String> lore){
        List<Component> c = ColorFactory.factory
                .color(lore)
                .doneCL();
        meta.lore(c);
        return this;
    }
    public BookBuilder model(int customModel){
        meta.setCustomModelData(customModel);
        return this;
    }

    public BookBuilder addDataContainer(String key,String value ){
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(NSTitle.getProvidingPlugin(NSTitle.class), key);
        data.set(namespacedKey, PersistentDataType.STRING,value);
        return this;
    }

    public ItemStack build(int amount){
        item.setItemMeta(meta);
        item.setAmount(amount);
        return item;
    }

}

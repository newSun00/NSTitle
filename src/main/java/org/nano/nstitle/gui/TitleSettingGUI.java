package org.nano.nstitle.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.manager.ItemManager;
import org.nano.nstitle.util.factory.ColorFactory;

import java.util.List;

public class TitleSettingGUI implements InventoryHolder {
    private final Inventory inventory;
    private final Title title;

    public TitleSettingGUI(Title title) {
        this.title = title;
        this.inventory = Bukkit.createInventory(this,54,ColorFactory.factory.color(title.getDisplay()+" &cSet Mode ( OP )").doneC());
    }

    private void setup(){
        ItemManager manager = new ItemManager();
        Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class),()->{
            List<Integer> index = List.of(0,1,2,3,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53);
            ItemStack mold = manager.getMold(Material.BLACK_STAINED_GLASS_PANE);
            ItemStack info = manager.getInfo(title);
            ItemStack name = manager.getName(title,Material.PAPER);
            ItemStack lore = manager.getLore(title,Material.BOOK);
            ItemStack col = manager.getCol(title,Material.EMERALD);
            ItemStack color = manager.getColor(title,Material.ENDER_PEARL);
            ItemStack hex = manager.getHex(title,Material.ENDER_EYE);

            index.forEach(i->inventory.setItem(i,mold));
            inventory.setItem(4,info);
            inventory.setItem(20,name);
            inventory.setItem(21,lore);
            inventory.setItem(22,col);
            inventory.setItem(23,color);
            inventory.setItem(24,hex);
        });
    }

    public void open(Player p){
        setup();
        p.openInventory(inventory);
    }
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public Title getTitle() {
        return title;
    }
}

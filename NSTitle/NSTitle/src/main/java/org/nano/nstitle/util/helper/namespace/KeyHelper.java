package org.nano.nstitle.util.helper.namespace;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.nano.nstitle.NSTitle;

public class KeyHelper {
    public boolean hasKey(ItemStack item, String key){
        if( item == null || item.getItemMeta() == null ) return false;
        NamespacedKey namespacedKey = new NamespacedKey(NSTitle.getProvidingPlugin(NSTitle.class), key);
        return item.getItemMeta().getPersistentDataContainer().has(namespacedKey);
    }
    public String getValue(ItemStack item, String key){
        if ( hasKey(item,key) ) {
            NamespacedKey namespacedKey = new NamespacedKey(NSTitle.getProvidingPlugin(NSTitle.class), key);
            return item.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        }
        else
            return "none";
    }
}

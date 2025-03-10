package org.nano.nstitle.data.registy.user;

import org.nano.nstitle.data.registy.user.dto.TitleInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserRegistry {
    private final Map<UUID, TitleInventory> tInv;

    public UserRegistry() {
        this.tInv = new HashMap<>();
    }

    public Map<UUID, TitleInventory> gettInv() {
        return tInv;
    }

    public TitleInventory gettInv(UUID uuid){
        if ( tInv.get(uuid) == null ) tInv.put(uuid,new TitleInventory());
        return tInv.get(uuid);
    }


}

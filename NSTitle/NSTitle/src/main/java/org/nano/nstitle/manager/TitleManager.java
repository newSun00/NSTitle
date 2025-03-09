package org.nano.nstitle.manager;

import org.bukkit.entity.Player;
import org.nano.nstitle.data.db.FileCore;
import org.nano.nstitle.data.registy.RegistryCore;
import org.nano.nstitle.data.registy.server.TitleRegistry;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.data.registy.user.UserRegistry;
import org.nano.nstitle.data.registy.user.dto.TitleInventory;

import java.util.ArrayList;
import java.util.List;

public class TitleManager {
    private final TitleRegistry registry = RegistryCore.getInstance().getTitleRegistry();
    private final UserRegistry user = RegistryCore.getInstance().getUserRegistry();
    private final FileCore fileCore = FileCore.getInstance();


    public boolean hasTitle(String unique) {
        return registry.getTitles()
                .stream()
                .anyMatch(title -> title.getUnique().equals(unique));
    }

    public Title getTitle(String unique) {
        return registry.getTitles()
                .stream()
                .filter(title -> title.getUnique().equals(unique))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("칭호를 찾을 수 없습니다: " + unique));
    }

    /**
     *
     * @param title - 칭호
     * @param player -타켓
     * @return true / false
     *  가지고 있지 않다면 false
     *  가지고 있다면 true
     */
    public boolean haveTitle(Title title, Player player) {
        TitleInventory inv = user.gettInv(player.getUniqueId());
        return inv.getEquipTitle().containsValue(title.getUnique()) || inv.getHaveTitles().contains(title.getUnique());
    }


    public void giveTitle(Title title, Player player) {
        TitleInventory inv = user.gettInv(player.getUniqueId());
        inv.addTitle(title.getUnique());
        fileCore.getUserFile().save(player.getUniqueId());
    }

    public List<Title> getTitles(Player player) {
        TitleInventory inv = user.gettInv(player.getUniqueId());
        List<Title> list = new ArrayList<>();

        List<String> titles = inv.getHaveTitles();
        titles.forEach(title-> list.add(getTitle(title)));
        return list;
    }

    /**
     * @param player player
     * @param title title
     * @description
     * b = 장착 중인 거라면 해체 ( 이미 장착충이라면 true )
     * false 는 다른거 착용중
     */
    public void equip(Player player, Title title) {
        TitleInventory inv = user.gettInv(player.getUniqueId());
        boolean b = inv.getEquip(title.getColNum()) != null && inv.getEquip(title.getColNum()).equals(title.getUnique());
        if ( b ){
            inv.unEquipTitle(title.getColNum());
        }else{
            inv.addEquipTitle(title.getColNum(), title.getUnique());
        }

        fileCore.getUserFile().save(player.getUniqueId());
    }


    public void replaceCol(String unique) {
        user.gettInv().forEach((key, inventory) -> {
            int i = 1;
            for (String value : inventory.getEquipTitle().values()) {
                if (value.equals(unique)) {
                    inventory.unEquipTitle(i);
                }
                i++;
            }
        });

    }

    public void remove(String unique) {
        user.gettInv().forEach((key, inventory) -> inventory.remove(unique));
        fileCore.getDataFile().remove(unique);
        fileCore.getUserFile().remove(unique);
    }

    public void removeTitle(Player target, String unique) {
        user.gettInv(target.getUniqueId()).remove(unique);
        fileCore.getUserFile().save(target.getUniqueId());
    }

    /**
     * @description 서버에 접속할때 삭제된 칭호를 착용하고 있거나 보유하고 있을 경우 삭제
     * @see org.nano.nstitle.listener.PlayerIO io
     * @param p target
     */
    public void compare(Player p) {
        user.gettInv(p.getUniqueId()).getEquipTitle().values()
                .stream()
                .filter(this::isTitleDeleted)
                .forEach(title -> {
                    removeTitle(p,title);
                });

        user.gettInv(p.getUniqueId()).getHaveTitles()
                .stream()
                .filter(this::isTitleDeleted)
                .forEach(title -> {
                    removeTitle(p,title);
                });
        fileCore.getUserFile().save(p.getUniqueId());
    }
    private boolean isTitleDeleted(String unique) {
        Title title = getTitle(unique);
        return !registry.getTitles().contains(title);
    }
}

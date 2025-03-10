package org.nano.nstitle.data.registy.user.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitleInventory {
    private Map<Integer,String> equipTitle;
    private List<String> haveTitles;

    public TitleInventory() {
        this.equipTitle = new HashMap<>();
        this.haveTitles = new ArrayList<>();
    }

    public Map<Integer, String> getEquipTitle() {
        return equipTitle;
    }
    public String getEquip(int col){
        return equipTitle.get(col);
    }
    public void setEquipTitle(Map<Integer, String> equipTitle) {
        this.equipTitle = equipTitle;
    }

    public void addEquipTitle(int col, String unique){
        this.equipTitle.put(col,unique);
    }

    public void unEquipTitle(int col){
        this.equipTitle.remove(col);
    }

    public List<String> getHaveTitles() {
        return haveTitles;
    }

    public void setHaveTitles(List<String> haveTitles) {
        this.haveTitles = haveTitles;
    }
    public void addTitle(String title){
        this.haveTitles.add(title);
    }

    public void remove(String unique) {
        equipTitle.entrySet().removeIf(entry -> entry.getValue().equals(unique));
        haveTitles.remove(unique);
    }
}

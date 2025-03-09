package org.nano.nstitle.util.builder;

import org.nano.nstitle.data.db.FileCore;
import org.nano.nstitle.data.db.file.DataFile;
import org.nano.nstitle.data.registy.RegistryCore;
import org.nano.nstitle.data.registy.server.TitleRegistry;
import org.nano.nstitle.data.registy.server.dto.Color;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.util.key.ColorKeys;
import org.nano.nstitle.util.key.LoreKeys;

import java.util.ArrayList;
import java.util.List;

public class TitleBuilder {
    private final TitleRegistry titleRegistry = RegistryCore.getInstance().getTitleRegistry();
    private final DataFile dataFile = FileCore.getInstance().getDataFile();

    public static TitleBuilder builder = new TitleBuilder();

    private Title title;


    public TitleBuilder create(String unique){
        this.title = new Title(
                unique,
                unique,
                List.of("",""),
                1,
                new Color(unique,ColorKeys.NONE,new ArrayList<>())
        );
        return this;
    }

    public TitleBuilder modify(Title title){
        this.title = title;
        return this;
    }

    public TitleBuilder display(String display){
        this.title.setDisplay(display);
        return this;
    }

    public TitleBuilder lore(List<String> lore){
        this.title.setLore(lore);
        return this;
    }
    public TitleBuilder lore_modify(LoreKeys type, int line, String str) {
        List<String> lore = new ArrayList<>(title.getLore()); // 기존 로어 복사

        switch (type) {
            case ADD -> {
                while (lore.size() <= line) {
                    lore.add("");
                }
                lore.set(line, str);
            }
            case REMOVE -> {
                if (line >= 0 && line < lore.size()) {
                    lore.remove(line);
                }
            }
            case CLEAR -> lore.clear();
        }

        title.setLore(lore);
        return this;
    }

    public TitleBuilder lore_modify(String str) {
        List<String> lore = new ArrayList<>(title.getLore()); // 기존 로어 복사
        lore.add(str);
        title.setLore(lore);
        return this;
    }

    public TitleBuilder col(int colNum){
        if ( colNum > 3 ) colNum = 1;
        this.title.setColNum(colNum);
        return this;
    }

    public TitleBuilder color(Color color){
        this.title.setColor(color);
        return this;
    }


    public void build(){
        titleRegistry.addTitle(this.title);
        dataFile.save(this.title);
    }

    public void remove(){
        titleRegistry.removeTitle(this.title);
    }

}

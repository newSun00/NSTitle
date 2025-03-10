package org.nano.nstitle.data.registy.server;

import org.nano.nstitle.data.registy.server.dto.Title;

import java.util.ArrayList;
import java.util.List;

public class TitleRegistry {
    private final List<Title> titles;

    public TitleRegistry() {
        titles = new ArrayList<>();
    }

    public List<Title> getTitles() {
        return titles;
    }

    public void addTitle(Title param) {
        titles.stream()
                .filter(title -> title.getUnique().equals(param.getUnique()))
                .findFirst()
                .ifPresent(title->titles.remove(title));
        titles.add(param);
    }

    public void removeTitle(Title param){
        titles.remove(param);
    }

}

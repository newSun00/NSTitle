package org.nano.nstitle.data.registy.server.dto;

import java.util.List;

public class Title {
    /**
     * @description unique / 고유 이름
     * @description display / 표기 이름
     * @description lore / 표기 설명
     * @description colNum / 칭호 적용 라인
     * @description Color / 색깔
     *
     */
    private final String unique;
    private String display;
    private List<String> lore;
    private int colNum;
    private Color color;

    public Title(String unique, String display, List<String> lore, int colNum, Color color) {
        this.unique = unique;
        this.display = display;
        this.lore = lore;
        this.colNum = colNum;
        this.color = color;
    }

    public String getUnique() {
        return unique;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

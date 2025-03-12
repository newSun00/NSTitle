package org.nano.nstitle.data.registy.server.dto;

import org.nano.nstitle.util.factory.ColorFactory;
import org.nano.nstitle.util.key.ColorKeys;

import java.util.List;

public class Color{
    private String display;

    private ColorKeys colorKeys;
    private List<String> hex;

    public Color(String display, ColorKeys colorKeys, List<String> hex) {
        this.display = display;
        this.colorKeys = colorKeys;
        this.hex = hex;
        build();
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public ColorKeys getColorKeys() {
        return colorKeys;
    }

    public void setColorKeys(ColorKeys colorKeys) {
        this.colorKeys = colorKeys;
    }

    public List<String> getHex() {
        return hex;
    }

    public void setHex(List<String> hex) {
        this.hex = hex;
    }

    public void build(){
        display = ColorFactory.factory
                .root(display)
                .special(colorKeys,hex)
                .doneD();
    }
}

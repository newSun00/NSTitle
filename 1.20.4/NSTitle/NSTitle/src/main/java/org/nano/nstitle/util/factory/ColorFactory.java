package org.nano.nstitle.util.factory;

import net.kyori.adventure.text.Component;
import org.nano.nstitle.util.helper.color.ColorHelper;
import org.nano.nstitle.util.key.ColorKeys;

import java.util.List;

public class ColorFactory {
    /**
     * @description 색 포맷
     *
     */
    public static ColorFactory factory = new ColorFactory();
    private final ColorHelper helper = new ColorHelper();

    private String display;
    private Component component;
    private List<Component> components;

    public ColorFactory color(String string){
        component = helper.convert(string);
        return this;
    }
    public ColorFactory color(List<String> string){
        components = helper.convert(string);
        return this;
    }

    public String doneD(){
        return display;
    }
    public Component doneC(){
        return component;
    }

    public ColorFactory root(String display) {
        this.display = display;
        return this;
    }

    public ColorFactory special(ColorKeys keys, List<String> hex){
        if (keys == ColorKeys.GRADIENT) {
            display = display.replaceAll("(?i)<color:#([0-9A-F]{6})>", "");
            display = display.replaceAll("(?i)&[0-9A-FK-OR]", "");

            StringBuilder gradientBuilder = new StringBuilder("<gradient:");

            for (String color : hex) {
                gradientBuilder.append(color).append(":");
            }
            gradientBuilder.append("#phase-mm-g#>").append(display).append("</gradient>");

            display = gradientBuilder.toString();
        }
        else if (keys == ColorKeys.RAINBOW) {
            display = display.replaceAll("(?i)<color:#([0-9A-F]{6})>", "");
            display = display.replaceAll("(?i)&[0-9A-FK-OR]", "");
            display = "<rainbow:#phase-mm#>" + display + "</rainbow>";
        }else {
            display = helper.hex(display);
        }
        return this;
    }

    public List<Component> doneCL() {
        return components;
    }
}

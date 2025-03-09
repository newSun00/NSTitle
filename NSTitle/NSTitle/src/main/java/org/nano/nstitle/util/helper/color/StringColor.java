package org.nano.nstitle.util.helper.color;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public class StringColor {
    private final String message;
    private final String color;
    List<TextDecoration> decorations;

    public StringColor(String message, String color, List<TextDecoration> decorations) {
        this.message = message;
        this.color = color;
        this.decorations = decorations;
    }
    public Component build(){
        Component text = Component.text(message, TextColor.fromHexString(color)).decoration(TextDecoration.ITALIC,false);
        if ( decorations != null && !decorations.isEmpty()){
            for ( TextDecoration textDecoration : decorations){
                text = text.decoration(textDecoration,true);
            }
        }
        return text;
    }
}

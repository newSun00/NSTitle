package org.nano.nstitle.manager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.util.builder.ItemBuilder;
import org.nano.nstitle.util.key.ColorKeys;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public ItemStack getMold(Material material){
        return ItemBuilder.builder
                .create(material)
                .display("")
                .model(1)
                .build();
    }
    public ItemStack getInfo(Title title){
        return ItemBuilder.builder // 4
                .create(Material.ENCHANTED_BOOK)
                .display(title.getDisplay())
                .lore(title.getLore())
                .model(1)
                .build();
    }
    public ItemStack getName(Title title, Material material){
        return ItemBuilder.builder // 20
                .create(material)
                .display("<color:#5DE56D>이름 변경")
                .lore(List.of(
                        "<color:#81EBC6> 칭호의 &a표기형 이름<color:#81EBC6>을 변경할 수 있습니다.",
                        "<color:#81EBC6> 16진수 색을 사용하고 싶다면 &a〈color:#hex〉 <color:#81EBC6>을 입력 해주세요.",
                        "",
                        "<color:#81EBC6> 현재 표기형 이름 : "+title.getDisplay()
                ))
                .model(1)
                .build();
    }
    public ItemStack getLore(Title title, Material material){

        List<String> newLore = new ArrayList<>();
        newLore.addAll(
                List.of(
                        "<color:#46E6D2> 칭호의 &a설명<color:#46E6D2>을 변경할 수 있습니다.",
                        "<color:#46E6D2> 16진수 색을 사용하고 싶다면 &a〈color:#hex〉 <color:#46E6D2>을 입력 해주세요.",
                        "<color:#46E6D2> [ 좌클릭 ] :<color:#A3D0E5> 로어를 추가 합니다.",
                        "<color:#46E6D2> [ 우클릭 ] :<color:#A3D0E5> 로어를 삭제 합니다.",
                        "<color:#46E6D2> [ 웅크리기+우클릭 ] :<color:#A3D0E5> 로어를 초기화 합니다.",
                        "",
                        "<color:#46E6D2> 현재 설명 "
        ));
        newLore.addAll(title.getLore());

        return ItemBuilder.builder // 21
                .create(material)
                .display("<color:#83CCE6>설명 변경")
                .lore(newLore)
                .model(1)
                .build();
    }
    public ItemStack getCol(Title title, Material material){
        return ItemBuilder.builder // 22
                .create(material)
                .display("<color:#83CCE6>칭호 번호")
                .lore(List.of(
                        "<color:#81EBC6> 칭호의 &a번호<color:#81EBC6>를 변경할 수 있습니다.",
                        "<color:#81EBC6> 좌클릭시 +1 씩 증가하며 1 ~ 3까지 변경 가능합니다.",
                        "",
                        "<color:#81EBC6> 현재 번호 : &b"+title.getColNum()
                ))
                .model(1)
                .build();
    }
    public ItemStack getColor(Title title, Material material){

        ColorKeys key =  title.getColor().getColorKeys();
        List<String> newLore = new ArrayList<>();
        newLore.add("<color:#F4E699>칭호의 특수한 색깔을 넣거나 제거할 수 있습니다.");
        newLore.add("<color:#F4E699>특수한 색은 <color:#EBD86C>GRADIENT &7| <color:#E998CB>RAINBOW &7| &fNONE <color:#F4E699>이 있습니다.");
        newLore.add("");
        switch (key){
            case GRADIENT -> {
                newLore.add("<color:#83CCE6>현재 내 색상 : <color:#EBD86C>GRADIENT");
                newLore.add("<color:#EAEAB4>이 색상은 3개의 HEX 코드를 적어서 사용합니다.");
                newLore.add("<color:#EAEAB4>오른쪽 아이템칸의 특수색상 색깔을 입력해주세요.");
            }
            case RAINBOW -> {
                newLore.add("<color:#83CCE6>현재 내 색상 : <color:#E998CB>RAINBOW");
                newLore.add("<color:#83CCE6>이 색상 3개의 HEX 를 적용할 필요가 없이 작동합니다.");
            }
            case NONE -> {
                newLore.add("<color:#83CCE6>현재 내 색상 : &fNONE");
                newLore.add("<color:#83CCE6>이 색상은 특수색을 사용하지 않는다는 의미입니다.");
            }
        }

        return ItemBuilder.builder // 23
                .create(material)
                .display("<color:#83CCE6>특수 색상")
                .lore(newLore)
                .model(1)
                .build();
    }

    public ItemStack getHex(Title title, Material material){

        List<String> newLore = new ArrayList<>();
        newLore.add("<color:#83CCE6>특정 특수한 색을 이용하기 위해 작성해야할 곳 입니다.");
        newLore.add("<color:#83CCE6>특수한 색은 <color:#EBD86C>GRADIENT 입니다.");
        newLore.add("");
        if ( title.getColor().getHex().isEmpty()){
            newLore.add("<color:#E6C7B7> 현재 지정된 색상이 아무것도 없습니다.");
        }else{
            for ( String hex : title.getColor().getHex() ){
                newLore.add("<color:#E6C7B7> Hex : <color:"+hex+">||||||||||");
            }
        }

        return ItemBuilder.builder // 23
                .create(material)
                .display("<color:#83CCE6>특수 색상")
                .lore(newLore)
                .model(1)
                .build();
    }

}

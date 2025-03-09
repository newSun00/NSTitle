package org.nano.nstitle.controller;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.nano.nstitle.data.registy.server.dto.Color;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.manager.PlayerManager;
import org.nano.nstitle.manager.TitleManager;
import org.nano.nstitle.util.builder.TitleBuilder;
import org.nano.nstitle.util.key.ColorKeys;
import org.nano.nstitle.util.key.LoreKeys;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.nano.nstitle.util.message.Message.message;

public class TitleController {
    private final TitleManager titleManager = new TitleManager();
    private final PlayerManager playerManager = new PlayerManager();

    public void commandListOp(Player sender) {
        String prefix = " <color:#CCAAFA>/tt ";

        String unique = " <color:#FAE1BB>[unique] ";
        String set = "<color:#C0F7FC>set";

        String pl = "<color:#5FDFFA><playername>";

        message(sender,"");
        message(sender,"&b   Title Plugin Made By &6New_Sun#1933 &c(OP)");
        message(sender,prefix+"<color:#D38CFA>create"+unique);
        message(sender,prefix+set+unique+"<color:#C5FABD>"+"name");

        message(sender,prefix+set+unique+"<color:#C5FABD>"+"lore add [-s]");
        message(sender,prefix+set+unique+"<color:#C5FABD>"+"lore add <line> [-s]");
        message(sender,prefix+set+unique+"<color:#C5FABD>"+"lore remove <line>");
        message(sender,prefix+set+unique+"<color:#C5FABD>"+"lore clear");

        message(sender,prefix+set+unique+"<color:#C5FABD>"+"col <1,2,3>");
        message(sender,prefix+set+unique+"<color:#C5FABD>"+"special-color <rainbow, none> ");
        message(sender,prefix+set+unique+"<color:#C5FABD>"+"special-color <gradient> <hex> <hex> <hex>");

        message(sender,prefix+"<color:#D38CFA>give "+pl+unique);
        message(sender,prefix+"<color:#D38CFA>remove "+pl+unique);

    }
    public void commandListUser(Player sender) {
        String prefix = " <color:#CCAAFA>/tt ";

        String unique = " <color:#FAE1BB>[unique]";
        String set = "<color:#C0F7FC>apply";

        message(sender,"");
        message(sender,"&b   Title Plugin Made By &6New_Sun#1933 &a(USER)");
        message(sender,prefix+"<color:#D38CFA>list");
        message(sender,prefix+set+unique);

    }
    /**
     *
     * @param unique / uniqueName
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     * 1. 같은 이름 유효성 검사
     */
    public void create(Player sender, String unique){
        if (titleManager.hasTitle(unique)){
            message(sender,"");
            message(sender,"&c 오류 〉 이미 존재하는 칭호 입니다.");
            message(sender,"&c 오류 〉 다른 칭호명을 사용해주세요.");
            return;
        }

        TitleBuilder.builder
                .create(unique)
                .build();

        message(sender,"");
        message(sender,"&b   성공적으로 &6"+unique+"&a 칭호가 생성 되었습니다!");
        message(sender,"");
    }

    /**
     *
     * @param display / uniqueName
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     * 1. 존재하는 Title 확인
     */
    public void display(Player sender, String unique, String display){
        try {
            Title title = titleManager.getTitle(unique);
            Color color = title.getColor();
            color.setDisplay(display);
            color.build();

            TitleBuilder.builder
                    .modify(title)
                    .display(display)
                    .build();

            message(sender,"");
            message(sender,"&a 성공!");
            message(sender,"&b   수정 : "+unique);
            message(sender,"&b   이름 : "+display);
        } catch ( NullPointerException e ){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호입니다.");
            message(sender,"&c 오류 〉 칭호를 생성 후 이름을 설정해주세요.");
        }
    }
    /**
     *
     * @param key ───────┐
     *     @see LoreKeys keys
     * @param dec / 설명 1줄 / 그냥 추가라면 -99 
     * @param line / 로어 번호
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     * 1. 존재하는 Title 확인
     */
    public void lore(Player sender,String unique, LoreKeys key, String dec, int line){
        try {
            if ( line > 0 ){
                TitleBuilder.builder
                        .modify(titleManager.getTitle(unique))
                        .lore_modify(key,line, dec)
                        .build();
            }
            else {
                TitleBuilder.builder
                        .modify(titleManager.getTitle(unique))
                        .lore_modify(dec)
                        .build();
            }
            message(sender,"");
            message(sender,"&a 성공!");
            message(sender,"&b   수정 : "+unique);

            if (line != -99) {
                message(sender, "&b   줄 번호 : " + line);
            } else {
                message(sender, "&b   줄 번호 : " + "X");
            }
            
            message(sender,"&b   줄 로어 : "+dec);
            message(sender,"");
        } catch ( NullPointerException e ){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호이거나, 로어 타입이 잘못되었습니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 ADD, REMOVE 를 정확하게 입력해주세요.");
        }
    }
    /**
     *
     * @param number / 컬럼 번호 1 ~ 3 만 지원
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     * 1. 존재하는 Title 확인
     */
    public void col(Player sender, String unique, int number){
        try {
            if ( number < 0 || number > 3 ) throw new NullPointerException();

            TitleBuilder.builder
                    .modify(titleManager.getTitle(unique))
                    .col(number)
                    .build();

            titleManager.replaceCol(unique);

            // 유저가 장착하고 있다면 그것도 업데이트 시켜줘야함;;

            message(sender,"");
            message(sender,"&a 성공!");
            message(sender,"&b   수정 : "+unique);
            message(sender,"&b   번호 : "+number);
            message(sender,"");
        } catch ( NullPointerException e ){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호이거나, 컬럼 번호가 잘못되었습니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 1 ~ 3까지의 컬럼 번호만 지정 가능합니다.");
        }
    }
    /**
     *
     * @param sc ───────────┐
     *     @see ColorKeys keys
     * @param hex / hexCode ---------
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     * 1. 존재하는 Title 확인
     */
    public void color(Player sender, String unique, ColorKeys sc, List<String> hex){
        try {
            Title title = titleManager.getTitle(unique);
            Color color = new Color(title.getDisplay(),sc,hex);

            TitleBuilder.builder
                    .modify(title)
                    .color(color)
                    .build();

            message(sender,"");
            message(sender,"&a 성공!");
            message(sender,"&b   수정 : "+unique);
            message(sender,"&b   칭호를 착용해야 보입니다.");
            message(sender,"");
        } catch ( NullPointerException e ){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호이거나, 특수 색상 키가 잘못되었습니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 Gradient, Rainbow, None 중 선택해주세요!");
            message(sender,"");
        }
    }
    /**
     *
     * @param unique / uniqueName
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     * 1. 존재하는 Title 확인
     */
    public void delete(Player sender, String unique){
        try {

            TitleBuilder.builder
                    .modify(titleManager.getTitle(unique))
                    .remove();

            titleManager.remove(unique);

            message(sender,"");
            message(sender, "&b   성공적으로 &6" + unique + "&a 칭호가 삭제 되었습니다!");
            message(sender,"");
        }catch (NullPointerException e){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호입니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
        }
    }
    public void delete(ConsoleCommandSender sender, String unique){
        try {

            TitleBuilder.builder
                    .modify(titleManager.getTitle(unique))
                    .remove();

            titleManager.remove(unique);

            message(sender,"");
            message(sender, "&b   성공적으로 &6" + unique + "&a 칭호가 삭제 되었습니다!");
            message(sender,"");
        }catch (NullPointerException e){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호입니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
        }
    }
    /**
     *
     * @param tName / target Name
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     * 1. 존재하는 Title 확인
     */
    public void giveTitleByPlayer(Player sender, String tName, String unique){
        try {

            Title title = titleManager.getTitle(unique);
            Player target = playerManager.getTarget(tName);

            boolean b = titleManager.haveTitle(title,target);
            if( !b ){
                titleManager.giveTitle(title,target);
                message(sender,"");
                message(sender, "&b   성공적으로 &7"+tName+"&7 님에게 "+title.getDisplay()+"&7 칭호를 주었습니다!");
                message(sender,"");

                message(sender,"");
                message(target, "&c 서버 관리자 &a"+sender.getName()+"&7 님이 "+title.getDisplay()+"&7 칭호를 주었습니다!");
                message(sender,"");
            }else{
                message(sender,"&c 오류 〉 플레이어에게 칭호를 주려고 했으나 칭호를 이미 가지고 있습니다.");
            }
        }catch (NullPointerException e){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호거나 존재하지 않는 플레이어 입니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 플레이어가 서버에 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 프록시일 경우 서버가 같아야 합니다.");
        }
    }
    public void giveTitleByPlayer(ConsoleCommandSender sender, String tName, String unique){
        try {

            Title title = titleManager.getTitle(unique);
            Player target = playerManager.getTarget(tName);

            boolean b = titleManager.haveTitle(title,target);
            if( !b ){
                titleManager.giveTitle(title,target);
                message(sender,"");
                message(sender, "&b   성공적으로 &7"+tName+"&7 님에게 "+title.getDisplay()+"&7 칭호를 주었습니다!");
                message(sender,"");

                message(sender,"");
                message(target, "&c 서버 관리자 &aConsole"+"&7 님이 "+title.getDisplay()+"&7 칭호를 주었습니다!");
                message(sender,"");
            }else{
                message(sender,"&c 오류 〉 플레이어에게 칭호를 주려고 했으나 칭호를 이미 가지고 있습니다.");
            }
        }catch (NullPointerException e){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호거나 존재하지 않는 플레이어 입니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 플레이어가 서버에 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 프록시일 경우 서버가 같아야 합니다.");
        }
    }
    /**
     *
     * @param tName / target Name
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     * 1. 존재하는 Title 확인
     */
    public void removeTitleByPlayer(Player sender, String tName, String unique){
        try {

            Title title = titleManager.getTitle(unique);
            Player target = playerManager.getTarget(tName);

            boolean b = titleManager.haveTitle(title,target);
            if( b ){
                titleManager.removeTitle(target,unique);
                message(sender,"");
                message(sender, "&b   성공적으로 &7"+tName+"&7 님에게 "+title.getDisplay()+"&7 칭호를 삭제했습니다!");

                message(sender,"");
                message(target, "&c 서버 관리자 &a"+sender.getName()+"&7 님이 "+title.getDisplay()+"&7 칭호를 삭제했습니다");
                message(sender,"");
            }else{
                message(sender,"&c 오류 〉 플레이어에게 칭호를 삭제하려고 했으나 해당 플레이어가 칭호를 가지고 있지않습니다.");
            }
        }catch (NullPointerException e){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호거나 존재하지 않는 플레이어 입니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 플레이어가 서버에 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 프록시일 경우 서버가 같아야 합니다.");
        }
    }
    public void removeTitleByPlayer(ConsoleCommandSender sender, String tName, String unique){
        try {

            Title title = titleManager.getTitle(unique);
            Player target = playerManager.getTarget(tName);

            boolean b = titleManager.haveTitle(title,target);
            if( b ){
                titleManager.removeTitle(target,unique);
                message(sender,"");
                message(sender, "&b   성공적으로 &7"+tName+"&7 님에게 "+title.getDisplay()+"&7 칭호를 삭제했습니다!");

                message(sender,"");
                message(target, "&c 서버 관리자 &aConsole"+"&7 님이 "+title.getDisplay()+"&7 칭호를 삭제했습니다");
                message(sender,"");
            }else{
                message(sender,"&c 오류 〉 플레이어에게 칭호를 삭제하려고 했으나 해당 플레이어가 칭호를 가지고 있지않습니다.");
            }
        }catch (NullPointerException e){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호거나 존재하지 않는 플레이어 입니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 플레이어가 서버에 존재하는 지 확인해주세요.");
            message(sender,"&c 오류 〉 프록시일 경우 서버가 같아야 합니다.");
        }
    }
    /**
     *
     * @param tName / target Name
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     *
     */
    public void list(Player sender, String tName){
        try {

            Player target = playerManager.getTarget(tName);
            List<Title> titles = titleManager.getTitles(target);
            message(sender,"&n&6                                   ");
            if ( !titles.isEmpty()){
                AtomicInteger i = new AtomicInteger();
                titles.forEach(title->{
                    message(sender,i+"&7) "+title.getUnique()+" | "+title.getDisplay());
                    i.getAndIncrement();
                });
                message(sender,"");
            }else{
                message(sender,"&a 가지고 있는 칭호가 없습니다.");
            }
            message(sender,"&n&6                                   ");

        }catch (NullPointerException e){
            message(sender,"");
            message(sender,"&c 오류 〉 알 수 없는 오류 입니다.");
            message(sender,"");
        }
    }
    /**
     *
     * @param tName / target Name
     * @param string / string - unique / int number
     * @throws NullPointerException
     * 지정을 안하고 제작하는 경우 뜸
     * @description
     *
     */
    public void equip(Player sender, String tName, String string) {
        try {

            Player target = playerManager.getTarget(tName);
            Title title = titleManager.getTitle(string);
            if ( titleManager.haveTitle(title,target)){
                titleManager.equip(target,title);
                message(sender,"&n&6                                   ");
                message(sender,"&a 칭호를 착용했습니다! ");
                message(sender,"&a 칭호 : "+title.getDisplay());
                message(sender,"");
            }else{
                message(sender,"");
                message(sender,"&c 오류 〉 칭호를 보유해야 합니다.");
                message(sender,"");
            }
        }catch (NullPointerException e){
            message(sender,"");
            message(sender,"&c 오류 〉 알 수 없는 오류 입니다.");
            message(sender,"&c 오류 〉 칭호가 존재하는 지 확인해주세요.");
        }
    }
}

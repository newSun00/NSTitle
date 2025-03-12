package org.nano.nstitle.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.controller.TitleController;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.gui.TitleSettingGUI;
import org.nano.nstitle.manager.TitleBookManager;
import org.nano.nstitle.util.builder.TitleBuilder;
import org.nano.nstitle.util.key.ColorKeys;
import org.nano.nstitle.util.key.LoreKeys;
import org.nano.nstitle.util.message.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nano.nstitle.util.message.Message.message;

public class GuiListener implements Listener {
    private final TitleController controller = new TitleController();

    private final Map<Player, String> chat;
    private final Map<Player, Title> titleMap;
    private final Map<Player, Object> obj;

    public GuiListener() {
        chat = new HashMap<>();
        titleMap = new HashMap<>();
        obj = new HashMap<>();
    }

    @EventHandler
    public void clickGUI(InventoryClickEvent e){
        if ( e.getInventory().getHolder() instanceof TitleSettingGUI gui){
            if ( e.getCurrentItem() == null ) return;
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            int slot = e.getRawSlot();
            Title title = gui.getTitle();

            switch (slot){
                case 4->{
                    // giveBook
                    TitleBookManager titleBookManager = new TitleBookManager();
                    titleBookManager.giveBook(p,title,1);
                }
                case 20->{
                    // setName
                    p.closeInventory();
                    message(p,"");
                    message(p,"<color:#A5EBAA> 채팅창에 칭호의 이름을 적어주세요! ");
                    message(p,"<color:#A5EBAA> 이름을 입력하고 (Enter) 하여 완료해주세요! ");
                    message(p,"<color:#EB5A4E> 취소하고 싶다면 <color:#EBA75A>cancel<color:#EB5A4E> 을 입력하세요!");
                    chat.put(p,"name");
                    titleMap.put(p,title);
                }
                case 21->{
                    /*
                      @description
                     * - 좌클릭 : 추가 | 줄번호 입력 -> 내용 입력
                     * - 우클릭 : 삭제 | 줄번호 입력
                     * - 쉬우클 : 전삭 : 확인 텍스트 입력
                     */
                    switch (e.getClick()){
                        case LEFT -> {
                            p.closeInventory();
                            message(p,"");
                            message(p,"<color:#59F566> 채팅창에 수정할 로어의 줄 번호를 입력해주세요! ");
                            message(p,"<color:#59F566> 줄 번호입력 없이 내용만 추가할 거라면 <color:#97DBF5>(숫자가 아닌 아무키) <color:#59F566> 를 입력해주세요!");
                            message(p,"<color:#59F566> 취소하고 싶다면 <color:#EBA75A>cancel<color:#59F566> 을 입력하세요!");
                            chat.put(p,"lore-add");
                            titleMap.put(p,title);
                        }
                        case RIGHT -> {
                            p.closeInventory();
                            message(p,"");
                            message(p,"<color:#59F566> 채팅창에 삭제할 로어의 줄 번호를 입력해주세요! ");
                            message(p,"<color:#59F566> 줄 번호입력 없이 마지막 내용만 삭제할 거라면 <color:#97DBF5>(숫자가 아닌 아무키) <color:#59F566> 를 입력해주세요!");
                            message(p,"<color:#59F566> 취소하고 싶다면 <color:#EBA75A>cancel<color:#59F566> 을 입력하세요!");
                            chat.put(p,"lore-remove");
                            titleMap.put(p,title);
                        }
                        case SHIFT_RIGHT -> {
                            p.closeInventory();
                            message(p,"");
                            message(p,"<color:#59F566> 로어의 전체삭제를 확인하는 메세지 입니다! ");
                            message(p,"<color:#59F566> 로어 전체삭제가 맞다면 <color:#97DBF5>ok <color:#59F566> 를 입력해주세요!");
                            message(p,"<color:#59F566> 취소하고 싶다면 <color:#EBA75A>cancel<color:#59F566> 을 입력하세요!");
                            chat.put(p,"lore-clear");
                            titleMap.put(p,title);
                        }
                    }
                }
                case 22->{
                    // setCol
                    int num = title.getColNum();
                    num++;
                    if ( num > 3) num = 1;
                    controller.col(p,title.getUnique(),num);
                    openGUI(p,title);

                }
                case 23->{
                    // setColor
                    List<ColorKeys> keys = List.of(ColorKeys.NONE,ColorKeys.GRADIENT,ColorKeys.RAINBOW);
                    int index = keys.indexOf(title.getColor().getColorKeys());
                    index++;
                    if ( index == 3 ) index = 0;
                    ColorKeys c = keys.get(index);
                    controller.color(p,title.getUnique(),c,title.getColor().getHex());
                    openGUI(p,title);
                }
                case 24->{
                    // setHex
                    if ( title.getColor().getColorKeys() == ColorKeys.GRADIENT ) {
                        p.closeInventory();
                        message(p, "");
                        message(p, "<color:#A5EBAA> 채팅창에 HEX 색 3개를 아래와 같이 입력하세요!");
                        message(p, "<color:#A5EBAA> EX><color:#EAAD79>#DDEA8B<color:#48EB45>-<color:#EBB365>#52EB7D<color:#48EB45>-<color:#EBDE4D>#CEEBB7");
                        message(p, "<color:#EB5A4E> 취소하고 싶다면 <color:#EBA75A>cancel<color:#EB5A4E> 을 입력하세요!");
                        chat.put(p, "hex");
                        titleMap.put(p,title);
                    }
                }

            }

        }
    }
    @EventHandler
    public void chat(AsyncChatEvent e){
        if ( chat.containsKey(e.getPlayer())){
            e.setCancelled(true);
            Player p = e.getPlayer();
            String str = PlainTextComponentSerializer.plainText().serialize(e.originalMessage());
            if ( str.equalsIgnoreCase("cancel")){
                openGUI(p,titleMap.get(p));
                chat.remove(p);
                titleMap.remove(p);
                return;
            }

            String type = chat.get(p);
            Title title = titleMap.get(p);
            chat.remove(p);
            titleMap.remove(p);

            switch (type){
                case "name"->{
                    TitleBuilder.builder
                            .modify(title)
                            .display(str)
                            .build();

                    openGUI(p,title);
                }
                case "lore-add"->{
                    try{
                        int line = Integer.parseInt(str);

                        String description = title.getLore().size() <= line ? "내용 없음" : title.getLore().get(line);

                        Message.clear(p);

                        message(p,"<color:#59F566> 다음과 같은 정보를 수정하려고 합니다");
                        message(p,"<color:#59F566> 줄 번호 : &f"+line);
                        message(p,"<color:#59F566> 줄 내용 : &f"+description);
                        message(p,"<color:#59F566> 맞다면 채팅창에 바뀔 내용을 입력해주세요!");
                        message(p,"<color:#59F566> 취소하고 싶다면 <color:#EBA75A>cancel<color:#59F566> 을 입력하세요!");
                        chat.put(p,"next-lore-add");
                        titleMap.put(p,title);
                        obj.put(p,line);

                    }catch (NumberFormatException e2){

                        Message.clear(p);

                        message(p,"<color:#59F566> 번호 수정없이 내용을 추가하려고 합니다,");
                        message(p,"<color:#59F566> 맞다면 채팅창에 추가할 내용을 입력해주세요!");
                        message(p,"<color:#59F566> 취소하고 싶다면 <color:#EBA75A>cancel<color:#59F566> 을 입력하세요!");
                        chat.put(p,"next-lore-add");
                        titleMap.put(p,title);

                    }
                }
                case "next-lore-add"->{
                    if ( obj.containsKey(p) ){
                        int line = (int) obj.get(p);
                        TitleBuilder.builder
                                .modify(title)
                                .lore_modify(LoreKeys.ADD, line, str).build();

                    }else {
                        TitleBuilder.builder
                                .modify(title)
                                .lore_modify(LoreKeys.ADD, str).build();
                    }
                    openGUI(p,title);
                }
                case "lore-remove"->{
                    try{
                        int line = Integer.parseInt(str);

                        String description = title.getLore().size() <= line ? "throw" : title.getLore().get(line);
                        if ( description.equals("throw")) throw new NumberFormatException();

                        Message.clear(p);
                        message(p,"<color:#59F566> 다음과 같은 정보를 삭제하려고 합니다");
                        message(p,"<color:#59F566> 줄 번호 : &f"+line);
                        message(p,"<color:#59F566> 줄 내용 : &f"+description);
                        message(p,"<color:#59F566> 확인 되었다면 <color:#EBA75A>ok<color:#59F566> 을 입력하세요!");
                        message(p,"<color:#59F566> 취소하고 싶다면 <color:#EBA75A>cancel<color:#59F566> 을 입력하세요!");
                        chat.put(p,"lore-remove-ok");
                        titleMap.put(p,title);
                        obj.put(p,line);

                    }catch (NumberFormatException e2){
                        Message.clear(p);
                        message(p,"<color:#59F566> 마지막 로어를 삭제했습니다.");

                        TitleBuilder.builder
                                .modify(title)
                                .lore_modify(LoreKeys.REMOVE,"").build();
                        openGUI(p,title);
                    }
                }
                case "lore-remove-ok"->{
                    Message.clear(p);
                    message(p,"<color:#59F566> 특정 번호의 로어를 삭제했습니다!");

                    TitleBuilder.builder
                            .modify(title)
                            .lore_modify(LoreKeys.REMOVE, (Integer) obj.get(p),"")
                            .build();

                    openGUI(p,title);
                }
                case "lore-clear"->{
                    if ( str.equalsIgnoreCase("ok")){
                        TitleBuilder.builder
                                .modify(title)
                                .lore_modify(LoreKeys.CLEAR,"")
                                .build();
                    }
                    openGUI(p,title);
                }
                case "hex"->{
                    List<String> list = Arrays.asList(str.split("-"));
                    if ( list.size() != 3 ) {
                        Message.clear(p);
                        message(p,"<color:#59F566> HEX 를 인식하지 못했습니다.");
                        message(p,"<color:#59F566> 다시 시도해주세요.");
                    }else{
                        title.getColor().setHex(list);
                    }
                    openGUI(p,title);
                }
            }
        }
    }
    private void openGUI(Player p,Title title){
        Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class),()->{
            new TitleSettingGUI(title).open(p);
        });
    }
}


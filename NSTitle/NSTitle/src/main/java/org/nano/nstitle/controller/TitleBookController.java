package org.nano.nstitle.controller;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.manager.TitleBookManager;
import org.nano.nstitle.manager.TitleManager;

import static org.nano.nstitle.util.message.Message.message;

public class TitleBookController {
    private final TitleBookManager manager = new TitleBookManager();
    private final TitleManager tm = new TitleManager();
    
    public void command(Player sender) {
        String prefix = " <color:#CCAAFA>/ttb ";

        String unique = " <color:#FAE1BB>[unique] ";
        String amount = "<color:#C0F7FC>[amount]";

        message(sender,"");
        message(sender,"&b   Title Plugin Made By &6New_Sun#1933 &c(OP)");
        message(sender,prefix+"<color:#D38CFA>give"+unique+amount);
    }

    public void giveBook(Player sender, String unique, int amount) {
        if (tm.hasTitle(unique)){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호 입니다.");
            message(sender,"&c 오류 〉 다른 칭호명을 사용해주세요.");
            return;
        }
        
        if ( amount <= 0 ){
            message(sender,"");
            message(sender,"&c 오류 〉 갯수는 1 이상의 숫자만 가능 합니다.");
            return;
        }
        Title title = tm.getTitle(unique);
        
        manager.giveBook(sender,title,amount);
        message(sender,"");
        message(sender,"&a   성공적으로 칭호북이 생성되었습니다!");
        message(sender,"&6   칭호북 : "+unique);
        message(sender,"&6   갯 수 : "+amount);
        message(sender,"");
    }

    /**
     *
     * @param sender player
     * @param unique title unique
     * @description
     *  - 1. 존재하는 칭호인지 확인
     *  - 2. 이미 보유하고 있는 칭호인지 확인
     *  - 3.
     */
    public void clickTitleBook(ItemStack book, Player sender, String unique) {
        if (tm.hasTitle(unique)){
            message(sender,"");
            message(sender,"&c 오류 〉 존재하지 않는 칭호 입니다.");
            message(sender,"&c 오류 〉 관리자에게 문의하세요.");
            return;
        }
        Title title = tm.getTitle(unique);
        if ( tm.haveTitle(title,sender) ){
            message(sender,"");
            message(sender,"&c 오류 〉 이미 보유하고 있는 칭호 입니다.");
            return;
        }

        book.setAmount(book.getAmount()-1);
        tm.giveTitle(title,sender);
        message(sender,"");
        message(sender,"&a   칭호를 획득하였습니다!");
        message(sender,"&6   칭호: "+title.getDisplay());
        message(sender,"");
    }
}

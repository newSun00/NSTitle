package org.nano.nstitle.data.db.file;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.data.db.FileCore;
import org.nano.nstitle.data.db.SuperFile;
import org.nano.nstitle.data.registy.RegistryCore;
import org.nano.nstitle.data.registy.server.TitleRegistry;
import org.nano.nstitle.data.registy.server.dto.Color;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.util.key.ColorKeys;
import org.nano.nstitle.util.key.DataKeys;

import java.io.File;
import java.util.List;

public class DataFile extends SuperFile {
    /**
     * 1. 데이터베이스를 활성화 하면 파일 저장 X
     * - 오로지 데이터 베이스에 의존함.
     * - 장점 : Proxy 사용 가능
     * - 단점 : 파일 형태는 지원하지 않음

     * 2. 데이터베이스 비활성화 / 파일 저장 O
     * - 오로지 파일에만 의존함
     * - 장점 : 파일로만 처리하기에 데이터의 문제가 없음
     * - 단점 : 프록시가 불가함

     * 오케이 결정
     * 둘중 하나를 선택해서 1개만 사용하게 하는 걸로
     * enable : true 면 MySQL 사용
     * 혹여나 내가 파일로 쓰다가 MySQL 로 넘어가고 싶다면, /tt sql update < 내일하자
     *
     *
     */
    private final TitleRegistry titleRegistry = RegistryCore.getInstance().getTitleRegistry();
    private final FileCore core;

    private FileConfiguration configuration;

    public DataFile(FileCore core) {
        this.core = core;
        load();
    }

    private void setup(String fileName){
        super.folderName = "data/server";
        super.fileName = fileName;
        super.type = DataKeys.DT;

        super.setUp();

        this.configuration = super.configuration;
    }
    public void save(Title title){
        if ( core.getConfig().isEnable()){
            core.getDataSQL().save(title);
        }else{
            remove(title.getUnique());
            Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class),()-> {
                setup(title.getUnique());

                String display = title.getDisplay();
                List<String> lore = title.getLore();
                int colNum = title.getColNum();
                Color color = title.getColor();

                ColorKeys keys = color.getColorKeys();
                List<String> hex = color.getHex();
                String colorDisplay = color.getDisplay();

                String path;
                path = "info";
                configuration.set(path+".Display",display);
                configuration.set(path+".Lore",lore);
                configuration.set(path+".ColNum",colNum);
                configuration.set(path+".Color.Key",keys.name());
                configuration.set(path+".Color.ColorDisplay",colorDisplay);
                configuration.set(path+".Color.Hex",hex);

                super.saveFile();
            });
        }
    }

    public void load() {
        if ( core.getConfig().isEnable()){
            core.getDataSQL().update();
            core.getDataSQL().load();
        }else{
            Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
                File pluginFolder = NSTitle.getProvidingPlugin(NSTitle.class).getDataFolder();
                File folder = new File(pluginFolder, "data/server");
                if (!folder.exists()) {
                    return;
                }

                File[] files = folder.listFiles();
                if (files == null) {
                    return;
                }

                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith("."+DataKeys.DT.name().toLowerCase())) {

                        String fileNameWithoutExtension = file.getName().replace("."+DataKeys.DT.name().toLowerCase(), "");
                        setup(fileNameWithoutExtension);

                        String path = "info";
                        if (configuration.contains(path)) {
                            String display = configuration.getString(path + ".Display");
                            List<String> lore = configuration.getStringList(path + ".Lore");
                            int colNum = configuration.getInt(path + ".ColNum");
                            ColorKeys colorKey = ColorKeys.valueOf(configuration.getString(path + ".Color.Key"));
                            String colorDisplay = configuration.getString(path + ".Color.ColorDisplay");
                            List<String> hex = configuration.getStringList(path + ".Color.Hex");

                            Color color = new Color(colorDisplay, colorKey, hex);

                            Title title = new Title(fileNameWithoutExtension, display, lore, colNum, color);

                            titleRegistry.addTitle(title);
                        }
                    }
                }
            });
        }
    }


    public void remove(String unique){
        if ( core.getConfig().isEnable()) {
            core.getDataSQL().remove(unique);
        }else{
            Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
                setup(unique);
                delete();
            });
        }
    }
}

package org.nano.nstitle.data.db.file;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.data.db.SuperFile;
import org.nano.nstitle.data.db.mysql.DataSQL;
import org.nano.nstitle.data.registy.RegistryCore;
import org.nano.nstitle.data.registy.server.TitleRegistry;
import org.nano.nstitle.data.registy.server.dto.Color;
import org.nano.nstitle.data.registy.server.dto.Title;
import org.nano.nstitle.util.key.ColorKeys;
import org.nano.nstitle.util.key.DataKeys;

import java.io.File;
import java.util.List;

public class DataFile extends SuperFile {
    private final TitleRegistry titleRegistry = RegistryCore.getInstance().getTitleRegistry();
    private final DataSQL sql = new DataSQL();

    private FileConfiguration configuration;

    public DataFile() {
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
        Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class),()-> {
            remove(title.getUnique());
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
        sql.save(title);
    }

    public void load() {
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
        sql.load();
    }


    public void remove(String unique){
        Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class),()-> {
            setup(unique);
            delete();
        });
    }
}

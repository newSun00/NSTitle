package org.nano.nstitle.data.db.file;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.data.db.SuperFile;
import org.nano.nstitle.data.db.mysql.UserSQL;
import org.nano.nstitle.data.registy.RegistryCore;
import org.nano.nstitle.data.registy.user.UserRegistry;
import org.nano.nstitle.data.registy.user.dto.TitleInventory;
import org.nano.nstitle.util.key.DataKeys;

import java.io.File;
import java.util.*;

public class UserFile extends SuperFile {
    private final UserRegistry userRegistry = RegistryCore.getInstance().getUserRegistry();
    private final UserSQL sql = new UserSQL();

    private FileConfiguration configuration;

    private void setup(String uuid){
        super.folderName = "data/user";
        super.fileName = uuid;
        super.type = DataKeys.DT;

        super.setUp();

        this.configuration = super.configuration;
    }
    public void save(UUID uuid) {
        Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            setup(uuid.toString());
            TitleInventory ti = userRegistry.gettInv(uuid);
            Map<Integer, String> equipTitles = ti.getEquipTitle();
            List<String> haveTitles = ti.getHaveTitles();

            String path = "Equip";
            configuration.set(path, null);
            for (Map.Entry<Integer, String> entry : equipTitles.entrySet()) {
                configuration.set(path + "." + entry.getKey(), entry.getValue());
            }

            path = "Have";
            configuration.set(path, haveTitles);

            super.saveFile();
            if ( configuration.getKeys(false).isEmpty()){
                delete();
            }
        });
        sql.save(uuid);
    }

    public void load(UUID uuid) {
        Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            setup(uuid.toString());

            if ( !super.hasFile()) return;

            Map<Integer, String> equipTitles = new HashMap<>();
            List<String> haveTitles = new ArrayList<>();

            String equipPath = "Equip";
            if (configuration.contains(equipPath)) {
                for (String key : Objects.requireNonNull(configuration.getConfigurationSection(equipPath)).getKeys(false)) {
                    int titleId = Integer.parseInt(key);
                    String title = configuration.getString(equipPath + "." + key);
                    equipTitles.put(titleId, title);
                }
            }

            String havePath = "Have";
            if (configuration.contains(havePath)) {
                haveTitles = configuration.getStringList(havePath);
            }

            TitleInventory ti = userRegistry.gettInv(uuid);
            ti.setEquipTitle(equipTitles);
            ti.setHaveTitles(haveTitles);
        });
        sql.load(uuid);
    }

    public void remove(String equip) {
        Bukkit.getScheduler().runTask(NSTitle.getProvidingPlugin(NSTitle.class), () -> {
            File pluginFolder = NSTitle.getProvidingPlugin(NSTitle.class).getDataFolder();
            File folder = new File(pluginFolder, "data/user");

            if (!folder.exists()) {
                return;
            }

            File[] files = folder.listFiles();
            if (files == null) {
                return;
            }

            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(DataKeys.DT.name().toLowerCase())) {
                    String fileNameWithoutExtension = file.getName().replace("." + DataKeys.DT.name().toLowerCase(), "");
                    setup(fileNameWithoutExtension);

                    boolean modified = false;

                    String equipPath = "Equip";
                    String havePath = "Have";

                    List<String> haveTitles = configuration.contains(havePath)
                            ? new ArrayList<>(configuration.getStringList(havePath))
                            : new ArrayList<>();

                    if (configuration.contains(equipPath)) {
                        Set<String> keys = new HashSet<>(Objects.requireNonNull(configuration.getConfigurationSection(equipPath)).getKeys(false));
                        for (String key : keys) {
                            String title = configuration.getString(equipPath + "." + key);

                            if (title != null && (!haveTitles.contains(title) || title.equals(equip))) {

                                configuration.set(equipPath + "." + key, null);
                                modified = true;
                            }
                        }
                    }

                    if (haveTitles.removeIf(title -> title.equals(equip))) {
                        configuration.set(havePath, haveTitles);
                        modified = true;
                    }

                    if (modified) {
                        super.saveFile();
                    }
                }
            }
        });
        sql.remove(equip);
    }

}

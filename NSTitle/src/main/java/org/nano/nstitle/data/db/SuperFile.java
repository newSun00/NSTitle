package org.nano.nstitle.data.db;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nano.nstitle.NSTitle;
import org.nano.nstitle.util.key.DataKeys;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SuperFile {
    protected String folderName = null;
    protected String fileName = null;
    protected DataKeys type;

    protected FileConfiguration configuration;
    protected File file;

    protected void setUp(){
        File pluginFolder = Objects.requireNonNull(NSTitle.getProvidingPlugin(NSTitle.class)).getDataFolder();
        File folder = new File(pluginFolder, folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        file = new File(folder, fileName+"."+type.name().toLowerCase());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }
    protected void delete(){
        File pluginFolder = NSTitle.getProvidingPlugin(NSTitle.class).getDataFolder();
        File folder = new File(pluginFolder, folderName);
        File fileToDelete = new File(folder, fileName + "." + type.name().toLowerCase());
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }

    protected void delete(String folderName, String fileName){
        if (configuration.getKeys(false).isEmpty()) {
            File pluginFolder = NSTitle.getProvidingPlugin(NSTitle.class).getDataFolder();
            File folder = new File(pluginFolder, folderName);
            File fileToDelete = new File(folder, fileName + "." + DataKeys.DT.name().toLowerCase());
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }
        }
    }

    protected boolean hasFile(){
        File pluginFolder = NSTitle.getProvidingPlugin(NSTitle.class).getDataFolder();
        File folder = new File(pluginFolder, folderName);
        File fileToCheck = new File(folder, fileName + "." + type.name().toLowerCase());
        return fileToCheck.exists();
    }
    protected void saveFile(){
        try {
            configuration.save(file);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

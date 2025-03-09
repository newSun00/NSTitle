package org.nano.nstitle.data.db.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.nano.nstitle.data.db.SuperFile;
import org.nano.nstitle.util.key.DataKeys;

public class Config extends SuperFile {
    private FileConfiguration configuration;

    private boolean enable;
    private String username;
    private String password;

    private String url;

    private void setup(){
        super.folderName = "config";
        super.fileName = "config";
        super.type = DataKeys.YML;

        super.setUp();

        this.configuration = super.configuration;
    }

    /**
     * @description
     *  MySQL
     *      enable: true
     *      port: 3306
     *      user: root
     *      password: 0000
     *      url:
     */

    public void load(){
        setup();
        String path = "MySQL";
        if (!configuration.contains(path)) {
            configuration.set(path + ".enable", false);
            configuration.set(path + ".host", "localhost");
            configuration.set(path + ".port", 3306);
            configuration.set(path + ".database", "nstitle");
            configuration.set(path + ".username", "root");
            configuration.set(path + ".password", "0000");
            saveFile();
        }
        enable = configuration.getBoolean(path+".enable");
        if ( enable ) {
            String host = configuration.getString(path + ".host");
            int port = configuration.getInt(path + ".port");
            String database = configuration.getString(path + ".database");
            username = configuration.getString(path + ".username");
            password = configuration.getString(path + ".password");

            url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC";
        }
    }
    public boolean isEnable(){
        return this.enable;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public void setEnabled(boolean b) {
        this.enable = b;
    }
}

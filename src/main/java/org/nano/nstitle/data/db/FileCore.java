package org.nano.nstitle.data.db;

import org.bukkit.Bukkit;
import org.nano.nstitle.data.db.file.Config;
import org.nano.nstitle.data.db.file.DataFile;
import org.nano.nstitle.data.db.file.UserFile;
import org.nano.nstitle.data.db.mysql.DataSQL;
import org.nano.nstitle.data.db.mysql.UserSQL;

public class FileCore {
    public static FileCore instance;
    public static FileCore getInstance(){
        if (instance == null) {
            instance = new FileCore();
        }
        return instance;
    }

    private final Config config;
    private DataFile dataFile;
    private UserFile userFile;

    private DataSQL dataSQL;
    private UserSQL userSQL;

    public FileCore() {
        this.config = new Config();
        this.config.load();

        this.dataSQL = new DataSQL(this);
        this.userSQL = new UserSQL(this);

        this.dataFile = new DataFile(this);
        this.userFile = new UserFile(this);

    }

    public Config getConfig() {
        return config;
    }

    public DataFile getDataFile() {
        return dataFile;
    }

    public UserFile getUserFile() {
        return userFile;
    }

    public DataSQL getDataSQL() {
        return dataSQL;
    }

    public UserSQL getUserSQL() {
        return userSQL;
    }

    public void reload() {
        this.config.load();
        if ( config.isEnable() ){
            dataSQL = new DataSQL(this);
            userSQL = new UserSQL(this);

            dataSQL.load();
            Bukkit.getOnlinePlayers().forEach(p->userSQL.load(p.getUniqueId()));
        }
        else{
            dataFile = new DataFile(this);
            userFile = new UserFile(this);

            dataFile.load();
            Bukkit.getOnlinePlayers().forEach(p->userFile.load(p.getUniqueId()));
        }
    }
}

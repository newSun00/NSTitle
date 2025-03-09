package org.nano.nstitle.data.db;

import org.nano.nstitle.data.db.file.Config;
import org.nano.nstitle.data.db.file.DataFile;
import org.nano.nstitle.data.db.file.UserFile;

public class FileCore {
    public static FileCore instance;
    public static FileCore getInstance(){
        if (instance == null) {
            instance = new FileCore();
        }
        return instance;
    }

    private final Config config;
    private final DataFile dataFile;
    private final UserFile userFile;

    public FileCore() {
        this.config = new Config();
        this.dataFile = new DataFile();
        this.userFile = new UserFile();
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
}

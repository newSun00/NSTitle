package org.nano.nstitle.data.registy;

import org.nano.nstitle.data.registy.server.TitleRegistry;
import org.nano.nstitle.data.registy.user.UserRegistry;

public class RegistryCore {
    public static RegistryCore instance;
    public static RegistryCore getInstance(){
        if (instance == null) {
            instance = new RegistryCore();
        }
        return instance;
    }
    private final TitleRegistry titleRegistry;
    private final UserRegistry userRegistry;

    public RegistryCore() {
        this.titleRegistry = new TitleRegistry();
        this.userRegistry = new UserRegistry();
    }

    public TitleRegistry getTitleRegistry() {
        return titleRegistry;
    }

    public UserRegistry getUserRegistry() {
        return userRegistry;
    }

}

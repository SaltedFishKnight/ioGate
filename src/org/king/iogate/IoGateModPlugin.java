package org.king.iogate;

import com.fs.starfarer.api.BaseModPlugin;
import lunalib.lunaSettings.LunaSettings;
import org.king.iogate.client.manager.ListenerManager;

public class IoGateModPlugin extends BaseModPlugin {

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        LunaSettings.addSettingsListener(ListenerManager.TRIGGER_LISTENER);
    }
}

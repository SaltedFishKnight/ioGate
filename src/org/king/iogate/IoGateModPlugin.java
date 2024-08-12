package org.king.iogate;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;
import lunalib.lunaSettings.LunaSettings;
import org.king.iogate.client.manager.ListenerManager;

public class IoGateModPlugin extends BaseModPlugin {

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        SettingsAPI settings = Global.getSettings();
        // TODO 不允许进入开发模式，因为 Ctrl + LMB 会破坏同步
        // settings.setDevMode(false);

        LunaSettings.addSettingsListener(ListenerManager.TRIGGER_LISTENER);
    }
}

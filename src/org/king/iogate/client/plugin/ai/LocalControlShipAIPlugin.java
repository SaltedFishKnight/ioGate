package org.king.iogate.client.plugin.ai;

import com.fs.starfarer.api.Global;
import org.king.iogate.client.manager.NetworkInfoManager;

public class LocalControlShipAIPlugin extends AbstractShipAIPlugin {

    @Override
    public void advance(float amount) {
        Global.getCombatEngine().setPlayerShipExternal(NetworkInfoManager.localShip);
    }
}

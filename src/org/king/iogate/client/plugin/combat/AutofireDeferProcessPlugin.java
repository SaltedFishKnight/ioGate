package org.king.iogate.client.plugin.combat;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.input.InputEventAPI;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.state.RemoteShipState;

import java.util.List;

public class AutofireDeferProcessPlugin extends BaseEveryFrameCombatPlugin {

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        super.advance(amount, events);
        for (int i = 0; i < 7; i++) {
            if (i == RemoteShipState.curGroupIndex) {
                continue;
            }
            boolean iLocalDisplay = RemoteShipState.localAutofireDisplay.get(i);
            boolean iRemoteDisplay = RemoteShipState.remoteAutofireDisplay.get(i);
            if (iLocalDisplay != iRemoteDisplay) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_AUTOFIRE, null, i);
                RemoteShipState.localAutofireDisplay.set(i, iRemoteDisplay);
                break;
            }
        }
    }
}

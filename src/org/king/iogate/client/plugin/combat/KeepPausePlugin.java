package org.king.iogate.client.plugin.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import org.king.iogate.client.listener.InputStateListener;
import org.king.iogate.client.manager.InputStateManager;
import org.king.iogate.client.state.type.InputType;

import java.util.List;

public class KeepPausePlugin extends BaseEveryFrameCombatPlugin {

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        super.advance(amount, events);
        InputStateListener.listenPauseInput();
        if (InputStateManager.KEY_SPACE.type == InputType.DOWN
                || InputStateManager.KEY_TAB.type == InputType.DOWN
                || InputStateManager.KEY_ESCAPE.type == InputType.DOWN) {
            Global.getCombatEngine().setPaused(true);
        }
    }
}

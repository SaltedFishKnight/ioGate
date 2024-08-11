package org.king.iogate.client.plugin.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.network.LobbyActionSet;
import org.king.iogate.client.state.NetworkState;
import org.king.iogate.client.state.type.MatchType;

import java.util.List;

public class InterruptCombatPlugin extends BaseEveryFrameCombatPlugin {

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        super.advance(amount, events);
        CombatEngineAPI combatEngine = Global.getCombatEngine();
        /*
            TODO
                当使用 ESC 的 END MISSION，并弹出结算面板时才触发
                若没有结算面板，直接退出任务，则不会触发
         */
        if (combatEngine.isCombatOver()
                && NetworkInfoManager.localShip.isAlive()
                && NetworkInfoManager.remoteShip.isAlive()
                && NetworkState.matchState == MatchType.MATCHED) {
            combatEngine.endCombat(0f, FleetSide.ENEMY);
            NetworkState.matchState = MatchType.NOT_MATCHED;
            LobbyActionSet.retrunToLobbyActively();
        }
    }
}

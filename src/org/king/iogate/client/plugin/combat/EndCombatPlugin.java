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

public class EndCombatPlugin extends BaseEveryFrameCombatPlugin {

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        super.advance(amount, events);

        // 如果任意一方 Flagship 死亡，则选出胜者
        FleetSide winner = !NetworkInfoManager.localShip.isAlive() ? FleetSide.ENEMY :
                !NetworkInfoManager.remoteShip.isAlive() ? FleetSide.PLAYER : null;

        // 如果选出了胜者且比赛状态为匹配，则结束战斗
        if (winner != null && NetworkState.matchState == MatchType.MATCHED) {
            CombatEngineAPI combatEngine = Global.getCombatEngine();
            combatEngine.endCombat(0f, winner);
            NetworkState.matchState = MatchType.NOT_MATCHED;
            LobbyActionSet.endCombat();
        }
    }
}

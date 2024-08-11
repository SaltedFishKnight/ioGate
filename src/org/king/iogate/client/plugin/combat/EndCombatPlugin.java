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

        // 确定检查哪个飞船的状态以及结束哪个舰队的战斗
        FleetSide winner = !NetworkInfoManager.localShip.isAlive() ? FleetSide.ENEMY :
                !NetworkInfoManager.remoteShip.isAlive() ? FleetSide.PLAYER : null;

        // 如果飞船不存活且比赛状态为匹配，则结束战斗
        if (winner != null && NetworkState.matchState == MatchType.MATCHED) {
            CombatEngineAPI combatEngine = Global.getCombatEngine();
            combatEngine.endCombat(0f, winner);
            NetworkState.matchState = MatchType.NOT_MATCHED;
            LobbyActionSet.endCombat();
        }
    }
}

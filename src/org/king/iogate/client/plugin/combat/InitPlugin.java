package org.king.iogate.client.plugin.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.combat.WeaponGroupAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import lombok.extern.slf4j.Slf4j;
import org.king.iogate.client.manager.FrameManager;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.manager.PluginManager;
import org.king.iogate.client.network.RoomActionSet;
import org.king.iogate.client.state.LocalShipState;
import org.king.iogate.client.state.NetworkState;
import org.king.iogate.client.state.RemoteShipState;
import org.king.iogate.client.state.type.MatchType;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

@Slf4j
public class InitPlugin extends BaseEveryFrameCombatPlugin {

    @Override
    public void init(CombatEngineAPI engine) {
        super.init(engine);

        initSystem(engine);

        initRemoteShip();

        initLocalShip();

        initPlugins(engine);

        RoomActionSet.ready();
    }

    private void initSystem(CombatEngineAPI engine) {
        if (NetworkState.matchState != MatchType.MATCHED) {
            log.info("用户未成功匹配，需要匹配成功才能开始战斗");
            log.info("User is not successfully matched and need to be matched with an opponent to start the combat");
            engine.endCombat(0f, FleetSide.ENEMY);
        }

        // 自定义结束对局的实现
        engine.setDoNotEndCombat(true);

        // 不允许进入开发模式
        SettingsAPI settings = Global.getSettings();
        settings.setDevMode(false);

        FrameManager.newCombat();
    }

    private void initRemoteShip() {
        NetworkInfoManager.remoteShip = CombatUtils.spawnShipOrWingDirectly(NetworkInfoManager.remoteVariantId, FleetMemberType.SHIP, FleetSide.ENEMY, 1f, new Vector2f(0f, 3000f), 270f);
        NetworkInfoManager.remoteShip.setShipAI(PluginManager.EMPTY_SHIP_AI_PLUGIN);

        List<WeaponGroupAPI> weaponGroupsCopy = NetworkInfoManager.remoteShip.getWeaponGroupsCopy();
        RemoteShipState.NumberOfGroups = weaponGroupsCopy.size();
        for (int i = 0; i < RemoteShipState.NumberOfGroups; i++) {
            RemoteShipState.localAutofireDisplay.set(i, weaponGroupsCopy.get(i).isAutofiring());
            RemoteShipState.remoteAutofireDisplay.set(i, weaponGroupsCopy.get(i).isAutofiring());
        }

        RemoteShipState.curGroupIndex = 0;
        if (weaponGroupsCopy.get(0).isAutofiring()) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_AUTOFIRE, null, 0);
            RemoteShipState.localAutofireDisplay.set(0, false);
        }
    }

    private void initLocalShip() {
        NetworkInfoManager.localShip = CombatUtils.spawnShipOrWingDirectly(NetworkInfoManager.localVariantId, FleetMemberType.SHIP, FleetSide.PLAYER, 1f, new Vector2f(0f, -3000f), 90f);
        NetworkInfoManager.localShip.setShipAI(PluginManager.LOCAL_CONTROL_SHIP_AI_PLUGIN);
        NetworkInfoManager.localShip.setAlly(true);

        LocalShipState.curGroupIndex = 0;
        List<WeaponGroupAPI> weaponGroupsCopy = NetworkInfoManager.localShip.getWeaponGroupsCopy();
        for (int i = 0; i < weaponGroupsCopy.size(); i++) {
            LocalShipState.autofireState.set(i, weaponGroupsCopy.get(i).isAutofiring());
        }
    }

    private void initPlugins(CombatEngineAPI engine) {
        engine.addPlugin(PluginManager.AUTOFIRE_DEFER_PROCESS_PLUGIN);
        engine.addPlugin(PluginManager.KEEP_PAUSE_PLUGIN);
        engine.addPlugin(PluginManager.INTERRUPT_COMBAT_PLUGIN);
        engine.addPlugin(PluginManager.END_COMBAT_PLUGIN);
    }
}

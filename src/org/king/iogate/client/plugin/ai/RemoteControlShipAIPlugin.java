package org.king.iogate.client.plugin.ai;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import org.king.iogate.client.listener.InputStateListener;
import org.king.iogate.client.listener.LocalShipActionListener;
import org.king.iogate.client.manager.FrameManager;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.network.RoomActionSet;
import org.king.iogate.client.state.RemoteShipState;
import org.king.iogate.common.protobuf.room.*;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class RemoteControlShipAIPlugin extends AbstractShipAIPlugin {

    @Override
    public void advance(float amount) {
        // 更新本地玩家输入状态
        InputStateListener.listenAllLocalInput();

        // 构建一帧的飞船行为
        ShipAction producerShipAction = LocalShipActionListener.buildShipAction();

        // 推送到指定用户
        ActionWithId actionWithId = new ActionWithId();
        actionWithId.remoteUserId = NetworkInfoManager.remoteUserId;
        actionWithId.shipAction = producerShipAction;
        RoomActionSet.uploadShipAction(actionWithId);

        // 远程飞船行动逻辑
        ShipAction shipAction = FrameManager.FRAME_BUFFER.poll();
        if(shipAction == null) {
            repeatLastFrameShipAction(FrameManager.lastFrameShipAction);
        } else {
            consumeLatestShipAction(shipAction);
            FrameManager.lastFrameShipAction = shipAction;
        }
    }

    private void repeatLastFrameShipAction(ShipAction shipAction) {

        Vector2f mouseTarget = NetworkInfoManager.remoteShip.getMouseTarget();

        if (shipAction.isShiftPressed) {
            NetworkInfoManager.remoteShip.setFacing(VectorUtils.getAngleStrict(NetworkInfoManager.remoteShip.getLocation(), mouseTarget));
        }

        if (shipAction.defenseSystem.isFired) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.FIRE, mouseTarget, RemoteShipState.curGroupIndex);
        }

        processMovement(shipAction.movement);
    }

    private void consumeLatestShipAction(ShipAction shipAction) {

        processDefenseSystem(shipAction.mouseLocation, shipAction.defenseSystem, shipAction.shipFacing);

        processMovement(shipAction.movement);

        processWeaponGroup(shipAction.weaponGroup);
    }

    private void processDefenseSystem(MouseLocation mouseLocation, DefenseSystem defenseSystem, float shipFacing) {

        Vector2f mouseTarget = NetworkInfoManager.remoteShip.getMouseTarget();
        mouseTarget.set(mouseLocation.x, mouseLocation.y);

        if (defenseSystem.isTargetedShipOrClearedTarget) {
            List<ShipAPI> shipsWithinRange = CombatUtils.getShipsWithinRange(mouseTarget, 0f);
            ShipAPI shipTarget = NetworkInfoManager.remoteShip.getShipTarget();
            if (!shipsWithinRange.isEmpty() && NetworkInfoManager.remoteShip != shipsWithinRange.getFirst() && shipTarget != shipsWithinRange.getFirst()) {
                NetworkInfoManager.remoteShip.setShipTarget(shipsWithinRange.getFirst());
            } else {
                NetworkInfoManager.remoteShip.setShipTarget(null);
            }
        }
        if (defenseSystem.isFired) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.FIRE, mouseTarget, RemoteShipState.curGroupIndex);
        }
        if (defenseSystem.isHoldenFire) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.HOLD_FIRE, null, 0);
        }
        if (defenseSystem.isToggledShieldOrPhaseCloak) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_SHIELD_OR_PHASE_CLOAK, null, 0);
        }

        boolean isUsedSpecificSystem = false;
        if (defenseSystem.isUsedSystem) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.USE_SYSTEM, null, 0);
            ShipSystemAPI system = NetworkInfoManager.remoteShip.getSystem();
            String displayName = system.getDisplayName();
            int ammo = system.getAmmo();
            if (("Phase Skimmer".equals(displayName) || "Degraded Phase Skimmer".equals(displayName)) && ammo > 0) {
                isUsedSpecificSystem = true;
                NetworkInfoManager.remoteShip.setFacing(VectorUtils.getAngleStrict(NetworkInfoManager.remoteShip.getLocation(), mouseTarget));
            }
        }
        if (!isUsedSpecificSystem) {
            NetworkInfoManager.remoteShip.setFacing(shipFacing);
        }

        if (defenseSystem.isVentedFlux) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.VENT_FLUX, null, 0);
        }
        if (defenseSystem.isPulledBackFighters) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.PULL_BACK_FIGHTERS, null, 0);
        }
    }

    private void processMovement(Movement movement) {
        if (movement.isAccelerated) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.ACCELERATE, null, 0);
        }
        if (movement.isAcceleratedBackwards) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.ACCELERATE_BACKWARDS, null, 0);
        }
        if (movement.isDecelerated) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.DECELERATE, null, 0);
        }
        if (movement.isStrafedLeft) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.STRAFE_LEFT, null, 0);
        }
        if (movement.isStrafedRight) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.STRAFE_RIGHT, null, 0);
        }
        if (movement.isTurnedLeft) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TURN_LEFT, null, 0);
        }
        if (movement.isTurnedRight) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TURN_RIGHT, null, 0);
        }
    }

    private void processWeaponGroup(WeaponGroup weaponGroup) {
        RemoteShipState.remoteAutofireDisplay = weaponGroup.autofireState;
        int oldIndex = RemoteShipState.curGroupIndex;
        int newIndex = weaponGroup.curGroupIndex;
        if (oldIndex != newIndex) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.SELECT_GROUP, null, newIndex);
            boolean isNewAutofire = RemoteShipState.localAutofireDisplay.get(newIndex);
            if (isNewAutofire) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_AUTOFIRE, null, newIndex);
                RemoteShipState.localAutofireDisplay.set(newIndex, false);
            } else {
                boolean oldLocalDisplay = RemoteShipState.localAutofireDisplay.get(oldIndex);
                boolean oldRemoteDisplay = RemoteShipState.remoteAutofireDisplay.get(oldIndex);
                if (oldLocalDisplay != oldRemoteDisplay) {
                    NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_AUTOFIRE, null, oldIndex);
                    RemoteShipState.localAutofireDisplay.set(oldIndex, oldRemoteDisplay);
                }
            }
            RemoteShipState.curGroupIndex = newIndex;
        } else {
            for (int i = 0; i < 7; i++) {
                if (i == oldIndex) {
                    continue;
                }
                boolean iLocalDisplay = RemoteShipState.localAutofireDisplay.get(i);
                boolean iRemoteDisplay = RemoteShipState.remoteAutofireDisplay.get(i);
                if (iLocalDisplay != iRemoteDisplay) {
                    NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_AUTOFIRE, null, i);
                    RemoteShipState.localAutofireDisplay.set(i, iRemoteDisplay);
                    // 一帧之内只能处理一次切换自动开火命令
                    break;
                }
            }
        }
    }
}

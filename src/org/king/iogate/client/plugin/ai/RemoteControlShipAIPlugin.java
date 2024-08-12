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
import org.king.iogate.common.protobuf.room.ActionWithId;
import org.king.iogate.common.protobuf.room.CommandType;
import org.king.iogate.common.protobuf.room.ShipAction;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;
import java.util.Objects;

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

        /*
         * TODO
         *  计数器记录使用缓存帧次数
         *  后发玩家使用计数器，清除先手玩家的所有action
         *  使用计数器跳过延迟帧和丢帧（假设缓存帧预测成功）
         *  若缓存帧预测失败，没有任何纠错能力
         *  使用 boolean 代替 CommandType，减少循环判断
         */
        // 远程飞船行动逻辑
        ShipAction shipAction = FrameManager.FRAME_BUFFER.poll();
        if(shipAction == null) {
            repeatLastFrameShipAction(FrameManager.lastFrameShipAction);
        } else {
            consumeLatestShipAction(shipAction);
            FrameManager.lastFrameShipAction = shipAction;
        }
        FrameManager.FRAME_COUNTER.getAndIncrement();
    }

    private void repeatLastFrameShipAction(ShipAction shipAction) {

        Vector2f mouseTarget = NetworkInfoManager.remoteShip.getMouseTarget();
        mouseTarget.set(shipAction.mouseLocation.x, shipAction.mouseLocation.y);

        for (String commandType : shipAction.commandList) {
            if (Objects.equals(commandType, CommandType.FIRE.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.FIRE, mouseTarget, RemoteShipState.curGroupIndex);
            } else if (Objects.equals(commandType, CommandType.ACCELERATE.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.ACCELERATE, null, 0);
            } else if (Objects.equals(commandType, CommandType.ACCELERATE_BACKWARDS.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.ACCELERATE_BACKWARDS, null, 0);
            } else if (Objects.equals(commandType, CommandType.DECELERATE.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.DECELERATE, null, 0);
            } else if (Objects.equals(commandType, CommandType.STRAFE_LEFT.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.STRAFE_LEFT, null, 0);
            } else if (Objects.equals(commandType, CommandType.STRAFE_RIGHT.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.STRAFE_RIGHT, null, 0);
            } else if (Objects.equals(commandType, CommandType.TURN_LEFT.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TURN_LEFT, null, 0);
            } else if (Objects.equals(commandType, CommandType.TURN_RIGHT.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TURN_RIGHT, null, 0);
            }

            if (shipAction.isShiftPressed) {
                NetworkInfoManager.remoteShip.setFacing(VectorUtils.getAngleStrict(NetworkInfoManager.remoteShip.getLocation(), mouseTarget));
            }
        }

    }

    private void consumeLatestShipAction(ShipAction shipAction) {

        boolean isUsedSpecificSystem = false;



        Vector2f mouseTarget = NetworkInfoManager.remoteShip.getMouseTarget();
        mouseTarget.set(shipAction.mouseLocation.x, shipAction.mouseLocation.y);



        for (String commandType : shipAction.commandList) {
            if (Objects.equals(commandType, CommandType.TARGET_SHIP_OR_CLEAR_TARGET.name())) {
                List<ShipAPI> shipsWithinRange = CombatUtils.getShipsWithinRange(mouseTarget, 0f);
                ShipAPI shipTarget = NetworkInfoManager.remoteShip.getShipTarget();
                if (!shipsWithinRange.isEmpty() && NetworkInfoManager.remoteShip != shipsWithinRange.getFirst() && shipTarget != shipsWithinRange.getFirst()) {
                    NetworkInfoManager.remoteShip.setShipTarget(shipsWithinRange.getFirst());
                } else {
                    NetworkInfoManager.remoteShip.setShipTarget(null);
                }
            } else if (Objects.equals(commandType, CommandType.FIRE.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.FIRE, mouseTarget, RemoteShipState.curGroupIndex);
            } else if (Objects.equals(commandType, CommandType.HOLD_FIRE.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.HOLD_FIRE, null, 0);
            } else if (Objects.equals(commandType, CommandType.TOGGLE_SHIELD_OR_PHASE_CLOAK.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_SHIELD_OR_PHASE_CLOAK, null, 0);
            } else if (Objects.equals(commandType, CommandType.USE_SYSTEM.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.USE_SYSTEM, null, 0);
                ShipSystemAPI system = NetworkInfoManager.remoteShip.getSystem();
                String displayName = system.getDisplayName();
                int ammo = system.getAmmo();
                if (("Phase Skimmer".equals(displayName) || "Degraded Phase Skimmer".equals(displayName)) && ammo > 0) {
                    isUsedSpecificSystem = true;
                    NetworkInfoManager.remoteShip.setFacing(VectorUtils.getAngleStrict(NetworkInfoManager.remoteShip.getLocation(), mouseTarget));
                }
            } else if (Objects.equals(commandType, CommandType.VENT_FLUX.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.VENT_FLUX, null, 0);
            } else if (Objects.equals(commandType, CommandType.PULL_BACK_FIGHTERS.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.PULL_BACK_FIGHTERS, null, 0);
            } else if (Objects.equals(commandType, CommandType.ACCELERATE.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.ACCELERATE, null, 0);
            } else if (Objects.equals(commandType, CommandType.ACCELERATE_BACKWARDS.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.ACCELERATE_BACKWARDS, null, 0);
            } else if (Objects.equals(commandType, CommandType.DECELERATE.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.DECELERATE, null, 0);
            } else if (Objects.equals(commandType, CommandType.STRAFE_LEFT.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.STRAFE_LEFT, null, 0);
            } else if (Objects.equals(commandType, CommandType.STRAFE_RIGHT.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.STRAFE_RIGHT, null, 0);
            } else if (Objects.equals(commandType, CommandType.TURN_LEFT.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TURN_LEFT, null, 0);
            } else if (Objects.equals(commandType, CommandType.TURN_RIGHT.name())) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TURN_RIGHT, null, 0);
            }
        }



        RemoteShipState.remoteAutofireDisplay = shipAction.weaponGroupState.autofireState;
        int oldIndex = RemoteShipState.curGroupIndex;
        int newIndex = shipAction.weaponGroupState.curGroupIndex;
        if (oldIndex != newIndex) {
            NetworkInfoManager.remoteShip.giveCommand(ShipCommand.SELECT_GROUP, null, newIndex);
            boolean isNewAutofire = RemoteShipState.localAutofireDisplay.get(newIndex);
            if (isNewAutofire) {
                NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_AUTOFIRE, null, newIndex);
                RemoteShipState.localAutofireDisplay.set(newIndex, false);
            } else {
                // TODO 可提取方法，简化代码
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
                // TODO 可提取方法，简化代码
                boolean iLocalDisplay = RemoteShipState.localAutofireDisplay.get(i);
                boolean iRemoteDisplay = RemoteShipState.remoteAutofireDisplay.get(i);
                if (iLocalDisplay != iRemoteDisplay) {
                    NetworkInfoManager.remoteShip.giveCommand(ShipCommand.TOGGLE_AUTOFIRE, null, i);
                    RemoteShipState.localAutofireDisplay.set(i, iRemoteDisplay);
                    break;
                }
            }
        }

        if (!isUsedSpecificSystem) {
            NetworkInfoManager.remoteShip.setFacing(shipAction.shipFacing);
        }
    }
}

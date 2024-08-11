package org.king.iogate.client.listener;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ViewportAPI;
import org.king.iogate.client.manager.FrameManager;
import org.king.iogate.client.manager.InputStateManager;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.manager.PluginManager;
import org.king.iogate.client.state.InputState;
import org.king.iogate.client.state.LocalShipState;
import org.king.iogate.client.state.type.InputType;
import org.king.iogate.common.protobuf.room.CommandType;
import org.king.iogate.common.protobuf.room.MouseLocation;
import org.king.iogate.common.protobuf.room.ShipAction;
import org.king.iogate.common.protobuf.room.WeaponGroupState;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LocalShipActionListener {

    public static final List<InputState> NUMBER_STATE_LIST = new ArrayList<>(7);

    static {
        NUMBER_STATE_LIST.add(InputStateManager.KEY_1);
        NUMBER_STATE_LIST.add(InputStateManager.KEY_2);
        NUMBER_STATE_LIST.add(InputStateManager.KEY_3);
        NUMBER_STATE_LIST.add(InputStateManager.KEY_4);
        NUMBER_STATE_LIST.add(InputStateManager.KEY_5);
        NUMBER_STATE_LIST.add(InputStateManager.KEY_6);
        NUMBER_STATE_LIST.add(InputStateManager.KEY_7);
    }

    public static ShipAction buildShipAction() {

        ShipAction shipAction = new ShipAction();



        MouseLocation mouseLocation = new MouseLocation();
        ViewportAPI viewport = Global.getCombatEngine().getViewport();
        float mouseTargetX = viewport.convertScreenXToWorldX(Mouse.getX());
        float mouseTargetY = viewport.convertScreenYToWorldY(Mouse.getY());
        mouseLocation.x = -mouseTargetX;
        mouseLocation.y = -mouseTargetY;
        shipAction.mouseLocation = mouseLocation;



        LinkedList<String> commandList = new LinkedList<>();
        if (InputStateManager.KEY_R.type == InputType.DOWN) {
            commandList.add(CommandType.TARGET_SHIP_OR_CLEAR_TARGET.name());
        }

        if (InputStateManager.LEFT_MOUSE_BUTTON.type == InputType.DOWN || InputStateManager.LEFT_MOUSE_BUTTON.type == InputType.REPEAT) {
            commandList.add(CommandType.FIRE.name());
        }

        if (InputStateManager.KEY_X.type == InputType.DOWN) {
            commandList.add(CommandType.HOLD_FIRE.name());
        }

        if (InputStateManager.RIGHT_MOUSE_BUTTON.type == InputType.DOWN) {
            commandList.add(CommandType.TOGGLE_SHIELD_OR_PHASE_CLOAK.name());
        }

        if (InputStateManager.KEY_F.type == InputType.DOWN) {
            commandList.add(CommandType.USE_SYSTEM.name());
        }

        if (InputStateManager.KEY_V.type == InputType.DOWN) {
            commandList.add(CommandType.VENT_FLUX.name());
        }

        if (InputStateManager.KEY_Z.type == InputType.DOWN && InputStateManager.KEY_Y.type == InputType.DOWN) {
            commandList.add(CommandType.PULL_BACK_FIGHTERS.name());
        }



        if (InputStateManager.KEY_W.type == InputType.DOWN || InputStateManager.KEY_W.type == InputType.REPEAT) {
            commandList.add(CommandType.ACCELERATE.name());
        }

        if (InputStateManager.KEY_S.type == InputType.DOWN || InputStateManager.KEY_S.type == InputType.REPEAT) {
            commandList.add(CommandType.ACCELERATE_BACKWARDS.name());
        }

        if (InputStateManager.KEY_C.type == InputType.DOWN || InputStateManager.KEY_C.type == InputType.REPEAT) {
            commandList.add(CommandType.DECELERATE.name());
        }

        if (InputStateManager.KET_LSHIFT.type == InputType.DOWN || InputStateManager.KET_LSHIFT.type == InputType.REPEAT) {

            shipAction.isShiftPressed = true;

            if (InputStateManager.KEY_Q.type == InputType.DOWN || InputStateManager.KEY_Q.type == InputType.REPEAT
                    || InputStateManager.KEY_A.type == InputType.DOWN || InputStateManager.KEY_A.type == InputType.REPEAT) {
                commandList.add(CommandType.STRAFE_LEFT.name());
            }

            if (InputStateManager.KEY_E.type == InputType.DOWN || InputStateManager.KEY_E.type == InputType.REPEAT
                    || InputStateManager.KEY_D.type == InputType.DOWN || InputStateManager.KEY_D.type == InputType.REPEAT) {
                commandList.add(CommandType.STRAFE_RIGHT.name());
            }
        } else {
            if (InputStateManager.KEY_A.type == InputType.DOWN || InputStateManager.KEY_A.type == InputType.REPEAT) {
                commandList.add(CommandType.TURN_LEFT.name());
            }

            if (InputStateManager.KEY_D.type == InputType.DOWN || InputStateManager.KEY_D.type == InputType.REPEAT) {
                commandList.add(CommandType.TURN_RIGHT.name());
            }

            if (InputStateManager.KEY_Q.type == InputType.DOWN || InputStateManager.KEY_Q.type == InputType.REPEAT) {
                commandList.add(CommandType.STRAFE_LEFT.name());
            }

            if (InputStateManager.KEY_E.type == InputType.DOWN || InputStateManager.KEY_E.type == InputType.REPEAT) {
                commandList.add(CommandType.STRAFE_RIGHT.name());
            }
        }
        shipAction.commandList = commandList;



        WeaponGroupState weaponGroupState = new WeaponGroupState();
        for (int i = 0; i < 7; i++) {
            InputState numberState = NUMBER_STATE_LIST.get(i);
            if ((InputStateManager.KEY_LCONTROL.type == InputType.DOWN || InputStateManager.KEY_LCONTROL.type == InputType.REPEAT)
                    && numberState.type == InputType.DOWN) {
                boolean isAutofire = LocalShipState.autofireState.get(i);
                LocalShipState.autofireState.set(i, !isAutofire);
            } else if (numberState.type == InputType.DOWN) {
                LocalShipState.curGroupIndex = i;
            }
        }
        weaponGroupState.curGroupIndex = LocalShipState.curGroupIndex;
        weaponGroupState.autofireState = LocalShipState.autofireState;
        shipAction.weaponGroupState = weaponGroupState;



        if (InputStateManager.KEY_U.type == InputType.DOWN) {
            NetworkInfoManager.localShip.setShipAI(PluginManager.LOCAL_CONTROL_SHIP_AI_PLUGIN);
        }

        if (InputStateManager.KEY_SPACE.type == InputType.DOWN
                || InputStateManager.KEY_TAB.type == InputType.DOWN
                || InputStateManager.KEY_ESCAPE.type == InputType.DOWN) {
            Global.getCombatEngine().setPaused(false);
        }



        shipAction.shipFacing = (NetworkInfoManager.localShip.getFacing() + 180f) % 360f;



        shipAction.frameIndex = FrameManager.FRAME_COUNTER.get();



        return shipAction;
    }
}

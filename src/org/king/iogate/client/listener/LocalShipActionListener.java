package org.king.iogate.client.listener;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ViewportAPI;
import org.king.iogate.client.manager.InputStateManager;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.manager.PluginManager;
import org.king.iogate.client.state.InputState;
import org.king.iogate.client.state.LocalShipState;
import org.king.iogate.client.state.type.InputType;
import org.king.iogate.common.protobuf.room.*;
import org.king.iogate.common.protobuf.room.DefenseSystem;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
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

        processCommandExecution(shipAction);

        buildMouseLocation(shipAction);

        buildDefenseSystem(shipAction);

        buildMovement(shipAction);

        buildWeaaponGroup(shipAction);

        return shipAction;
    }

    private static void processCommandExecution(ShipAction shipAction) {
        // 不允许 AI 接管 Flagship
        if (InputStateManager.KEY_U.type == InputType.DOWN) {
            NetworkInfoManager.localShip.setShipAI(PluginManager.LOCAL_CONTROL_SHIP_AI_PLUGIN);
        }

        // SPACE 和 ESC 不会影响本地飞船执行命令
        if (InputStateManager.KEY_SPACE.type == InputType.DOWN || InputStateManager.KEY_ESCAPE.type == InputType.DOWN) {
            Global.getCombatEngine().setPaused(false);
        }

        // 使用 Tab 唤出指挥面板，在指挥面板时，本地飞船无法执行任何命令
        if (InputStateManager.KEY_TAB.type == InputType.DOWN) {
            LocalShipState.isOpenedCommandPanel = !LocalShipState.isOpenedCommandPanel;
            Global.getCombatEngine().setPaused(false);
        }
        shipAction.isOpenedCommandPanel = LocalShipState.isOpenedCommandPanel;
    }

    private static void buildMouseLocation(ShipAction shipAction) {
        MouseLocation mouseLocation = new MouseLocation();
        ViewportAPI viewport = Global.getCombatEngine().getViewport();
        float mouseTargetX = viewport.convertScreenXToWorldX(Mouse.getX());
        float mouseTargetY = viewport.convertScreenYToWorldY(Mouse.getY());
        mouseLocation.x = -mouseTargetX;
        mouseLocation.y = -mouseTargetY;
        shipAction.mouseLocation = mouseLocation;
    }

    private static void buildDefenseSystem(ShipAction shipAction) {
        DefenseSystem defenseSystem = new DefenseSystem();
        if (InputStateManager.KEY_R.type == InputType.DOWN) {
            defenseSystem.isTargetedShipOrClearedTarget = true;
        }
        if (InputStateManager.LEFT_MOUSE_BUTTON.type == InputType.DOWN || InputStateManager.LEFT_MOUSE_BUTTON.type == InputType.REPEAT) {
            defenseSystem.isFired = true;
        }
        if (InputStateManager.KEY_X.type == InputType.DOWN) {
            defenseSystem.isHoldenFire = true;
        }
        if (InputStateManager.RIGHT_MOUSE_BUTTON.type == InputType.DOWN) {
            defenseSystem.isToggledShieldOrPhaseCloak = true;
        }
        if (InputStateManager.KEY_F.type == InputType.DOWN) {
            defenseSystem.isUsedSystem = true;
        }
        if (InputStateManager.KEY_V.type == InputType.DOWN) {
            defenseSystem.isVentedFlux = true;
        }
        if (InputStateManager.KEY_Z.type == InputType.DOWN && InputStateManager.KEY_Y.type == InputType.DOWN) {
            defenseSystem.isPulledBackFighters = true;
        }
        shipAction.defenseSystem = defenseSystem;
    }

    private static void buildMovement(ShipAction shipAction) {
        Movement movement = new Movement();
        shipAction.shipFacing = (NetworkInfoManager.localShip.getFacing() + 180f) % 360f;
        if (InputStateManager.KEY_W.type == InputType.DOWN || InputStateManager.KEY_W.type == InputType.REPEAT) {
            movement.isAccelerated = true;
        }
        if (InputStateManager.KEY_S.type == InputType.DOWN || InputStateManager.KEY_S.type == InputType.REPEAT) {
            movement.isAcceleratedBackwards = true;
        }
        if (InputStateManager.KEY_C.type == InputType.DOWN || InputStateManager.KEY_C.type == InputType.REPEAT) {
            movement.isDecelerated = true;
        }
        if (InputStateManager.KET_LSHIFT.type == InputType.DOWN || InputStateManager.KET_LSHIFT.type == InputType.REPEAT) {
            shipAction.isShiftPressed = true;
            if (InputStateManager.KEY_Q.type == InputType.DOWN || InputStateManager.KEY_Q.type == InputType.REPEAT
                    || InputStateManager.KEY_A.type == InputType.DOWN || InputStateManager.KEY_A.type == InputType.REPEAT) {
                movement.isStrafedLeft = true;
            }
            if (InputStateManager.KEY_E.type == InputType.DOWN || InputStateManager.KEY_E.type == InputType.REPEAT
                    || InputStateManager.KEY_D.type == InputType.DOWN || InputStateManager.KEY_D.type == InputType.REPEAT) {
                movement.isStrafedRight = true;
            }
        } else {
            if (InputStateManager.KEY_A.type == InputType.DOWN || InputStateManager.KEY_A.type == InputType.REPEAT) {
                movement.isTurnedLeft = true;
            }
            if (InputStateManager.KEY_D.type == InputType.DOWN || InputStateManager.KEY_D.type == InputType.REPEAT) {
                movement.isTurnedRight = true;
            }
            if (InputStateManager.KEY_Q.type == InputType.DOWN || InputStateManager.KEY_Q.type == InputType.REPEAT) {
                movement.isStrafedLeft = true;
            }
            if (InputStateManager.KEY_E.type == InputType.DOWN || InputStateManager.KEY_E.type == InputType.REPEAT) {
                movement.isStrafedRight = true;
            }
        }
        shipAction.movement = movement;
    }

    private static void buildWeaaponGroup(ShipAction shipAction) {
        WeaponGroup weaponGroup = new WeaponGroup();
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
        weaponGroup.curGroupIndex = LocalShipState.curGroupIndex;
        weaponGroup.autofireState = LocalShipState.autofireState;
        shipAction.weaponGroup = weaponGroup;
    }
}

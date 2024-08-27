package org.king.iogate.client.manager;

import org.king.iogate.client.state.RemoteShipState;
import org.king.iogate.common.protobuf.room.*;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class FrameManager {

    public static final AtomicInteger FRAME_COUNTER = new AtomicInteger(0);

    public static ShipAction[] frameArray;

    public static ShipAction lastFrameShipAction;

    public static void newCombat() {
        FRAME_COUNTER.set(0);
        // 60 帧情况下，数组可以缓存 1 小时之内的 ShipAction
        frameArray = new ShipAction[216000];
        frameArray[0] = initFrameZero();
//        ShipAction frameZero = initFrameZero();
//        Arrays.fill(frameArray, 0, 1, frameZero);
    }

    private static ShipAction initFrameZero() {

        ShipAction frameZero = new ShipAction();

        MouseLocation mouseLocation = new MouseLocation();
        mouseLocation.x = 0f;
        mouseLocation.y = 0f;
        frameZero.mouseLocation = mouseLocation;

        WeaponGroup weaponGroup = new WeaponGroup();
        weaponGroup.curGroupIndex = 0;
        weaponGroup.autofireState = new LinkedList<>(RemoteShipState.remoteAutofireDisplay);
        frameZero.weaponGroup = weaponGroup;

        // All boolean fields are false
        frameZero.movement = new Movement();
        frameZero.defenseSystem = new DefenseSystem();

        frameZero.shipFacing = 270f;
        frameZero.isShiftPressed = false;
        frameZero.isOpenedCommandPanel = false;
        frameZero.frameIndex = 0;

        return frameZero;
    }

}

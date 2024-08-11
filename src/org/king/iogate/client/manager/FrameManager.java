package org.king.iogate.client.manager;

import org.jctools.queues.SpscLinkedQueue;
import org.king.iogate.client.state.RemoteShipState;
import org.king.iogate.common.protobuf.room.MouseLocation;
import org.king.iogate.common.protobuf.room.ShipAction;
import org.king.iogate.common.protobuf.room.WeaponGroupState;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class FrameManager {

    public static final AtomicInteger FRAME_COUNTER = new AtomicInteger(0);

    public static final SpscLinkedQueue<ShipAction> FRAME_BUFFER = new SpscLinkedQueue<>();

    public static ShipAction lastFrameShipAction;

    public static void newCombat() {
        FRAME_COUNTER.set(0);
        FRAME_BUFFER.clear();
        lastFrameShipAction = initFrameZero();
    }

    private static ShipAction initFrameZero() {
        ShipAction frameZero = new ShipAction();
        frameZero.commandList = new LinkedList<>();

        MouseLocation mouseLocation = new MouseLocation();
        mouseLocation.x = 0f;
        mouseLocation.y = 0f;
        frameZero.mouseLocation = mouseLocation;

        WeaponGroupState weaponGroupState = new WeaponGroupState();
        weaponGroupState.curGroupIndex = 0;
        weaponGroupState.autofireState = new LinkedList<>(RemoteShipState.remoteAutofireDisplay);
        frameZero.weaponGroupState = weaponGroupState;

        frameZero.shipFacing = 90f;
        frameZero.frameIndex = 0;
        frameZero.isShiftPressed = false;
        return frameZero;
    }

}

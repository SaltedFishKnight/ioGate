package org.king.iogate.client.manager;

import org.jctools.queues.SpscLinkedQueue;
import org.king.iogate.client.state.RemoteShipState;
import org.king.iogate.common.protobuf.room.*;

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

        frameZero.shipFacing = 90f;
        frameZero.isShiftPressed = false;

        return frameZero;
    }

}

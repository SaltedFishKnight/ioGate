package org.king.iogate.common.protobuf.room;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

@ProtobufClass
public class ShipAction {

    public MouseLocation mouseLocation;

    public WeaponGroup weaponGroup;

    public Movement movement;

    public DefenseSystem defenseSystem;

    public float shipFacing;

    public boolean isShiftPressed;

    public boolean isOpenedCommandPanel;

    public int frameIndex;

}

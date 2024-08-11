package org.king.iogate.common.protobuf.room;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

@ProtobufClass
public class ActionWithId {

    public long remoteUserId;

    public ShipAction shipAction;

}
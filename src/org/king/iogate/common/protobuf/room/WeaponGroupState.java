package org.king.iogate.common.protobuf.room;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

import java.util.List;

@ProtobufClass
public class WeaponGroupState {

    public int curGroupIndex;

    public List<Boolean> autofireState;

}

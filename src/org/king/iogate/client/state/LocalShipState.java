package org.king.iogate.client.state;

import java.util.ArrayList;
import java.util.List;

public class LocalShipState {

    public static int curGroupIndex;

    public static List<Boolean> autofireState = new ArrayList<>(7);

    static {
        for (int i = 0; i < 7; i++) {
            LocalShipState.autofireState.add(false);
        }
    }
}

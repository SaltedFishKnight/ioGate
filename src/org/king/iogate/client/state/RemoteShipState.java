package org.king.iogate.client.state;

import java.util.ArrayList;
import java.util.List;

public class RemoteShipState {

    public static int NumberOfGroups;

    public static int curGroupIndex;

    public static List<Boolean> localAutofireDisplay = new ArrayList<>(7);

    public static List<Boolean> remoteAutofireDisplay = new ArrayList<>(7);

    static {
        for (int i = 0; i < 7; i++) {
            RemoteShipState.localAutofireDisplay.add(false);
            RemoteShipState.remoteAutofireDisplay.add(false);
        }
    }
}
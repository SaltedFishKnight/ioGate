package org.king.iogate.client.state;

import org.king.iogate.client.state.type.InputType;

public class InputState {

    public int value;

    public InputType type = InputType.UP;

    public InputState(int value) {
        this.value = value;
    }
}

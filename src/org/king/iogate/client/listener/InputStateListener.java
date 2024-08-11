package org.king.iogate.client.listener;

import org.king.iogate.client.manager.InputStateManager;
import org.king.iogate.client.state.InputState;
import org.king.iogate.client.state.type.InputType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputStateListener {

    public static void listenAllLocalInput() {
        // 开火输入
        updateKeyboardState(InputStateManager.KEY_R);
        updateMouseState(InputStateManager.LEFT_MOUSE_BUTTON);
        updateKeyboardState(InputStateManager.KEY_X);
        // 飞船系统输入
        updateMouseState(InputStateManager.RIGHT_MOUSE_BUTTON);
        updateKeyboardState(InputStateManager.KEY_F);
        updateKeyboardState(InputStateManager.KEY_V);
        updateKeyboardState(InputStateManager.KEY_Z);
        updateKeyboardState(InputStateManager.KEY_Y);
        // 移动输入
        updateKeyboardState(InputStateManager.KEY_W);
        updateKeyboardState(InputStateManager.KEY_S);
        updateKeyboardState(InputStateManager.KEY_C);
        updateKeyboardState(InputStateManager.KET_LSHIFT);
        updateKeyboardState(InputStateManager.KEY_A);
        updateKeyboardState(InputStateManager.KEY_D);
        updateKeyboardState(InputStateManager.KEY_Q);
        updateKeyboardState(InputStateManager.KEY_E);
        // 武器组输入
        updateKeyboardState(InputStateManager.KEY_1);
        updateKeyboardState(InputStateManager.KEY_2);
        updateKeyboardState(InputStateManager.KEY_3);
        updateKeyboardState(InputStateManager.KEY_4);
        updateKeyboardState(InputStateManager.KEY_5);
        updateKeyboardState(InputStateManager.KEY_6);
        updateKeyboardState(InputStateManager.KEY_7);
        updateKeyboardState(InputStateManager.KEY_LCONTROL);
        // 只在本地处理
        updateKeyboardState(InputStateManager.KEY_U);
        updateKeyboardState(InputStateManager.KEY_SPACE);
        updateKeyboardState(InputStateManager.KEY_TAB);
        updateKeyboardState(InputStateManager.KEY_ESCAPE);
    }

    public static void listenPauseInput() {
        updateKeyboardState(InputStateManager.KEY_SPACE);
        updateKeyboardState(InputStateManager.KEY_TAB);
        updateKeyboardState(InputStateManager.KEY_ESCAPE);
    }

    private static void updateMouseState(InputState inputState) {
        if (Mouse.isButtonDown(inputState.value)) {
            if (inputState.type == InputType.UP) {
                inputState.type = InputType.DOWN;
            } else {
                inputState.type = InputType.REPEAT;
            }
        } else {
            inputState.type = InputType.UP;
        }
    }

    private static void updateKeyboardState(InputState inputState) {
        if (Keyboard.isKeyDown(inputState.value)) {
            if (inputState.type == InputType.UP) {
                inputState.type = InputType.DOWN;
            } else {
                inputState.type = InputType.REPEAT;
            }
        } else {
            inputState.type = InputType.UP;
        }
    }
}

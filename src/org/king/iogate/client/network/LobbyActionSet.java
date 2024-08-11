package org.king.iogate.client.network;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.mission.FleetSide;
import com.iohao.game.common.kit.concurrent.TaskKit;
import com.iohao.game.sdk.command.IoGameClient;
import com.iohao.game.sdk.command.ListenCommand;
import com.iohao.game.sdk.command.RequestCommand;
import com.iohao.game.sdk.kit.CmdKit;
import com.iohao.game.sdk.kit.WrapperKit;
import lombok.extern.slf4j.Slf4j;
import lunalib.lunaSettings.LunaSettings;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.state.NetworkState;
import org.king.iogate.client.state.type.ClientType;
import org.king.iogate.client.state.type.LoginType;
import org.king.iogate.client.state.type.MatchType;
import org.king.iogate.common.protobuf.lobby.LoginResponse;
import org.king.iogate.common.route.LobbyCmd;

import java.util.concurrent.TimeUnit;

@Slf4j
public final class LobbyActionSet {

    private LobbyActionSet() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void login() {
        int loginCmd = CmdKit.merge(LobbyCmd.cmd, LobbyCmd.login);
        RequestCommand.of(loginCmd)
                .setTitle("使用指定的用户ID进行登录")
                .setRequestData(() -> {
                    long userId = LunaSettings.getInt("ioGate", "userId");
                    return WrapperKit.of(userId);
                })
                .setCallback(result -> {
                    LoginResponse loginResponse = result.getValue(LoginResponse.class);
                    long selectedUserId = loginResponse.userId;
                    if (loginResponse.existUser) {
                        NetworkState.loginState = LoginType.NOT_LOGGED_IN;
                        log.info("用户ID[{}]已被占用，请选择其他ID", selectedUserId);
                    } else if (loginResponse.success) {
                        NetworkState.loginState = LoginType.LOGGED_IN;
                        NetworkInfoManager.localUserId = selectedUserId;
                        log.info("用户ID[{}]登录成功", selectedUserId);
                    } else {
                        NetworkState.loginState = LoginType.NOT_LOGGED_IN;
                        log.info("服务器产生未知错误，用户ID[{}]登录失败，请重新尝试", selectedUserId);
                    }
                })
                .execute();

        TaskKit.runOnce(() -> {
            if (NetworkState.loginState == LoginType.LOGGING_IN) {
                NetworkState.loginState = LoginType.NOT_LOGGED_IN;
                log.info("用户登录请求超时");
            }
        }, IoGameClient.requestCommandTimeoutMilliseconds, TimeUnit.MILLISECONDS);
    }

    public static void autoMatch() {
        int autoMatchCmd = CmdKit.merge(LobbyCmd.cmd, LobbyCmd.autoMatch);
        RequestCommand.of(autoMatchCmd)
                .setTitle("上传本地用户的匹配信息，进行自动匹配")
                .setRequestData(() -> {
                    // 当输入框没有任何字符时，提取的字符串不会为 null，而是 ""
                    String variantId = LunaSettings.getString("ioGate", "variantId");
                    String supplementToVariantId = LunaSettings.getString("ioGate", "supplementToVariantId");
                    NetworkInfoManager.localVariantId = variantId.concat(supplementToVariantId);
                    return WrapperKit.of(NetworkInfoManager.localVariantId);
                })
                .setCallback(result -> {
                    boolean isQueued = result.getBoolean();
                    if (isQueued) {
                        log.info("匹配成功，用户信息进入匹配队列");
                    } else {
                        NetworkState.matchState = MatchType.NOT_MATCHED;
                        log.info("匹配失败，请重新尝试");
                    }
                })
                .execute();
    }

    public static void endCombat() {
        int endCombatCmd = CmdKit.merge(LobbyCmd.cmd, LobbyCmd.endCombat);
        RequestCommand.of(endCombatCmd)
                .setTitle("结束战斗")
                .setRequestData(() -> null)
                .execute();
    }

    public static void listenDisconnect() {
        int disconnectCmd = CmdKit.merge(LobbyCmd.cmd, LobbyCmd.disconnect);
        ListenCommand.of(disconnectCmd)
                .setTitle("心跳超时，断开与服务器的连接")
                .setCallback(result -> {
                    if (Global.getCurrentState() == GameState.COMBAT) {
                        Global.getCombatEngine().endCombat(0f, FleetSide.ENEMY);
                    }
                    NetworkState.matchState = MatchType.NOT_MATCHED;
                    NetworkState.loginState = LoginType.NOT_LOGGED_IN;
                    NetworkState.clientState = ClientType.NOT_CONNECTED;
                })
                .listen();
    }

    public static void retrunToLobbyActively() {
        int retrunToLobbyActivelyCmd = CmdKit.merge(LobbyCmd.cmd, LobbyCmd.retrunToLobbyActively);
        RequestCommand.of(retrunToLobbyActivelyCmd)
                .setTitle("手动中断战斗")
                .setRequestData(() -> null)
                .execute();
    }
}

package org.king.iogate.client.listener;

import lombok.extern.slf4j.Slf4j;
import lunalib.lunaSettings.LunaSettings;
import lunalib.lunaSettings.LunaSettingsListener;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.network.LobbyActionSet;
import org.king.iogate.client.network.NetworkClient;
import org.king.iogate.client.state.NetworkState;
import org.king.iogate.client.state.type.ClientType;
import org.king.iogate.client.state.type.LoginType;
import org.king.iogate.client.state.type.MatchType;

@Slf4j
public class TriggerListener implements LunaSettingsListener {

    public TriggerListener() {
    }

    @Override
    public void settingsChanged(String modId) {

        launchNetworkClient();

        login();

        autoMatch();

        printState();
    }

    private void launchNetworkClient() {
        boolean triggerLaunchClient = LunaSettings.getBoolean("ioGate", "launchNetworkClient");
        if (!triggerLaunchClient || NetworkState.clientState != ClientType.NOT_CONNECTED) {
            return;
        }
        NetworkState.clientState = ClientType.CONNECTING;
        NetworkClient.launch();
    }

    private void login() {
        boolean triggerLogin = LunaSettings.getBoolean("ioGate", "login");
        if (NetworkState.clientState != ClientType.CONNECTED
                || !triggerLogin
                || NetworkState.loginState != LoginType.NOT_LOGGED_IN) {
            return;
        }
        NetworkState.loginState = LoginType.LOGGING_IN;
        LobbyActionSet.login();
    }

    private void autoMatch() {
        boolean triggerAutoMatch = LunaSettings.getBoolean("ioGate", "autoMatch");
        if (NetworkState.loginState != LoginType.LOGGED_IN
                || !triggerAutoMatch
                || NetworkState.matchState != MatchType.NOT_MATCHED) {
            return;
        }
        NetworkState.matchState = MatchType.MATCHING;
        LobbyActionSet.autoMatch();
    }

    private void printState() {

        if (NetworkState.clientState == ClientType.NOT_CONNECTED) {
            log.info("未连接服务器，需要连接服务器才能进行登录操作");
            return;
        } else if (NetworkState.clientState == ClientType.CONNECTING) {
            log.info("正在连接服务器");
            return;
        } else if (NetworkState.clientState == ClientType.CONNECTED) {
            log.info("已连接服务器");
        }

        if (NetworkState.loginState == LoginType.NOT_LOGGED_IN) {
            log.info("用户未登录，需要登录成功才能进行自动匹配操作");
            return;
        } else if (NetworkState.loginState == LoginType.LOGGING_IN) {
            log.info("正在登录中");
            return;
        } else if (NetworkState.loginState == LoginType.LOGGED_IN) {
            log.info("用户已登录，本地用户ID：{}", NetworkInfoManager.localUserId);
        }

        if (NetworkState.matchState == MatchType.NOT_MATCHED) {
            log.info("用户未匹配，需要匹配成功才能开始战斗");
        } else if (NetworkState.matchState == MatchType.MATCHING) {
            log.info("用户正在匹配中");
        } else if (NetworkState.matchState == MatchType.MATCHED) {
            log.info("用户已成功匹配，请进入任务，等待战斗开始" +
                            "{}" +
                            "\t本地用户ID：{}" +
                            "{}" +
                            "\t本地用户飞船装配方案ID：{}" +
                            "{}" +
                            "\t远程用户ID：{}" +
                            "{}" +
                            "\t远程用户飞船装配方案ID：{}",
                    System.lineSeparator(),
                    NetworkInfoManager.localUserId,
                    System.lineSeparator(),
                    NetworkInfoManager.localVariantId,
                    System.lineSeparator(),
                    NetworkInfoManager.remoteUserId,
                    System.lineSeparator(),
                    NetworkInfoManager.remoteVariantId
            );
        }
    }
}
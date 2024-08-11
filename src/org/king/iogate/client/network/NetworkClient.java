package org.king.iogate.client.network;

import com.iohao.game.common.kit.concurrent.TaskKit;
import com.iohao.game.sdk.net.NetCoreSetting;
import com.iohao.game.sdk.net.NetJoinEnum;
import com.iohao.game.sdk.net.internal.DefaultCoreSetting;
import com.iohao.game.sdk.net.internal.DefaultNetServer;
import com.iohao.game.sdk.net.internal.DefaultNetServerHook;
import com.iohao.game.sdk.net.internal.config.ClientUserConfigs;
import lombok.extern.slf4j.Slf4j;
import lunalib.lunaSettings.LunaSettings;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.state.NetworkState;
import org.king.iogate.client.state.type.ClientType;

@Slf4j
public class NetworkClient {

    private NetworkClient() {
    }

    public static void launch() {

        // 开启心跳回调的日志
        ClientUserConfigs.openLogIdle = false;

        // 请求过期时间，超过该时间将强行移除相关回调处理，当值为 0 时则不启动该机制，会无限等待
        // IoGameClient.requestCommandTimeoutMilliseconds = 0;

        // 对外服IP地址
        String serverAddress = LunaSettings.getString("ioGate", "serverAddress");
        // 对外服端口
        int externalPort = LunaSettings.getInt("ioGate", "externalPort");
        // 设置
        DefaultCoreSetting setting = new DefaultCoreSetting()
                // 设置连接方式
                .setJoinEnum(NetJoinEnum.WEBSOCKET)
                /*
                 * 向服务器发送心跳，心跳周期（秒）
                 * 0 表示不开启心跳。当不为 0 时，会向服务器发送心跳消息
                 */
                .setIdlePeriod(2)
                // 远程服务器主机名或 IP 地址
                .setHost(serverAddress)
                // 远程服务器端口号
                .setPort(externalPort)
                // 钩子接口，启动成功后的回调
                .setNetServerHook(new DefaultNetServerHook() {
                    @Override
                    public void success(NetCoreSetting coreSetting) {
                        super.success(coreSetting);
                        NetworkState.clientState = ClientType.CONNECTED;
                        NetworkInfoManager.serverAddress = serverAddress;
                        NetworkInfoManager.externalPort = externalPort;
                        // 添加所有监听
                        addListenCommands();
                        log.info("成功连接服务器" +
                                        "{}" +
                                        "\t当前连接的服务器地址：{}" +
                                        "{}" +
                                        "\t当前连接的服务器端口：{}",
                                System.lineSeparator(),
                                NetworkInfoManager.serverAddress,
                                System.lineSeparator(),
                                NetworkInfoManager.externalPort);
                    }
                });

        // 启动
        TaskKit.execute(() -> new DefaultNetServer(setting).startup());
    }

    public static void addListenCommands() {
        LobbyActionSet.listenDisconnect();

        RoomActionSet.listenPushRemoteUserInfo();
        RoomActionSet.listenAllPlayersReady();
        RoomActionSet.listenUserException();
        RoomActionSet.listenPushShipAction();
    }
}

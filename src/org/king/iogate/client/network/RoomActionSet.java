package org.king.iogate.client.network;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.iohao.game.sdk.command.ListenCommand;
import com.iohao.game.sdk.command.RequestCommand;
import com.iohao.game.sdk.kit.CmdKit;
import lombok.extern.slf4j.Slf4j;
import org.king.iogate.client.manager.FrameManager;
import org.king.iogate.client.manager.NetworkInfoManager;
import org.king.iogate.client.manager.PluginManager;
import org.king.iogate.client.state.NetworkState;
import org.king.iogate.client.state.type.MatchType;
import org.king.iogate.common.protobuf.lobby.RemoteUserInfo;
import org.king.iogate.common.protobuf.room.ActionWithId;
import org.king.iogate.common.protobuf.room.ShipAction;
import org.king.iogate.common.route.RoomCmd;

import java.time.Instant;

@Slf4j
public final class RoomActionSet {

    private RoomActionSet() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void ready() {
        int readyCmd = CmdKit.merge(RoomCmd.cmd, RoomCmd.ready);
        RequestCommand.of(readyCmd)
                .setTitle("玩家进入准备状态")
                .setRequestData(() -> null)
                .execute();
    }

    public static void listenAllPlayersReady() {
        int allPlayersReadyCmd = CmdKit.merge(RoomCmd.cmd, RoomCmd.allPlayersReady);
        ListenCommand.of(allPlayersReadyCmd)
                .setTitle("所有玩家已准备")
                .setCallback(result -> {
                    CombatEngineAPI combatEngine = Global.getCombatEngine();
                    combatEngine.removePlugin(PluginManager.KEEP_PAUSE_PLUGIN);
                    combatEngine.addPlugin(PluginManager.PROHIBIT_PAUSE_PLUGIN);

                    long startTimeNano = result.getLong();
                    while (true) {
                        Instant now = Instant.now();
                        long nowNano = now.getEpochSecond() * 1_000_000_000L + now.getNano();
                        if (nowNano >= startTimeNano) {
                            break;
                        }
                    }

                    NetworkInfoManager.localShip.setShipAI(PluginManager.LOCAL_CONTROL_SHIP_AI_PLUGIN);
                    NetworkInfoManager.remoteShip.setShipAI(PluginManager.REMOTE_CONTROL_SHIP_AI_PLUGIN);

                    log.info("开始战斗");
                    log.info("Start the combat");
                })
                .listen();
    }

    public static void listenPushRemoteUserInfo() {
        int pushMatchInfoCmd = CmdKit.merge(RoomCmd.cmd, RoomCmd.pushRemoteUserInfo);
        ListenCommand.of(pushMatchInfoCmd)
                .setTitle("接收远程用户的匹配信息")
                .setCallback(result -> {
                    if (NetworkState.matchState != MatchType.MATCHING) {
                        log.info("当前监听状态为：{}，此次监听回调无效", NetworkState.matchState);
                        log.info("The current listener state is: {}, this listener callback is invalidated", NetworkState.matchState);
                        return;
                    }
                    RemoteUserInfo remoteUserInfo = result.getValue(RemoteUserInfo.class);
                    NetworkInfoManager.remoteUserId = remoteUserInfo.userId;
                    NetworkInfoManager.remoteVariantId = remoteUserInfo.variantId;
                    NetworkState.matchState = MatchType.MATCHED;
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
                            NetworkInfoManager.remoteVariantId);
                    log.info("User has been successfully matched, please enter the mission and wait for the combat to begin" +
                                    "{}" +
                                    "\tlocal user ID：[{}]" +
                                    "{}" +
                                    "\tlocal user variantId：{}" +
                                    "{}" +
                                    "\tremote user ID：[{}]" +
                                    "{}" +
                                    "\tremote user variantId：{}",
                            System.lineSeparator(),
                            NetworkInfoManager.localUserId,
                            System.lineSeparator(),
                            NetworkInfoManager.localVariantId,
                            System.lineSeparator(),
                            NetworkInfoManager.remoteUserId,
                            System.lineSeparator(),
                            NetworkInfoManager.remoteVariantId
                    );
                })
                .listen();
    }

    public static void listenUserException() {
        int userExceptionCmd = CmdKit.merge(RoomCmd.cmd, RoomCmd.userException);
        ListenCommand.of(userExceptionCmd)
                .setTitle("监听用户发生异常")
                .setCallback(result -> {
                    long userId = result.getLong();
                    log.info("ID为[{}]的用户产生异常，将结束战斗，取消匹配", userId);
                    log.info("The user with ID [{}] generates an exception that will end the combat and cancel the match", userId);
                    if (Global.getCurrentState() == GameState.COMBAT) {
                        Global.getCombatEngine().endCombat(0f, userId == NetworkInfoManager.localUserId ? FleetSide.ENEMY : FleetSide.PLAYER);
                    }
                    NetworkState.matchState = MatchType.NOT_MATCHED;
                })
                .listen();
    }

    public static void uploadShipAction(ActionWithId actionWithId) {
        int uploadShipActionCmd = CmdKit.merge(RoomCmd.cmd, RoomCmd.uploadShipAction);
        RequestCommand.of(uploadShipActionCmd)
                .setTitle("上传一帧之内，飞船的所有行为")
                .setRequestData(() -> actionWithId)
                .execute();
    }

    public static void listenPushShipAction() {
        int pushShipActionCmd = CmdKit.merge(RoomCmd.cmd, RoomCmd.pushShipAction);
        ListenCommand.of(pushShipActionCmd)
                .setTitle("接收一帧之内，飞船的所有行为")
                .setCallback(result -> {
                    ShipAction shipAction = result.getValue(ShipAction.class);
                    FrameManager.frameArray[shipAction.frameIndex] = shipAction;
                })
                .listen();
    }
}

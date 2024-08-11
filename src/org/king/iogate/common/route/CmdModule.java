package org.king.iogate.common.route;

public interface CmdModule {

    /**
     * 保留路由，用作开发测试
     */
    int debugCmd = 0;

    /**
     * 大厅模块
     */
    int lobbyCmd = 1;

    /**
     * 房间模块
     */
    int roomCmd = 2;
}

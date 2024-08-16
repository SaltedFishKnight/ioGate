# ioGate 客户端
1. 这是一个实验性 mod，使用 ioGate 之前，请创建一个原版游戏的副本，以下所有操作请在副本上进行
2. 加入 [Discord](https://discord.gg/NC7J5DUURy)，下载并安装对应版本的 unlock.jar
    * unlock.jar 提供者：[tomatopaste](https://fractalsoftworks.com/forum/index.php?action=profile;u=10783)
    * [相关帖子](https://fractalsoftworks.com/forum/index.php?topic=24577.0)
3. 按照这篇[帖子](https://fractalsoftworks.com/forum/index.php?topic=29320.0)为游戏安装 JDK 23
4. 修改原始的 Miko_R3.txt，将 “--enable-preview” 之后的配置，覆盖为以下配置
    > --enable-preview  
    > --add-opens=java.base/sun.nio.ch=ALL-UNNAMED  
    > --add-opens=java.base/java.nio=ALL-UNNAMED  
    > --add-opens=java.base/java.util=ALL-UNNAMED  
    > --add-opens=java.base/java.util.concurrent=ALL-UNNAMED  
    > --add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED  
    > --add-opens=java.base/jdk.internal.ref=ALL-UNNAMED  
    > --add-opens=java.base/java.lang.reflect=ALL-UNNAMED  
    > --add-opens=java.base/java.lang.ref=ALL-UNNAMED  
    > --add-opens=java.base/java.lang=ALL-UNNAMED  
    > --add-opens=java.management/javax.management=ALL-UNNAMED  
    > --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED  
    > --add-opens=java.base/java.text=ALL-UNNAMED  
    > --add-opens=java.desktop/java.awt.font=ALL-UNNAMED  
    > --add-opens=java.desktop/java.awt=ALL-UNNAMED  
    > --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED  
    > -Xms2048m  
    > -Xmx2048m  
    > -Xss4m  
    > -classpath  
    > ..\\mods/unlock.jar;..\\mikohime/janino-3.0.12.jar;..\\mikohime/commons-compiler-3.0.12.jar;..\\mikohime/commons-compiler-jdk-3.0.12.jar;starfarer.api.jar;..\\mikohime/port_obf.jar;..\\mikohime/jcraft-jorbis-0.0.17.jar;json.jar;..\\mikohime/lwjgl.jar;..\\mikohime/jinput.jar;..\\mikohime/log4j-api-3.0.0-alpha1.jar;..\\mikohime/log4j-1.2-api-3.0.0-alpha1.jar;..\\mikohime/log4j-core-3.0.0-alpha1.jar;..\\mikohime/log4j-plugins-3.0.0-alpha1.jar;..\\mikohime/disruptor-4.0.0.jar;..\\mikohime/lwjgl_util.jar;..\\mikohime/fs.sound_obf_bufferlarger.jar;..\\mikohime/port.common_obf.jar;..\\mikohime/xstream-1.4.17.jar;..\\mikohime/jaxb-api-2.4.0-b180830.0359.jar;..\\mikohime/txw2-3.0.2.jar  
    > -Dcom.fs.starfarer.settings.paths.saves=..\\saves  
    > -Dcom.fs.starfarer.settings.paths.screenshots=..\\screenshots  
    > -Dcom.fs.starfarer.settings.paths.mods=..\\mods  
    > -Dcom.fs.starfarer.settings.paths.logs=.  
    > -Dfile.encoding=GBK  
    > -Dorg.slf4j.simpleLogger.logFile=System.out  
    > -Dorg.slf4j.simpleLogger.showDateTime=true  
    > -Dorg.slf4j.simpleLogger.dateTimeFormat=[yyyy-MM-dd|HH:mm:ss.SSS]  
    > -Dorg.slf4j.simpleLogger.levelInBrackets=true  
    > -Dio.netty.tryReflectionSetAccessible=true  
    > com.fs.starfarer.StarfarerLauncher  
5. 根据你的需求修改 -Xms，-Xmx 和 -Dfile.encoding
6. 下载 ZIP，解压后将文件夹重命名为 ioGate，将 ioGate 文件夹放入 mods 文件夹下
7. 请使用 Miko_Rouge.bat 启动游戏，如果使用 Miko_Silent.bat 启动游戏，将不会显示任何状态信息
8. 在游戏主界面的左上角，使用 LunaLib 设置 ioGate 相关参数
9.（可选）如果 LunaLib 和任务描述中存在乱码，则需要下载并安装中文字库：https://www.fossic.org/thread-177-1-1.html

***

# ioGate Client
1. This is an experimental mod, before using ioGate, please create a copy of the original game, and all the following operations should be performed on the copy
2. Join [Discord](https://discord.gg/NC7J5DUURy) to download and install the corresponding version of unlock.jar
    * The provider of unlock.jar: [tomatopaste](https://fractalsoftworks.com/forum/index.php?action=profile;u=10783)
    * [Related post](https://fractalsoftworks.com/forum/index.php?topic=24577.0)
3. Follow this [post](https://fractalsoftworks.com/forum/index.php?topic=29320.0) to install JDK 23 for the game
4. Modify the original Miko_R3.txt to override the configuration after "--enable-preview" with the following configuration
    > --enable-preview  
    > --add-opens=java.base/sun.nio.ch=ALL-UNNAMED  
    > --add-opens=java.base/java.nio=ALL-UNNAMED  
    > --add-opens=java.base/java.util=ALL-UNNAMED  
    > --add-opens=java.base/java.util.concurrent=ALL-UNNAMED  
    > --add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED  
    > --add-opens=java.base/jdk.internal.ref=ALL-UNNAMED  
    > --add-opens=java.base/java.lang.reflect=ALL-UNNAMED  
    > --add-opens=java.base/java.lang.ref=ALL-UNNAMED  
    > --add-opens=java.base/java.lang=ALL-UNNAMED  
    > --add-opens=java.management/javax.management=ALL-UNNAMED  
    > --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED  
    > --add-opens=java.base/java.text=ALL-UNNAMED  
    > --add-opens=java.desktop/java.awt.font=ALL-UNNAMED  
    > --add-opens=java.desktop/java.awt=ALL-UNNAMED  
    > --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED  
    > -Xms2048m  
    > -Xmx2048m  
    > -Xss4m  
    > -classpath  
    > ..\\mods/unlock.jar;..\\mikohime/janino-3.0.12.jar;..\\mikohime/commons-compiler-3.0.12.jar;..\\mikohime/commons-compiler-jdk-3.0.12.jar;starfarer.api.jar;..\\mikohime/port_obf.jar;..\\mikohime/jcraft-jorbis-0.0.17.jar;json.jar;..\\mikohime/lwjgl.jar;..\\mikohime/jinput.jar;..\\mikohime/log4j-api-3.0.0-alpha1.jar;..\\mikohime/log4j-1.2-api-3.0.0-alpha1.jar;..\\mikohime/log4j-core-3.0.0-alpha1.jar;..\\mikohime/log4j-plugins-3.0.0-alpha1.jar;..\\mikohime/disruptor-4.0.0.jar;..\\mikohime/lwjgl_util.jar;..\\mikohime/fs.sound_obf_bufferlarger.jar;..\\mikohime/port.common_obf.jar;..\\mikohime/xstream-1.4.17.jar;..\\mikohime/jaxb-api-2.4.0-b180830.0359.jar;..\\mikohime/txw2-3.0.2.jar  
    > -Dcom.fs.starfarer.settings.paths.saves=..\\saves  
    > -Dcom.fs.starfarer.settings.paths.screenshots=..\\screenshots  
    > -Dcom.fs.starfarer.settings.paths.mods=..\\mods  
    > -Dcom.fs.starfarer.settings.paths.logs=.  
    > -Dfile.encoding=GBK  
    > -Dorg.slf4j.simpleLogger.logFile=System.out  
    > -Dorg.slf4j.simpleLogger.showDateTime=true  
    > -Dorg.slf4j.simpleLogger.dateTimeFormat=[yyyy-MM-dd|HH:mm:ss.SSS]  
    > -Dorg.slf4j.simpleLogger.levelInBrackets=true  
    > -Dio.netty.tryReflectionSetAccessible=true  
    > com.fs.starfarer.StarfarerLauncher  
5. Modify -Xms, -Xmx and -Dfile.encoding according to your needs
6. Download the ZIP. Unzip it and rename the folder to ioGate. Put the ioGate folder under the mods folder
7. Please use Miko_Rouge.bat to start the game. If you use Miko_Silent.bat to start the game, it will not show any status message
8. In the upper left corner of the main game interface, use LunaLib to set the ioGate parameters
9. (Optional) If there is garbled code in LunaLib and mission description, you need to download and install the [Chinese text library](https://www.fossic.org/thread-177-1-1.html)

package org.king.iogate.client.manager;

import org.king.iogate.client.plugin.ai.EmptyShipAIPlugin;
import org.king.iogate.client.plugin.ai.LocalControlShipAIPlugin;
import org.king.iogate.client.plugin.ai.RemoteControlShipAIPlugin;
import org.king.iogate.client.plugin.combat.*;


// TODO 拆分 ShipAIPlugin、EveryFrameCombatPlugin，并使用 enum 代替 class（枚举值代替静态常量）
public class PluginManager {

    public static final EmptyShipAIPlugin EMPTY_SHIP_AI_PLUGIN = new EmptyShipAIPlugin();

    public static final LocalControlShipAIPlugin LOCAL_CONTROL_SHIP_AI_PLUGIN = new LocalControlShipAIPlugin();

    public static final RemoteControlShipAIPlugin REMOTE_CONTROL_SHIP_AI_PLUGIN = new RemoteControlShipAIPlugin();

    public static final InitPlugin INIT_PLUGIN = new InitPlugin();

    public static final KeepPausePlugin KEEP_PAUSE_PLUGIN = new KeepPausePlugin();

    public static final ProhibitPausePlugin PROHIBIT_PAUSE_PLUGIN = new ProhibitPausePlugin();

    public static final EndCombatPlugin END_COMBAT_PLUGIN = new EndCombatPlugin();

    public static final InterruptCombatPlugin INTERRUPT_COMBAT_PLUGIN = new InterruptCombatPlugin();

    public static final AutofireDeferProcessPlugin AUTOFIRE_DEFER_PROCESS_PLUGIN = new AutofireDeferProcessPlugin();

}

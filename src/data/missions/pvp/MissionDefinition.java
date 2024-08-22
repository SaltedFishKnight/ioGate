package data.missions.pvp;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;
import org.king.iogate.client.manager.PluginManager;

public class MissionDefinition implements MissionDefinitionPlugin {

    @Override
    public void defineMission(MissionDefinitionAPI api) {

        api.initFleet(FleetSide.PLAYER, "Player", FleetGoal.ATTACK, false);
        api.initFleet(FleetSide.ENEMY, "Enemy", FleetGoal.ATTACK, false);

        api.setFleetTagline(FleetSide.PLAYER, "Player");
        api.setFleetTagline(FleetSide.ENEMY, "Enemy");

        float width = 12000f;
        float height = 12000f;
        api.initMap(-width / 2f, width / 2f, -height / 2f, height / 2f);

        // 在任务描述中“战术目标”下的项目符号列表中添加一行
        //addBriefingItem(java.lang.String item)

        // 失去指定名字的飞船会导致该方立即失败
        //api.defeatOnShipLoss("Main");

        api.addPlugin(PluginManager.INIT_PLUGIN);

    }
}
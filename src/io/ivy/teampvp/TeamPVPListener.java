package io.ivy.teampvp;

import java.util.List;
import net.canarymod.Canary;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Anvil;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Sign;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.EntitySpawnHook;
import net.canarymod.hook.player.BlockRightClickHook;
import net.canarymod.hook.world.ChunkLoadedHook;
import net.canarymod.plugin.PluginListener;

public class TeamPVPListener implements PluginListener {
	
	private Sign team_one_sign, team_two_sign;
	private Anvil team_one_flag, team_two_flag;
	
	@SuppressWarnings("deprecation")
	@HookHandler
	public void onBlockRightClickHook(BlockRightClickHook hook) {
		World the_world = hook.getPlayer().getWorld();
		if (the_world.getFqName().equals("pvptest_NORMAL")) {
			Block the_block = hook.getBlockClicked();
			if (the_block.getType().equals(BlockType.SignPost)) {
				Sign the_sign = (Sign) the_block.getTileEntity();
				if (the_sign.getTextOnLine(0).equals("gametest")) {
					Canary.log.info("u wot m8?");
				}
			}
		}
	}
}

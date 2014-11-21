package io.ivy.teampvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.base.Predicate;

import net.canarymod.Canary;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.EntityType;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.factory.ItemFactory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.scoreboard.Scoreboard;
import net.canarymod.api.scoreboard.Team;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Sign;
import net.canarymod.api.world.blocks.TileEntity;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.BlockRightClickHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.hook.player.PlayerRespawnedHook;
import net.canarymod.hook.system.PluginEnableHook;
import net.canarymod.plugin.PluginListener;

public class TeamPVPListener implements PluginListener {
	
	List<Player> team_red, team_blue = new ArrayList();

	
	
	
	
	@HookHandler
	public void onPlayerDeathHook(PlayerDeathHook hook) {
		// Score
	}
	
	
	@HookHandler
	public void onPlayerRespawnedHook(PlayerRespawnedHook hook) {
		Canary.log.info(hook.toString());
		if (hook.getPlayer().getWorld().getFqName().equals("ctf_NORMAL")) {
			Player the_player = hook.getPlayer();
			
			int sig = Long.signum((long)team_red.size() - team_blue.size());
			String[] red_title = {"/title @p title {text:\"Welcome To CTF\",color:red}"};
			String[] blue_title = {"/title @p title {text:\"Welcome to CTF\",color:blue}"};
			
			switch(sig) {
				case -1:
					team_red.add(the_player);
					the_player.executeCommand(red_title);
					break;
				case 0:
					team_blue.add(the_player);
					the_player.executeCommand(red_title);
					break;
				case 1:
					team_blue.add(the_player);
					the_player.executeCommand(blue_title);
					break;
			}
			reset_inventory(the_player);
		}
	}

	private void reset_inventory(Player the_player) {
		ItemFactory fac = Canary.factory().getItemFactory();
		
		the_player.getInventory().clearContents();
		
		Item a_sign = fac.newItem(ItemType.Sign);
		the_player.getInventory().addItem(a_sign);
	}
	
	
	/*
	Predicate<TileEntity> block_is_sign = new Predicate<TileEntity> () {
		public boolean apply(TileEntity the_block) {
			return the_block.getBlock().getType().equals(BlockType.SignPost);
		}
	};
	
	Predicate<Entity> entity_is_chicken = new Predicate<Entity>() {
		public boolean apply(Entity the_entity) {
			return the_entity.getEntityType().equals(EntityType.CHICKEN);
		}
	};
	*/

	/*
	@SuppressWarnings("deprecation")
	@HookHandler
	public void onBlockRightClickHook(BlockRightClickHook hook) {
		if (hook.getBlockClicked().getType().equals(BlockType.SignPost) &&
				hook.getPlayer().getWorld().getFqName().equals("ctf_NORMAL")) {
			Sign sign = (Sign) hook.getBlockClicked().getTileEntity();
			if (sign.getTextOnLine(0).equals("column") &&
					sign.getTextOnLine(1).length() > 0) {
				int count = Integer.parseInt(sign.getTextOnLine(1));
				make_column(BlockType.Stone, count, sign.getBlock());
			}
			if (sign.getTextOnLine(0).equals("fill")) {
				fill_hole(BlockType.Dirt, sign.getBlock());
			}
			if (sign.getTextOnLine(0).equals("testing")) {
				Block sign_block = sign.getBlock();
				
				List<Block> the_blocks = Arrays.asList(
					sign_block.getRelative(-1,-1, 0),
					sign_block.getRelative(0,-1,-1),						
					sign_block.getRelative(1,-1, 0),
					sign_block.getRelative(0,-1, 1),
					sign_block.getRelative(0,-1, 0),
					sign_block.getRelative(-1,-1,-1),
					sign_block.getRelative(1,-1, 1),
					sign_block.getRelative(-1,-1,1),
					sign_block.getRelative(1,-1,-1));

				the_blocks.forEach(new Consumer<Block>() {
					@Override
					public void accept(Block the_block) {
						the_block.setType(BlockType.WoolBlue);
						the_block.update();
					}
				});
			}
		}
	}
	*/
	
	/*
	private void fill_hole(BlockType fill_type, Block reference) {		
		for (int x = 0; x < 100; x++) {
			for (int z = 0; z < 100; z++) {
				Block nb = reference.getRelative(x, 0, z);
				if (nb.getType().equals(BlockType.Air)) {
					nb.setType(fill_type);
					nb.update();
				} else {
					break;
				}
			}
		}		
	}
	private void make_column(BlockType block_type, int blocks_high, Block reference) {
		List<Block> block_list = new ArrayList();
		for (int i = 0; i < blocks_high; i++) {
			block_list.add(reference.getRelative(0, i, 0));
		}
		block_list.forEach(new Consumer<Block>() {
			@Override
			public void accept(Block the_block) {
				the_block.setType(block_type);
				the_block.update();
			}
		});
	}
	*/
	/*
	@HookHandler
	public void onChunkLoadedHook(ChunkLoadedHook hook) {
		if (hook.getWorld().getFqName().equals("ctf_NORMAL")) {
			Collection<TileEntity> tiles = hook.getChunk().getTileEntityMap().values();
			
			Stream<TileEntity> all_banners = tiles.stream()
					.filter(block -> block.getBlock().getType().equals(BlockType.StandingBanner));
			
			all_banners.forEach(new Consumer<TileEntity>() {
				@Override
				public void accept(TileEntity block) {					
					// find the nearest sign, check its team, and
					// mark this anvil as that team's objective.
					
				}
			});
		}
	}
	*/
	
	/*
	@HookHandler
	public void onPluginEnableHook(PluginEnableHook hook) {
		Scoreboard scoreboard = Canary.scoreboards().getScoreboard();
		
		Team team_red = scoreboard.getTeam("Red");
		Team team_blue = scoreboard.getTeam("Blue");
		
		team_red.setDisplayName("Team Red");
		team_blue.setDisplayName("Team Blue");

		team_red.setPrefix("Red");
		team_blue.setPrefix("Blue");
		
		this.team_red = team_red;
		this.team_blue = team_blue;
	}
	*/
}

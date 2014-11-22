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
import net.canarymod.api.nbt.CompoundTag;
import net.canarymod.api.scoreboard.Scoreboard;
import net.canarymod.api.scoreboard.Team;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Sign;
import net.canarymod.api.world.blocks.TileEntity;
import net.canarymod.api.world.blocks.CommandBlock;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.BlockRightClickHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.hook.player.PlayerRespawnedHook;
import net.canarymod.hook.system.PluginEnableHook;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.commandsys.Command;

public class TeamPVPListener implements PluginListener {
	
    private List<Player> team_red = new ArrayList();
    private List<Player> team_blue = new ArrayList();
    
    @HookHandler
    public void onPlayerDeathHook(PlayerDeathHook hook) {
        // +1 for the good guys!
        // whoever they happen to be.
    }

    @HookHandler
    public void onBlockRightClickHook(BlockRightClickHook hook) {
        if (hook.getPlayer().getWorld().getFqName().equals("ctf_NORMAL")) {
            if (hook.getBlockClicked().getType().equals(BlockType.SignPost)) {
                Player the_player = hook.getPlayer();
                Sign the_sign = (Sign) hook.getBlockClicked().getTileEntity();
                
                if (the_sign.getTextOnLine(0).equals("Inventory")) {
                    reset_inventory(the_player);
                }
                
                if (the_sign.getTextOnLine(0).equals("testing")) {
                    if (team_red.contains(the_player)) {
                        the_player.notice("Red");
                    }
                    if (team_blue.contains(the_player)) {
                        the_player.notice("Blue");
                    }
                }
            }
        }
    }

    @HookHandler
    public void onPlayerRespawnedHook(PlayerRespawnedHook hook) {
        Canary.log.info(hook.toString());

        if (hook.getPlayer().getWorld().getFqName().equals("ctf_NORMAL")) {
            Player the_player = hook.getPlayer();
            
            int sig = Long.signum((long)team_red.size() - team_blue.size());
            String[] red_title = {"/title @p title {text:\"Welcome To CTF\",color:red}"};
            String[] blue_title = {"/title @p title {text:\"Welcome to CTF\",color:blue}"};

            if (!team_red.contains(the_player) && !team_blue.contains(the_player)) {
                switch(sig) {
                case -1:
                    team_red.add(the_player);
                    break;
                case 0:
                    team_blue.add(the_player);
                    break;
                case 1:
                    team_blue.add(the_player);
                    break;
                }
            }

            if (team_blue.contains(the_player)) {
                the_player.executeCommand(blue_title);
            }
            
            if (team_red.contains(the_player)) {
                the_player.executeCommand(red_title);
            }
            
            reset_inventory(the_player);
        }
    }

    @HookHandler
    public void onBlockPlaceHook(BlockPlaceHook hook) {
        Player the_player = hook.getPlayer();
        Block the_block = hook.getBlockPlaced();

        if (hook.getPlayer().getWorld().getFqName().equals("ctf_NORMAL")) {
            the_player.notice(the_block.getType().getMachineName());
        }
        
        // TileEntity the_entity = the_block.getTileEntity();
        // CompoundTag the_tag = the_entity.getDataTag();
        // the_tag.set("Color","red");
        // the_block.update();
    }

    private void reset_inventory(Player the_player) {
        ItemFactory fac = Canary.factory().getItemFactory();
        
        the_player.getInventory().clearContents();

        Item a_sign = fac.newItem(ItemType.Sign);
        Item a_sword = fac.newItem(ItemType.IronSword);
        Item a_banner = fac.newItem(ItemType.Banner);
        
        the_player.getInventory().addItem(a_sign);
        the_player.getInventory().addItem(a_sword);
        the_player.getInventory().addItem(a_banner);
        the_player.getInventory().update();
    }

    private List<Player> team_for_player(Player the_player) {
        if (team_red.contains(the_player)) {
            return team_red;
        }
        if (team_blue.contains(the_player)) {
            return team_blue;
        }
        return null;
    }
}

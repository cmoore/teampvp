package io.ivy.teampvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.function.Consumer;

import com.google.common.base.*;
//import com.google.common.collect.*;

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
import net.canarymod.api.world.blocks.*;
import net.canarymod.api.world.blocks.properties.*;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.*;
import net.canarymod.hook.system.PluginEnableHook;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.commandsys.Command;

public class TeamPVPListener implements PluginListener {
	
    private List<Player> team_red = new ArrayList();
    private List<Player> team_blue = new ArrayList();

    private Boolean red_has_planted = false;
    private Boolean blue_has_planted = false;

    
    @HookHandler
    public void onPlayerDeathHook(PlayerDeathHook hook) {
        // +1 for the good guys!
        // whoever they happen to be.
    }

    private void plant_flag(Player the_player, Block the_block, List<Player> the_team) {

        
        // Paint a square.
        
    }
    
    @HookHandler
    public void onBlockRightClickHook(BlockRightClickHook hook) {
        
        if (hook.getPlayer().getWorld().getFqName().equals("ctf_NORMAL")) {

            // hook.getBlockClicked().getPropertyKeys().forEach(new Consumer<BlockProperty>() {
            //         @Override
            //         public void accept(BlockProperty prop) {
            //             hook.getPlayer().notice("P: ".concat(prop.getName()));
            //         }});

            Player the_player = hook.getPlayer();
            Block the_block = hook.getBlockClicked();

            if (the_block.getType().equals(BlockType.StandingBanner)) {
                the_player.notice("Banner");
                
                if (team_red.contains(the_player)) {
                    // RED LEADER REPORTING IN
                    the_player.notice("Red");
                    plant_flag(the_player, the_block, team_red);
                }

                if (team_blue.contains(the_player)) {
                    // BLUE LEADER REPORTING IN
                    the_player.notice("Blue");
                    plant_flag(the_player, the_block, team_blue);
                }
            }
            
            if (hook.getBlockClicked().getType().equals(BlockType.SignPost)) {
                
                Sign the_sign = (Sign) the_block;
                
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

        if (is_level(the_player, "ctf_NORMAL")) {
            the_player.notice("BPH: ".concat(the_block.getType().getMachineName()));
            if (is_leader(the_player) != null) {
                the_player.notice("Leader and Command.");
                
                    
                    // the_block.getTileEntity().getDataTag().keySet().forEach(new Consumer<String>() {
                    //         @Override
                    //         public void accept(String fx) {
                    //             the_player.notice("D: ".concat(fx));
                    //         }});
                    // the_block.getTileEntity().getMetaTag().keySet().forEach(new Consumer<String>() {
                    //         @Override
                    //         public void accept(String fx) {
                    //             the_player.notice("M: ".concat(fx));
                    //         }});
            }
        }

        
        // TileEntity the_entity = the_block.getTileEntity();
        // CompoundTag the_tag = the_entity.getDataTag();
        // the_tag.set("Color","red");
        // the_block.update();
    }

    private List<Player> is_leader(Player the_player) {

        if (team_red.contains(the_player)) {
            if (team_red.indexOf(the_player) == 0) {
                return team_red;
            }
        }

        if (team_blue.contains(the_player)) {
            if (team_blue.indexOf(the_player) == 0) {
                return team_blue;
            }
        }
        return null;
    }
    
    private void reset_inventory(Player the_player) {
        
        ItemFactory fac = Canary.factory().getItemFactory();
        
        the_player.getInventory().clearContents();

        Item a_sign = fac.newItem(ItemType.Sign);
        Item a_sword = fac.newItem(ItemType.IronSword);
                
        the_player.getInventory().addItem(a_sign);
        the_player.getInventory().addItem(a_sword);

        if (is_leader(the_player) != null) {
            the_player.getInventory().addItem( fac.newItem(ItemType.CommandBlock) );
        }

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

    private boolean is_level(Player the_player, String level_name) {
        
        return the_player.getWorld().getFqName().equals(level_name);
    }
            
}

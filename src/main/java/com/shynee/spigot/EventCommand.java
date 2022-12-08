package com.shynee.spigot;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventCommand implements CommandExecutor, Listener {

    TNTPrimed tnt;
    Player ePlayer;
    FileConfiguration config = Spigot.instance.config;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (config.getBoolean("Item.OnlyOpRunCommand") && !player.isOp()) {
                player.sendMessage(ChatColor.RED + "You do not have permission to run this command");
            } else {
                player.getInventory().addItem(ItemManager.wand);
                player.sendMessage("You have successfully been given " + ChatColor.GOLD + config.get("Item.Name"));

            }
        } else {
            System.out.println("nice try dumbass");
        }
        return true;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Player player = e.getPlayer();
            ePlayer = e.getPlayer();
            if (player.getInventory().getItemInMainHand().equals(ItemManager.wand)) {

                player.setInvulnerable(true);

                player.setVelocity(player.getLocation().getDirection().multiply(-config.getDouble("Player.LaunchVelocity")));
                if (config.getBoolean("CreateExplosion.Enabled")) {
                    tnt = player.getWorld().spawn(e.getClickedBlock().getLocation(), TNTPrimed.class);
                    tnt.setFuseTicks(0);
                }

                    new BukkitRunnable(){

                        @Override
                        public void run() {

                            if (!config.getBoolean("Player.TakeFallDamage")) {
                                player.setInvulnerable(false);
                                if (!player.isOnGround()) {
                                    player.setFallDistance(-1000);

                                } else if (!player.getInventory().getItemInMainHand().equals(ItemManager.wand)) {
                                    player.setFallDistance(0);
                                    cancel();
                                } else {
                                    player.setFallDistance(0);
                                    cancel();
                                }
                            }
                            else{
                                player.setInvulnerable(false);
                                cancel();
                            }

                        }
                    }.runTaskTimer(Spigot.instance, 10, 0);
                }
                }
            }

    @EventHandler
    public void blockExplode(EntityExplodeEvent e) {
        if (config.getBoolean("CreateExplosion.LaunchBlocks")) {
            if (e.getEntity().equals(tnt)) {
                List<Block> blockList = e.blockList();
                List<FallingBlock> f = new ArrayList<>();
                e.setYield(0);
                List<BlockData> blocks = new ArrayList<>();
                for (Block b : blockList){
                    blocks.add(b.getBlockData());
                }

                for (int i = 0; i < blockList.size(); i++) {
                    FallingBlock fall = e.getEntity().getWorld().spawnFallingBlock(blockList.get(i).getLocation(), blockList.get(i).getBlockData());
                    fall.setGravity(true);
                    fall.setDropItem(false);
                    Random r = new Random();
                    float random = -1f + r.nextFloat() * (-2.5f + 1f);
                    fall.setVelocity(ePlayer.getLocation().getDirection().multiply(random));
                    fall.setHurtEntities(true);
                    f.add(fall);
                    i = i + 1;
                }

                new BukkitRunnable(){
                    int max = blockList.size() -1;
                    @Override
                    public void run() {
                        if (blockList.size() > 0){
                            Block b = blockList.get(0);
                            Location loc = b.getLocation();
                            loc.getBlock().setType(blocks.get(0).getMaterial());
                            blocks.remove(0);
                            blockList.remove(0);
                        }
                        else{
                            cancel();
                        }
                    }
                }.runTaskTimer(Spigot.instance, 0, 0);
            }
        }
    }
}

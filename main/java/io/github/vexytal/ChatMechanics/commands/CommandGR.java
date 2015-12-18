package io.github.vexytal.ChatMechanics.commands;

import io.github.vexytal.Main;
import io.github.vexytal.ChatMechanics.ChatMechanics;
import io.github.vexytal.CommunityMechanics.CommunityMechanics;
import io.github.vexytal.EcashMechanics.EcashMechanics;
import io.github.vexytal.PermissionMechanics.PermissionMechanics;
import io.github.vexytal.TutorialMechanics.TutorialMechanics;
import io.github.vexytal.jsonlib.JSONMessage;
import io.github.vexytal.managers.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandGR implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player pl = (Player)sender;
        if (pl == null) return true;

        if (args.length <= 0) {
            pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incorrect syntax. You must supply a message! " + ChatColor.BOLD + "/gr <MESSAGE>");
            return true;
        }

        if(TutorialMechanics.onTutorialIsland(pl) && !(pl.isOp())) {
            pl.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " chat while on tutorial island.");
            pl.sendMessage(ChatColor.GRAY + "Either finish the tutorial or type /skip to enable chat.");
            return true;
        }

        if(ChatMechanics.mute_list.containsKey(pl.getName())) {
            long time_left = ChatMechanics.mute_list.get(pl.getName());
            pl.sendMessage(ChatColor.RED + "You are currently " + ChatColor.BOLD + "GLOBALLY MUTED" + ChatColor.RED + ". You will be unmuted in " + time_left + " minute(s).");
            return true;
        }

        String msg = "";

        for(String s : args) {
            msg += s + " ";
        }

        String rank = PermissionMechanics.getRank(pl.getName());

        if(PlayerManager.getPlayerModel(pl).getGlobalChatDelay() != 0) {
            long old_time = PlayerManager.getPlayerModel(pl).getGlobalChatDelay();
            long cur_time = System.currentTimeMillis();

            int personal_delay = ChatMechanics.GChat_Delay;
            ItemStack global_amp = EcashMechanics.tickGlobalAmplifier(pl);
            if(global_amp != null) {
                // They have one!
                personal_delay *= 0.50D;

                // EcashMechanics.setMessagesLeftOnGlobalAmplifier(global_amp,
                // -1, true);
                // It will subtract from the item in getGlobalAmplifier.
            }

            if((cur_time - old_time) < (personal_delay * 1000) && !(pl.isOp()) && !(rank.equalsIgnoreCase("GM")) && !(rank.equalsIgnoreCase("PMOD") && !(rank.equalsIgnoreCase("WD")))) {
                int s_delay_left = personal_delay - (int) ((cur_time - old_time) / 1000);
                pl.sendMessage(ChatColor.RED + "You can send another GLOBAL MESSAGE in " + s_delay_left + ChatColor.BOLD + "s");
                return true;
            }
        }

        PlayerManager.getPlayerModel(pl).setGlobalChatDelay(System.currentTimeMillis());

        String prefix = ChatMechanics.getPlayerPrefix(pl);
        String message = ChatMechanics.fixCapsLock(msg);
            if (PlayerManager.getPlayerModel(pl).getToggleList() != null && PlayerManager.getPlayerModel(pl).getToggleList().contains("guild")) {
                pl.sendMessage(ChatColor.RED + "You currently have global messaging " + ChatColor.BOLD + "DISABLED." + ChatColor.RED
                        + " Type '/toggleglobal' to re-enable.");
                return true;
            }
        JSONMessage filter = null;
        JSONMessage normal = null;
        String aprefix = pl.getName() + ": " + ChatColor.WHITE;
        if(message.contains("@i@") && pl.getItemInHand().getType() != Material.AIR) {
            String[] split = message.split("@i@");
            String after = "";
            String before = "";
            if(split.length > 0) before = split[0];
            if(split.length > 1) after = split[1];

            normal = new JSONMessage(prefix + ChatColor.WHITE + aprefix, ChatColor.WHITE);
            normal.addText(before + " ");
            normal.addItem(pl.getItemInHand(), ChatColor.BOLD + "SHOW", ChatColor.UNDERLINE);
            normal.addText(after);

            filter = new JSONMessage(prefix + ChatColor.WHITE + aprefix, ChatColor.WHITE);
            filter.addText(ChatMechanics.censorMessage(before) + " ");
            filter.addItem(pl.getItemInHand(), ChatColor.BOLD + "SHOW", ChatColor.UNDERLINE);
            filter.addText(ChatMechanics.censorMessage(after));
        }

        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            if(CommunityMechanics.isPlayerOnIgnoreList(pl, p.getName()) || CommunityMechanics.isPlayerOnIgnoreList(pl, p.getName())) {
                continue; // Either sender has the sendee ignored or vice versa, no need for them to be able to see each other's messages.
            }
            if(PlayerManager.getPlayerModel(p).getToggleList() != null && PlayerManager.getPlayerModel(p).getToggleList().contains("global")) {
                continue; // They have global off, and only want to hear from their buds.
            }
            if(PlayerManager.getPlayerModel(p).getToggleList() != null && PlayerManager.getPlayerModel(p).getToggleList().contains("guildchat")) {
            	continue; // They have guildchat off, so stop them from receiving the messages.
            }
            if(TutorialMechanics.onTutorialIsland(p)) {
                continue; // Don't send any outside chat to players on tutorial island.
            }

            if(normal != null){
                JSONMessage toSend = normal;
                if(ChatMechanics.hasAdultFilter(p.getName())) {
                    toSend = filter;
                }
                ChatColor p_color = ChatMechanics.getPlayerColor(pl, p);
                toSend.setText(ChatColor.RED + "<" + ChatColor.BOLD + "GR" + ChatColor.RED + ">" + " " + prefix + p_color + aprefix);
                toSend.sendToPlayer(p);
            }else{
                ChatColor p_color = ChatMechanics.getPlayerColor(pl, p);
                p.sendMessage(ChatColor.RED + "<" + ChatColor.BOLD + "GR" + ChatColor.RED + ">" + " " + prefix + p_color + aprefix + message);
            }
        }

        Main.log.info(ChatColor.stripColor("" + "<" + "GR" + ">" + " " + prefix + pl.getName() + ": " + msg));
        return true;
    }
}

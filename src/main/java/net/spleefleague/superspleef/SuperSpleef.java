/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.spleefleague.superspleef;

import com.mongodb.DB;
import net.spleefleague.core.SpleefLeague;
import net.spleefleague.core.chat.ChatChannel;
import net.spleefleague.core.chat.ChatManager;
import net.spleefleague.core.command.CommandLoader;
import net.spleefleague.core.player.PlayerManager;
import net.spleefleague.core.player.Rank;
import net.spleefleague.core.plugin.CorePlugin;
import net.spleefleague.superspleef.game.Arena;
import net.spleefleague.superspleef.game.BattleManager;
import net.spleefleague.superspleef.listener.ConnectionListener;
import net.spleefleague.superspleef.listener.GameListener;
import net.spleefleague.superspleef.player.SpleefPlayer;
import org.bukkit.ChatColor;

/**
 *
 * @author Jonas
 */
public class SuperSpleef extends CorePlugin {

    private static SuperSpleef instance;
    private PlayerManager<SpleefPlayer> playerManager;
    private BattleManager battleManager;
    
    public SuperSpleef() {
        super("[SuperSpleef]", ChatColor.GRAY + "[" + ChatColor.GOLD + "SuperSpleef" + ChatColor.GRAY + "]" + ChatColor.RESET);
    }
    
    @Override
    public void start() {
        instance = this;
        Arena.initialize();
        this.playerManager = new PlayerManager(this, SpleefPlayer.class);
        this.battleManager = new BattleManager();
        ChatManager.registerPublicChannel(new ChatChannel("GAME_MESSAGE_SPLEEF", Rank.DEFAULT, true));
        ConnectionListener.init();
        GameListener.init();
        CommandLoader.loadCommands(this, "net.spleefleague.superspleef.commands");
    }

    @Override
    public DB getPluginDB() {
        return SpleefLeague.getInstance().getMongo().getDB("SuperSpleef");
    }   
    
    public PlayerManager<SpleefPlayer> getPlayerManager() {
        return playerManager;
    }
    
    public BattleManager getBattleManager() {
        return battleManager;
    }
    
    public static SuperSpleef getInstance() {
        return instance;
    }
}
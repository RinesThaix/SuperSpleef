/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.superspleef.player;

import com.spleefleague.core.io.DBLoad;
import com.spleefleague.core.io.DBSave;
import com.spleefleague.core.io.Settings;
import com.spleefleague.core.queue.RatedPlayer;
import com.spleefleague.superspleef.SuperSpleef;
import com.spleefleague.superspleef.game.Arena;
import com.spleefleague.superspleef.game.SpleefBattle;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jonas
 */
public class SpleefPlayer extends RatedPlayer {

    private int rating, playTo;
    private boolean ingame, frozen, requestingReset, requestingEndgame;
    private Set<Arena> visitedArenas;
    private Set<String> visitedArenas_broken;

    public SpleefPlayer() {
        setDefaults();
    }

    @DBLoad(fieldName = "rating")
    public void setRating(int rating) {
        this.rating = (rating > 0) ? rating : 0;
    }

    @Override
    @DBSave(fieldName = "rating")
    public int getRating() {
        return rating;
    }

    public int getRank() {
        return (int) SuperSpleef.getInstance().getPluginDB().getCollection("Players").count(new Document("rating", new Document("$gt", rating))) + 1;
    }

    @DBSave(fieldName = "visitedArenas")
    private List<String> saveVisitedArenas() {
        List<String> arenaNames = new ArrayList<>();
        for (Arena arena : visitedArenas) {
            if (arena != null) {
                arenaNames.add(arena.getName());
            }
        }
        for (String arena : visitedArenas_broken) {
            if (arena != null) {
                arenaNames.add(arena);
            }
        }
        return arenaNames;
    }

    @DBLoad(fieldName = "visitedArenas")
    private void loadVisitedArenas(List<String> arenaNames) {
        for (String name : arenaNames) {
            Arena arena = Arena.byName(name);
            if (arena != null) {
                visitedArenas.add(arena);
            } else {
                visitedArenas_broken.add(name);
            }
        }
    }

    public void setIngame(boolean ingame) {
        this.ingame = ingame;
    }

    public boolean isIngame() {
        return ingame;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setRequestingReset(boolean requestingReset) {
        this.requestingReset = requestingReset;
    }

    public boolean isRequestingReset() {
        return requestingReset;
    }

    public void setRequestingEndgame(boolean requestingEndgame) {
        this.requestingEndgame = requestingEndgame;
    }

    public boolean isRequestingEndgame() {
        return requestingEndgame;
    }

    public SpleefBattle getCurrentBattle() {
        if (SuperSpleef.getInstance().getBattleManager().isIngame(this)) {
            return SuperSpleef.getInstance().getBattleManager().getBattle(this);
        }
        return null;
    }

    public Set<Arena> getVisitedArenas() {
        return visitedArenas;
    }

    public int getPlayToRequest() {
        return playTo;
    }

    public void setPlayToRequest(int playTo) {
        this.playTo = playTo;
    }

    public void invalidatePlayToRequest() {
        this.playTo = -1;
    }

    @Override
    public void setDefaults() {
        super.setDefaults();
        this.rating = 1000;
        this.playTo = -1;
        this.frozen = false;
        this.ingame = false;
        visitedArenas = new HashSet<>();
        visitedArenas_broken = new HashSet<>();
        for (String name : (List<String>) Settings.getList("default_arenas_spleef")) {
            Arena a = Arena.byName(name);
            if (a != null) {
                visitedArenas.add(a);
            } else {
                visitedArenas_broken.add(name);
            }
        }
    }
}

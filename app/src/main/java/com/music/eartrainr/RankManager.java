package com.music.eartrainr;

import com.music.eartrainr.model.Rank;

/**
 * Created by Nicole on 2016-03-28.
 */
public class RankManager {
    private static volatile RankManager INSTANCE;
    private Rank currentRank;

    private RankManager() {
        currentRank = new Rank();
    }

    public static RankManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RankManager();
        }
        return INSTANCE;
    }

    public void destroy() {
        INSTANCE = null;
    }

    public int calculateTotalRank() {
        return this.currentRank.getGame_1_score() + this.currentRank.getGame_2_score() + this.currentRank.getGame_3_score();
    }

    public int calculateGameRank(int gameNumber) {
        switch (gameNumber) {
            case 1:
                return this.currentRank.getGame_1_score();
            case 2:
                return this.currentRank.getGame_2_score();
            case 3:
                return this.currentRank.getGame_3_score();
            default:
                return 0;
        }
    }

    public Rank getRank() {
        return this.currentRank;
    }

    public void updateRank(Rank rank) {
        this.currentRank = rank;
    }

    public String getUsername() {
        return this.currentRank.getUsername();
    }
}

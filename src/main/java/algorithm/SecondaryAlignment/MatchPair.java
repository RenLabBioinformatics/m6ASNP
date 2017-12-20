/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.SecondaryAlignment;

/**
 *
 * @author Ben
 */
public class MatchPair {
    private int matchI;
    private int matchJ;
    
    public MatchPair(int i, int j)
    {
        matchI = i;
        matchJ = j;
    }

    public int getMatchI() {
        return matchI;
    }

    public void setMatchI(int matchI) {
        this.matchI = matchI;
    }

    public int getMatchJ() {
        return matchJ;
    }

    public void setMatchJ(int matchJ) {
        this.matchJ = matchJ;
    }
}

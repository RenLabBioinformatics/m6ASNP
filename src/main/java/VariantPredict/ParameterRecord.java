/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

/**
 *
 * @author Ben
 */
public class ParameterRecord {
    private int up;
    private int down;
    private double highCutoff;
    private double mediumCutoff;
    private double lowCutoff;
    private double allCutoff;

    public double getAllCutoff() {
        return allCutoff;
    }

    public void setAllCutoff(double allCutoff) {
        this.allCutoff = allCutoff;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public void setHighCutoff(double highCutoff) {
        this.highCutoff = highCutoff;
    }

    public void setMediumCutoff(double mediumCutoff) {
        this.mediumCutoff = mediumCutoff;
    }

    public void setLowCutoff(double lowCutoff) {
        this.lowCutoff = lowCutoff;
    }
    
    public double getCutoff(String threshold)
    {
        if(threshold.equals("High"))
            return highCutoff;
        else if(threshold.equals("Medium"))
            return mediumCutoff;
        else if(threshold.equals("Low"))
            return lowCutoff;
        else
            return 0;
    }
}

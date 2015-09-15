package cn.edu.hit.triocnv.model;


import org.apache.commons.math3.distribution.PascalDistribution;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yongzhuang Liu
 */
public class NBEmissionProbability implements EmissionProbability {

    private double[] theta;
    private double[] coef1;
    private double[] coef2;
    private static final double MIN_PRO = Math.pow(10, -10);
    private static final double MAX_PRO = 1 - MIN_PRO;
    private static final double MAX_RD = 1000;
    private static final double EPSILON = 0.1;
    private int gcBinSize;

    public NBEmissionProbability(double[] theta, double[] coef1, double[] coef2, int gcBinSize) {
        this.theta = theta;
        this.coef1 = coef1;
        this.coef2 = coef2;
        this.gcBinSize = gcBinSize;
    }

    public int getMinGC() {
        int min = 0;
        for (int i = 0; i < theta.length; i++) {
            if (theta[i] != 0) {
                min = i;
                break;
            }
        }
        return min;
    }

    public int getMaxGC() {
        int max = 0;
        for (int i = theta.length-1; i >= 0; i--) {
            if (theta[i] != 0) {
                max = i;
                break;
            }
        }
        return max;
    }

    public double getProbOfObsGivenState(int state, int gc, double mappability, int rd) {
        double prob = 0;
        if (rd == 0) {
            if (state == 0) {
                return Math.log(MIN_PRO);
            } else {
                return Math.log(MAX_PRO);
            }
        } else if (rd >= MAX_RD) {
            if (state == 4) {
                return Math.log(MAX_PRO);
            } else {
                return Math.log(MIN_PRO);
            }
        } else {
            int index=gc/gcBinSize;
            double mu = Math.exp(coef1[index] + coef2[index] * mappability);
            double size = theta[index];
            double p;
            if (state == 0) {
                size = size * EPSILON;
                p = size / (size + mu * EPSILON);
            } else {
                size = size * (0.5 * (double) state);
                p = size / (size + mu * 0.5 * (double) state);
            }
            PascalDistribution pascal = new PascalDistribution((int) Math.ceil(size), p);
            prob = Math.log(pascal.probability(rd));
            return prob;
        }
    }
    
   
}
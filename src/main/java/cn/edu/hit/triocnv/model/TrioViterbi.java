package cn.edu.hit.triocnv.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yongzhuang Liu
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yongzhuang Liu
 */
public class TrioViterbi {

    private List<Observation> observations;
    private NBEmissionProbability[] emissions;
    private double[][][][][][] transition;
    private double[][][] inheritance;
    private double[] PI;
    private static final int NUM_STATES = 5;

    public TrioViterbi(List<Observation> observations, double[] PI, double[][][][][][] transition, double[][][] inheritance, NBEmissionProbability[] emissions) {
        this.observations = observations;
        this.PI = PI;
        this.transition = transition;
        this.inheritance = inheritance;
        this.emissions = emissions;
    }

    public int[][] viterbi() {
        int T = observations.size();
        byte[][][][][] path = new byte[T][NUM_STATES][NUM_STATES][NUM_STATES][3];
        double[][][][] r = new double[T][NUM_STATES][NUM_STATES][NUM_STATES];
        for (int f = 0; f < NUM_STATES; f++) {
            for (int m = 0; m < NUM_STATES; m++) {
                for (int o = 0; o < NUM_STATES; o++) {
                    double fEmission = emissions[0].getProbOfObsGivenState(f, observations.get(0).getGC(), observations.get(0).getMappability(), observations.get(0).getRD(0));
                    double mEmission = emissions[1].getProbOfObsGivenState(m, observations.get(0).getGC(), observations.get(0).getMappability(), observations.get(0).getRD(1));
                    double oEmission = emissions[2].getProbOfObsGivenState(o, observations.get(0).getGC(), observations.get(0).getMappability(), observations.get(0).getRD(2));
                    r[0][f][m][o] = Math.log(PI[m]) + Math.log(PI[f]) + Math.log(PI[o]) + inheritance[f][m][o] + fEmission + mEmission + oEmission;
                    path[0][f][m][o][0] = 0;
                    path[0][f][m][o][1] = 0;
                    path[0][f][m][o][2] = 0;
                }
            }
        }
        for (int t = 1; t < T; t++) {
            for (int f = 0; f < NUM_STATES; f++) {
                for (int m = 0; m < NUM_STATES; m++) {
                    for (int o = 0; o < NUM_STATES; o++) {
                        double tmp = Double.NEGATIVE_INFINITY;
                        int[] s = new int[3];
                        double aEmission = 0;
                        int gc = observations.get(t).getGC();
                        double fEmission = emissions[0].getProbOfObsGivenState(f, observations.get(t).getGC(), observations.get(t).getMappability(), observations.get(t).getRD(0));
                        double mEmission = emissions[1].getProbOfObsGivenState(m, observations.get(t).getGC(), observations.get(t).getMappability(), observations.get(t).getRD(1));
                        double oEmission = emissions[2].getProbOfObsGivenState(o, observations.get(t).getGC(), observations.get(t).getMappability(), observations.get(t).getRD(2));
                        aEmission = fEmission + mEmission + oEmission;
                        double mendelian = inheritance[f][m][o];
                        for (int f1 = 0; f1 < NUM_STATES; f1++) {
                            for (int m1 = 0; m1 < NUM_STATES; m1++) {
                                for (int o1 = 0; o1 < NUM_STATES; o1++) {
                                    //double tem = r[t - 1][f1][m1][o1] + aEmission + transition[f1][m1][o1][f][m][o] + mendelian;
                                    double tem = r[t - 1][f1][m1][o1] + aEmission + transition[f1][m1][o1][f][m][o];
                                    if (tem > tmp) {
                                        tmp = tem;
                                        s[0] = f1;
                                        s[1] = m1;
                                        s[2] = o1;
                                    }
                                }
                            }
                        }
                        r[t][f][m][o] = tmp;
                        path[t][f][m][o][0] = (byte) s[0];
                        path[t][f][m][o][1] = (byte) s[1];
                        path[t][f][m][o][2] = (byte) s[2];
                    }
                }
            }
        }
        double p = Double.NEGATIVE_INFINITY;
        int s[] = new int[3];
        for (int i = 0; i < NUM_STATES; i++) {
            for (int j = 0; j < NUM_STATES; j++) {
                for (int k = 0; k < NUM_STATES; k++) {
                    if (r[r.length - 1][i][j][k] > p) {
                        p = r[r.length - 1][i][j][k];
                        s[0] = i;
                        s[1] = j;
                        s[2] = k;
                    }
                }
            }
        }
        int[][] trace = new int[T][3];
        trace[T - 1][0] = s[0];
        trace[T - 1][1] = s[1];
        trace[T - 1][2] = s[2];
        for (int t = T - 1; t > 0; t--) {
            int f = s[0];
            int m = s[1];
            int o = s[2];
            for (int i = 0; i < 3; i++) {
                trace[t - 1][i] = path[t][f][m][o][i];
                s[i] = path[t][f][m][o][i];
            }
        }
        return trace;
    }

    public List<Observation> getObservations() {
        return observations;
    }
}

package cn.edu.hit.triocnv.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yongzhuang Liu
 */
public class Observation {

    private String chrom;
    private int start;
    private int end;
    private int gc;
    private double mappability;
    private int[] rd;

    public Observation(String chrom, int start, int end, int gc, double mappability, int[] rd) {
        this.chrom = chrom;
        this.start = start;
        this.end = end;
        this.gc = gc;
        this.mappability = mappability;
        this.rd = rd;
    }

    public String getChrom() {
        return chrom;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int[] getRD() {
        return rd;
    }

    public int getGC() {
        return gc;
    }

    public double getMappability() {
        return mappability;
    }

    public int getRD(int index) {
        return rd[index];
    }

    public String toString() {
        return chrom + "\t" + start + "\t" + end + "\t" + gc + "\t" + mappability + "\t" + rd[0] + "\t" + rd[1] + "\t" + rd[2];
    }
}

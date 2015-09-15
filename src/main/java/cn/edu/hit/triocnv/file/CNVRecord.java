package cn.edu.hit.triocnv.file;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yongzhuang Liu
 */
public class CNVRecord implements Comparable<CNVRecord> {

    private String chrom;
    private int start;
    private int end;
    private int[] states;
    private int length;
    private double mappability = -1;

    public CNVRecord(String chrom, int start, int end, int[] states, double mappability) {
        this.chrom = chrom;
        this.start = start;
        this.end = end;
        this.states = states;
        this.length = end - start + 1;
        this.mappability = mappability;
    }

    public CNVRecord(String chrom, int start, int end, int[] states) {
        this.chrom = chrom;
        this.start = start;
        this.end = end;
        this.states = states;
        this.length = end - start + 1;
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

    public int[] getStates() {
        return states;
    }

    public int getLength() {
        return length;
    }

    public double getMappability() {
        return mappability;
    }

    public int compareTo(CNVRecord other) {
        if (chrom.equals(other.getChrom())) {
            return start - other.start;
        } else {
            return chrom.compareTo(other.getChrom());
        }
    }

    public String toString() {
        return chrom + "\t" + start + "\t" + end + "\t" + states[0] + "\t" + states[1] + "\t" + states[2] + "\t" + mappability + "\t" + length;
    }
}

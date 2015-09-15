package cn.edu.hit.triocnv.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yongzhuang Liu
 */
public class GCCalculation {

    private byte[] bases;
    private int startIndex;
    private int endIndex;

    public GCCalculation(byte[] bases, int startIndex, int endIndex) {
        this.bases = bases;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public int getGCContent() {
        int gcCount = 0;
        int nCount = 0;
        for (int i = startIndex; i < endIndex; ++i) {
            final byte base = bases[i];
            if (base == 'G' || base == 'C') {
                ++gcCount;
            } else if (base == 'N') {
                ++nCount;
            }
        }
        if (nCount >= 10) {
            return -1;
        } else {
            return gcCount * 100 / (endIndex - startIndex);
        }
    }
}

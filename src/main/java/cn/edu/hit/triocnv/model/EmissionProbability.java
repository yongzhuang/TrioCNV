package cn.edu.hit.triocnv.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yongzhuang Liu
 */
public interface EmissionProbability {

    public double getProbOfObsGivenState(int state, int gc, double mappability, int rd);
}

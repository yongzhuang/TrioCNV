package cn.edu.hit.triocnv.module;


import cn.edu.hit.triocnv.model.Observation;
import cn.edu.hit.triocnv.file.CNVRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.batik.dom.util.HashTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yongzhuang Liu
 */
public class Postprocessing {

    private List<Observation> observations;
    private int[][] trace;
    private int minDistance;

    public Postprocessing(List<Observation> observations, int[][] trace, int minDistance) {
        this.observations = observations;
        this.trace = trace;
        this.minDistance = minDistance;
    }

    public List<CNVRecord> process() {
        for (int i = 0; i < trace.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (trace[i][j] == 0 || trace[i][j] == 1) {
                    trace[i][j] = 1;
                }
                if (trace[i][j] == 3 || trace[i][j] == 4) {
                    trace[i][j] = 3;
                }
            }
        }
        List<CNVRecord> records = new ArrayList();
        String mergeChrom = null;
        int mergeStart = -1;
        int mergeEnd = -1;
        String mergeState = null;
        for (int i = 0; i < trace.length; i++) {
            if (trace[i][0] != 2 || trace[i][1] != 2 || trace[i][2] != 2) {
                String chrom = observations.get(i).getChrom();
                int start = observations.get(i).getStart();
                int end = observations.get(i).getEnd();
                String state = Integer.toString(trace[i][0]) + trace[i][1] + trace[i][2];
                if (mergeState == null) {
                    mergeChrom = chrom;
                    mergeStart = start;
                    mergeEnd = end;
                    mergeState = state;
                } else if (chrom.equals(mergeChrom) && start == mergeEnd + 1 && state.equals(mergeState)) {
                    mergeEnd = end;
                } else {
                    int[] states = new int[]{Integer.parseInt(mergeState.substring(0, 1)), Integer.parseInt(mergeState.substring(1, 2)), Integer.parseInt(mergeState.substring(2, 3))};
                    CNVRecord record = new CNVRecord(mergeChrom, mergeStart, mergeEnd, states);
                    records.add(record);
                    mergeChrom = chrom;
                    mergeStart = start;
                    mergeEnd = end;
                    mergeState = state;
                }
            }
        }
        if (mergeChrom != null) {
            int[] states = new int[]{Integer.parseInt(mergeState.substring(0, 1)), Integer.parseInt(mergeState.substring(1, 2)), Integer.parseInt(mergeState.substring(2, 3))};
            records.add(new CNVRecord(mergeChrom, mergeStart, mergeEnd, states));
        }
        //List<CNVRecord> records2 = mergeStep1(records);
        List<CNVRecord> records3 = mergeStep(records);
        return records3;
    }
/*
    private List<CNVRecord> mergeStep1(List<CNVRecord> records) {
        List<CNVRecord> result = new ArrayList();
        CNVRecord lastRecord = null;
        for (CNVRecord record : records) {
            if (lastRecord == null) {
                lastRecord = record;
            } else {
                int[] lastStates = lastRecord.getStates();
                int[] states = record.getStates();
                if (record.getChrom().equals(lastRecord.getChrom()) && (record.getStart() - lastRecord.getEnd() - 1) <= minDistance && lastStates[0] == states[0] && lastStates[1] == states[1] && lastStates[2] == states[2]) {
                    lastRecord = new CNVRecord(lastRecord.getChrom(), lastRecord.getStart(), record.getEnd(), states);
                } else {
                    result.add(lastRecord);
                    lastRecord = record;
                }
            }
        }
        if (lastRecord != null) {
            result.add(lastRecord);
        }
        return result;
    }
*/
    private List<CNVRecord> mergeStep(List<CNVRecord> records) {
        List<CNVRecord> result = new ArrayList();
        List<CNVRecord> cluster = new ArrayList();
        CNVRecord lastRecord = null;
        for (CNVRecord record : records) {
            if (lastRecord == null) {
                lastRecord = record;
                cluster.add(record);
            } else {
                if ((record.getStart() - lastRecord.getEnd() - 1) > minDistance || ! isAmbigugous(record, lastRecord)) {
                    result.add(mergeByCluster(cluster));
                    cluster.clear();
                    cluster.add(record);
                    lastRecord = record;
                } else {
                    cluster.add(record);
                    lastRecord = record;
                }
            }
        }
        if (lastRecord != null) {
            result.add(mergeByCluster(cluster));
        }
        return result;
    }

    private CNVRecord mergeByCluster(List<CNVRecord> cluster) {
        if (cluster.size() == 1) {
            return cluster.get(0);
        }
        int[][] length = new int[3][5];
        for (CNVRecord record : cluster) {
            for (int i = 0; i < 3; i++) {
                length[i][record.getStates()[i]] += record.getLength();
            }
        }
        int[] states = new int[3];
        for (int i = 0; i < 3; i++) {
            int index = -1; 
            int max = 0;
            for (int j = 0; j < 5; j++) {
                if (length[i][j] > max) {
                    max = length[i][j];
                    index = j;
                }
            }
            states[i] = index;
        }
        String chrom = cluster.get(0).getChrom();
        int start = cluster.get(0).getStart();
        int end = cluster.get(cluster.size() - 1).getEnd();
        return new CNVRecord(chrom, start, end, states);
    }
    
    private boolean isAmbigugous(CNVRecord firstRecord, CNVRecord secondRecord) {
        int[] states1 = firstRecord.getStates();
        int[] states2 = secondRecord.getStates();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (states1[i] != 2 && states2[j] != 2 && states1[i] == states2[j]) {
                    return true;
                }
            }
        }
        return false;
    }
}

package cn.edu.hit.triocnv.module;


import cn.edu.hit.triocnv.file.CNVRecord;
import cn.edu.hit.triocnv.model.TrioViterbi;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yongzhuang Liu
 */
public class SingleThreadCall implements Runnable {

    private TrioViterbi trioViterbi;
    private String outputFile;
    private int minDistance;

    public SingleThreadCall(TrioViterbi trioViterbi, String outputFile, int minDistance) {
        this.trioViterbi = trioViterbi;
        this.outputFile = outputFile;
        this.minDistance = minDistance;
    }

    public void run() {
        try {
            int[][] trace = trioViterbi.viterbi();
            Postprocessing post = new Postprocessing(trioViterbi.getObservations(), trace, minDistance);
            List<CNVRecord> list = post.process();
            FileOutputStream out = new FileOutputStream(new File(outputFile), true);
            FileChannel fcout = out.getChannel();
            FileLock flout = null;
            while (true) {
                try {
                    flout = fcout.tryLock();
                    break;
                } catch (OverlappingFileLockException e) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            for (CNVRecord line : list) {
                String tmp = line.toString() + "\n";
                out.write(tmp.getBytes());
            }
            flout.release();
            fcout.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

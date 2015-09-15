package cn.edu.hit.triocnv.module;


import cn.edu.hit.triocnv.module.SingleThreadCall;
import cn.edu.hit.triocnv.model.Observation;
import cn.edu.hit.triocnv.file.CNVRecord;
import cn.edu.hit.triocnv.model.InheritanceMatrix;
import cn.edu.hit.triocnv.model.NBEmissionProbability;
import cn.edu.hit.triocnv.model.TransitionProbability;
import cn.edu.hit.triocnv.model.TrioViterbi;
import cn.edu.hit.triocnv.file.PEDReader;
import cn.edu.hit.triocnv.file.Trio;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.broad.igv.bbfile.BBFileReader;
import org.broad.igv.bbfile.BigWigIterator;
import org.broad.igv.bbfile.WigItem;
import rcaller.RCaller;
import rcaller.RCode;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yongzhuang Liu
 */
public class TrioCall {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TrioCall.class);
    private String inputFile;
    private String outputFile;
    private String pedFile;
    private double minMappability;
    int minDistance;
    int size=500;
    private double outlier;
    double p;
    double e;
    double a=0.0009;
    private String mappabilityFile;
    int gcBinSize;
    String[] samples;

    public TrioCall(String inputFile, String outputFile, String pedFile, String mappabilityFile, double minMappability, int minDistance, double transition_prob, double outlier,  double e, int gcBinSize) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.pedFile = pedFile;
        this.mappabilityFile = mappabilityFile;
        this.minMappability = minMappability;
        this.minDistance = minDistance;
        this.p = transition_prob;
        this.outlier = outlier;
        this.e = e;
        this.gcBinSize=gcBinSize;
    }

    private String getSexOfOffspring() {
        Trio trio = (new PEDReader(pedFile).getTrios()).get(0);
        if (trio.getOffspring().getSex() == 1) {
            return "M";
        } else if (trio.getOffspring().getSex() == 2) {
            return "F";
        } else {
            return "N";
        }
    }

    public void runMultiThreads(int numOfThreads) {
        logger.info("Checking R environment and packages ......");
        try {
            Process pid = Runtime.getRuntime().exec("which Rscript");
            BufferedReader runTimeReader = new BufferedReader(new InputStreamReader(pid.getInputStream()));
            String R_HOME = runTimeReader.readLine().trim();
            if (R_HOME.equals("")) {
                logger.error("Rscript exectuable is not set in the PATH environment variable!");
                return;
            }
            if (!checkInstalledPackages(R_HOME)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Estimating parameters ......");
        NBEmissionProbability[] emissions = getNBEmissions();
        int min_gc = Math.max(getMinGC(emissions) * gcBinSize, 30);
        int max_gc = Math.min((getMaxGC(emissions)+1)* gcBinSize-1, 70);
        List<List<Observation>> observationsByContig = getObservations(min_gc, max_gc);
        double[] pi = getPI();
        double[][][][][][] transition = (new TransitionProbability(p, a, a)).getTransitionMatrix();
        logger.info("HMM Segmentation ......");
        Pattern pattern = Pattern.compile("^(chr)?([1-9]|1[0-9]|2[0-2])$");
        File file = new File(outputFile);
        if (file.exists()) {
            try {
                file.delete();
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
        for (int i = 0; i < observationsByContig.size(); i++) {
            List<Observation> observations = observationsByContig.get(i);
            String chrom = observations.get(0).getChrom();
            Matcher matcher = pattern.matcher(chrom);
            double[][][] inheritance = null;
             if (!matcher.matches()) {
                break;
            } 
            if (matcher.matches()) {
                inheritance = (new InheritanceMatrix(e, a)).getAutoMatrix();
            } else if ((chrom.equals("X") || chrom.equals("chrX")) && getSexOfOffspring().equals("F")) {
                inheritance = (new InheritanceMatrix(e, a)).getFemaleXMatrix();
            } else if ((chrom.equals("X") || chrom.equals("chrX")) && getSexOfOffspring().equals("M")) {
                inheritance = (new InheritanceMatrix(e, a)).getMaleXMatrix();
            }
            TrioViterbi trioViterbi = (new TrioViterbi(observations, pi, transition, inheritance, emissions));
            threadPool.execute(new SingleThreadCall(trioViterbi, outputFile, minDistance));
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(30, TimeUnit.DAYS);
            boolean loop = true;
            long memory = 0;
            long maxTotalMemory = 0;
            do {
                long totalMemory = Runtime.getRuntime().totalMemory();
                long freeMemory = Runtime.getRuntime().freeMemory();
                long tmp = (totalMemory - freeMemory) / (1024 * 1024);
                if (tmp > memory) {
                    memory = tmp;
                }
                if (totalMemory > maxTotalMemory) {
                    maxTotalMemory = totalMemory;
                }
                loop = !threadPool.awaitTermination(1, TimeUnit.MINUTES);
            } while (loop);
            threadPool.awaitTermination(30, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        List<CNVRecord> records = new ArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(outputFile)));
            String line;
            while ((line = bufferedReader.readLine()) != null && line.trim().length() > 0) {
                String[] items = line.split("\t");
                String chrom = items[0];
                int start = Integer.parseInt(items[1]);
                int end = Integer.parseInt(items[2]);
                int[] states = new int[]{Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5])};
                double mappability = Double.parseDouble(items[6]);
                records.add(new CNVRecord(chrom, start, end, states, mappability));
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Collections.sort(records);
        try {
            PrintWriter writer = new PrintWriter(outputFile);
            writer.write("CHROM"+"\t"+"START"+"\t"+"END"+"\t"+samples[0]+"\t"+samples[1]+"\t"+samples[2]+"\t"+"Mappability"+"\t"+"Length"+"\n");
            for (CNVRecord record : records) {
                double mappability = getMappability(mappabilityFile, record.getChrom(), record.getStart(), record.getEnd());
                int[] states = record.getStates();
                boolean refState = states[0] == 2 && states[1] == 2 && states[2] == 2;
                if (mappability >= minMappability && !refState) {
                    writer.write(record.getChrom() + "\t" + record.getStart() + "\t" + record.getEnd() + "\t");
                    writer.write(toCNVType(states[0]) + "\t" + toCNVType(states[1]) + "\t" + toCNVType(states[2]) + "\t");
                    writer.write(mappability + "\t" + record.getLength() + "\n");
                }
            }
            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public double[] getPI() {
        double[] pi = new double[]{0.0001, 0.0001, 0.00096, 0.0001, 0.0001};
        return pi;
    }

    public List<List<Observation>> getObservations(int min_gc, int max_gc) {
        List<List<Observation>> observations = new ArrayList();
        List<Observation> tmp = null;
        int length_n_region = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputFile)));
            String lastChrom = null;
            int lastEnd = 0;
            String line = bufferedReader.readLine();
            String[] sampleNames=line.split("\t");
            samples[0]=sampleNames[3];
            samples[1]=sampleNames[4];
            samples[2]=sampleNames[5];
            while ((line = bufferedReader.readLine()) != null) {
                String[] record = line.split("\t");
                String chrom = record[0];
                int start = Integer.parseInt(record[1]);
                int end = Integer.parseInt(record[2]);
                int[] rd = new int[]{Integer.parseInt(record[3]), Integer.parseInt(record[4]), Integer.parseInt(record[5])};
                int gc = Integer.parseInt(record[6]);
                double mappability = Double.parseDouble(record[7]);
                if (gc == -1) {
                    if (!(lastChrom == null || !lastChrom.equals(chrom))) {
                        length_n_region = length_n_region + end -start+1;
                    }
                    continue;
                }
                if (gc < min_gc || gc > max_gc) {
                    continue;
                }
                if (lastChrom == null) {
                    tmp = new ArrayList();
                }
                if (lastChrom != null && lastChrom.equals(chrom) && length_n_region >= minDistance) {
                    length_n_region = 0;
                    observations.add(tmp);
                    tmp = new ArrayList();
                }
                if (lastChrom != null && !lastChrom.equals(chrom)) {
                    observations.add(tmp);
                    length_n_region = 0;
                    tmp = new ArrayList();
                }
                tmp.add(new Observation(chrom, start, end, gc, mappability, rd));
                lastChrom = chrom;
                lastEnd = end;
            }
            observations.add(tmp);
            bufferedReader.close();
            return observations;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private NBEmissionProbability[] getNBEmissions() {
        try {
            NBEmissionProbability[] emissions = new NBEmissionProbability[3];
            Process pid = Runtime.getRuntime().exec("which Rscript");
            BufferedReader runTimeReader = new BufferedReader(new InputStreamReader(pid.getInputStream()));
            String R_HOME = runTimeReader.readLine().trim();
            runTimeReader.close();
            String[] args = new String[]{inputFile};
            double[] outliers = new double[]{outlier / 2, 1 - outlier / 2};
            int[] sample_size=new int[]{size};
            int[] gc_bin_size=new int[]{gcBinSize};
            for (int k = 0; k < 3; k++) {
                int[] sample = new int[]{k + 4};
                RCaller caller = new RCaller();
                RCode code = new RCode();
                caller.setRscriptExecutable(R_HOME);
                caller.setRCode(code);
                code.addIntArray("sample", sample);
                code.addStringArray("args", args);
                code.addDoubleArray("outliers", outliers);
                code.addIntArray("size", sample_size);
                code.addIntArray("binsize", gc_bin_size);
                code.addRCode("library(MASS)");
                code.addRCode("data<-read.table(args[1], header = TRUE)");
                code.addRCode("data<-data[which(data$GC>-1 & data$CHROM!='X' & data$CHROM!='Y' & data$CHROM!='chrX' & data$CHROM !='chrY'& data[,c(sample[1])]>0),]");                
                code.addRCode("data<-split(data[,c(sample[1],8)],data$GC)");
                code.addRCode("theta<-c()");
                code.addRCode("coef1<-c()");
                code.addRCode("coef2<-c()");
                code.addRCode("remove_outliers <- function(data, na.rm = TRUE, ...) {");
                code.addRCode("range <- quantile(data[,1], prob=c(outliers[1], outliers[2]),na.rm = TRUE)");
                code.addRCode("new <- data");
                code.addRCode("new[which(new[,1] >= range[1] &  new[,1] <= range[2]),]");
                code.addRCode("}");
                code.addRCode("for(i in 0:floor(100/binsize[1])){");
                code.addRCode("bin<-as.data.frame(unname(data[i*binsize[1]+1]))");
                code.addRCode("if(binsize[1]>1){");
                code.addRCode("for(j in 2:binsize[1]){");
                code.addRCode("bin<-rbind(bin,as.data.frame(unname(data[i*binsize[1]+j])))");
                code.addRCode("}");
                code.addRCode("}");
                code.addRCode("if(nrow(bin)>size[1]){");
                code.addRCode("bin<-remove_outliers(bin)");
                code.addRCode("count<-bin[,1]");
                code.addRCode("mappability<-bin[,2]");
                code.addRCode("fit<-glm.nb(count~mappability)");
                code.addRCode("theta<-c(theta,fit$theta)");
                code.addRCode("coef1<-c(coef1,unname(fit$coef[1]))");
                code.addRCode("coef2<-c(coef2,unname(fit$coef[2]))");
                code.addRCode("}");
                code.addRCode("else{");
                code.addRCode("theta<-c(theta,0)");
                code.addRCode("coef1<-c(coef1,0)");
                code.addRCode("coef2<-c(coef2,0)");
                code.addRCode("}");
                code.addRCode("}");
                code.addRCode("result<-list(theta=theta,coef1=coef1, coef2=coef2)");
                caller.runAndReturnResult("result");
                double[] theta = caller.getParser().getAsDoubleArray("theta");
                double[] coef1 = caller.getParser().getAsDoubleArray("coef1");
                double[] coef2 = caller.getParser().getAsDoubleArray("coef2");
                emissions[k] = new NBEmissionProbability(theta, coef1, coef2, gcBinSize);
            }
            return emissions;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private int getMinGC(NBEmissionProbability[] emissions) {
        int min_gc = 0;
        for (int i = 0; i < emissions.length; i++) {
            if (emissions[i].getMinGC() > min_gc) {
                min_gc = emissions[i].getMinGC();
            }
        }
        return min_gc;
    }

    private int getMaxGC(NBEmissionProbability[] emissions) {
        int max_gc = 100;
        for (int i = 0; i < emissions.length; i++) {
            if (emissions[i].getMaxGC() < max_gc) {
                max_gc = emissions[i].getMaxGC();
            }
        }
        return max_gc;
    }

    private String toCNVType(int state) {
        String type = null;
        switch (state) {
            case 0:
                type = "DEL";
                break;
            case 1:
                type = "DEL";
                break;
            case 2:
                type = "REF";
                break;
            case 3:
                type = "DUP";
                break;
            case 4:
                type = "DUP";
                break;
        }
        return type;
    }

    private double getMappability(String mappabilityFile, String chrom, int start, int end) {
        if (!chrom.startsWith("chr")) {
            chrom = "chr" + chrom;
        }
        try {
            BBFileReader reader = new BBFileReader(mappabilityFile);
            BigWigIterator iterator = reader.getBigWigIterator(chrom, start - 1, chrom, end, false);
            double total = 0;
            while (iterator.hasNext()) {
                WigItem item = iterator.next();
                double value = item.getWigValue();
                int startBase = item.getStartBase();
                int endBase = item.getEndBase();
                if (startBase < start - 1 && endBase > end) {
                    total += value * (end - start + 1);
                } else if (startBase < start - 1 && endBase <= end) {
                    total += value * (endBase - start + 1);
                } else if (startBase >= start - 1 && endBase > end) {
                    total += value * (end - startBase);
                } else {
                    total += value * (endBase - startBase);
                }
            }
            reader.close();
            return total / (end - start + 1);
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private boolean checkInstalledPackages(String R_HOME) {
        boolean tag = true;
        RCaller caller = new RCaller();
        caller.setRscriptExecutable(R_HOME);
        caller.cleanRCode();
        RCode code = new RCode();
        String[] packages = new String[]{"Runiversal", "MASS"};
        code.addStringArray("packages", packages);
        code.addRCode("label<-c(1, 1)");
        code.addRCode("for(i in 1:2){");
        code.addRCode("if(!require(packages[i], character.only=TRUE)){");
        code.addRCode("label[i]=0");
        code.addRCode("}");
        code.addRCode("}");
        caller.setRCode(code);
        caller.runAndReturnResult("label");
        int[] label = caller.getParser().getAsIntArray("label");
        for (int i = 0; i < packages.length; i++) {
            if (label[i] == 0) {
                logger.error(packages[i] + " is not installed in R environment!");
                tag = false;
            }
        }
        return tag;
    }
}

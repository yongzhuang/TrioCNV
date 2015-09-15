package cn.edu.hit.triocnv.cmdline;


import cn.edu.hit.triocnv.module.Preprocessing;
import cn.edu.hit.triocnv.module.TrioCall;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yongzhuang Liu
 */
public class TrioCNV {

    private static Logger logger = Logger.getLogger(TrioCNV.class);

    public static void main(String[] args) throws IOException {
        String usage = "\nTrioCNV-0.1.2\n";
        usage = usage + "\nUsage: java -jar TrioCNV.jar <COMMAND> [OPTIONS]\n\n";
        usage = usage + "COMMANDS:\n"
                + "\tpreprocess\textract information\n"
                + "\tcall\t\tcall CNVs in a parent-offspring trio\n";
        if (args.length > 0) {
            if (args[0].equals("preprocess") || args[0].equals("call")) {
                run(args[0], args);
            } else {
                logger.error("Command is not recognized!\n" + usage);
            }
        } else {
            System.out.println(usage);
        }
    }

    private static void run(String cmd, String[] args) throws IOException {
        long start = System.currentTimeMillis();
        CommandLineParser parser = new PosixParser();
        CommandLine commandLine = null;
        Options options = createOptions(cmd);
        try {
            if (options != null) {
                commandLine = parser.parse(options, args);
            }
        } catch (ParseException parseException) {
            logger.error("Invalid command line parameters!");
        }
        if (cmd.equals("preprocess")) {
            if (isValidated(commandLine, "preprocess")) {
                String reference = commandLine.getOptionValue("reference");
                String bams = commandLine.getOptionValue("bams");
                String ped = commandLine.getOptionValue("pedigree");
                String mappability = commandLine.getOptionValue("mappability");
                String output = commandLine.getOptionValue("output");
                int window = Integer.parseInt(commandLine.getOptionValue("window", "200"));
                int min_mapping_quality = Integer.parseInt(commandLine.getOptionValue("min_mapping_quality", "0"));
                (new Preprocessing(reference, bams, ped, mappability, output, window, min_mapping_quality)).process();
            } else {
                printHelp("preprocess");
                return;
            }
        }
        if (cmd.equals("call")) {
            if (isValidated(commandLine, "call")) {
                String inputFile = commandLine.getOptionValue("input");
                String outputFile = commandLine.getOptionValue("output");
                String pedFile = commandLine.getOptionValue("pedigree");
                String mappabilityFile = commandLine.getOptionValue("mappability");
                int numOfThreads = Integer.parseInt(commandLine.getOptionValue("nt", "1"));
                double minMappability = Double.parseDouble(commandLine.getOptionValue("min_mappability", "0"));
                int minDistance = Integer.parseInt(commandLine.getOptionValue("min_distance", "10000"));
                double transitionProb = Double.parseDouble(commandLine.getOptionValue("transition_prob", "0.00001"));
                double e = Double.parseDouble(commandLine.getOptionValue("mutation_rate", "0.0001"));
                double outlier = Double.parseDouble(commandLine.getOptionValue("outlier", "0.025"));;
                int gcBinSize = Integer.parseInt(commandLine.getOptionValue("gc_bin_size", "1"));;
                (new TrioCall(inputFile, outputFile, pedFile, mappabilityFile, minMappability, minDistance, transitionProb,outlier, e, gcBinSize)).runMultiThreads(numOfThreads);
            } else {
                printHelp("call");
                return;
            }
        }
        long end = System.currentTimeMillis();
        logger.info("Total running time is " + (end - start) / 1000 + " seconds");
        logger.info("Done!");
    }

    private static Options createOptions(String cmd) {
        Options options = new Options();
        if (cmd.equals("preprocess")) {
            options.addOption(OptionBuilder.withLongOpt("reference").withDescription("reference genome file (required)").hasArg().withArgName("FILE").create("R"));
            options.addOption(OptionBuilder.withLongOpt("bams").withDescription("bam list file (required)").hasArg().withArgName("FILE").create("B"));
            options.addOption(OptionBuilder.withLongOpt("pedigree").withDescription("pedigree file (required)").hasArg().withArgName("FILE").create("P"));
            options.addOption(OptionBuilder.withLongOpt("mappability").withDescription("mappability file (required)").hasArg().withArgName("FILE").create("M"));
            options.addOption(OptionBuilder.withLongOpt("output").withDescription("perfix of output file (required)").hasArg().withArgName("FILE").create("O"));
            options.addOption(OptionBuilder.withLongOpt("window").withDescription("window size (optional, default 200)").hasArg().withArgName("INT").create());
            options.addOption(OptionBuilder.withLongOpt("min_mapping_quality").withDescription("minumum mapping quality (optional, default 0)").hasArg().withArgName("INT").create());
            return options;
        } else if (cmd.equals("call")) {
            options.addOption(OptionBuilder.withLongOpt("input").withDescription("read count file got by the preprocess step (required)").hasArg().withArgName("FILE").create("I"));
            options.addOption(OptionBuilder.withLongOpt("output").withDescription("output structural variation file (required)").hasArg().withArgName("FILE").create("O"));
            options.addOption(OptionBuilder.withLongOpt("mappability").withDescription("mappability file (required)").hasArg().withArgName("FILE").create("M"));
            options.addOption(OptionBuilder.withLongOpt("pedigree").withDescription("pedigree file (required)").hasArg().withArgName("FILE").create("P"));
            options.addOption(OptionBuilder.withLongOpt("min_mappability").withDescription("minumum mappability(optional, default 0)").hasArg().withArgName("FLOAT").create());
            options.addOption(OptionBuilder.withLongOpt("transition_prob").withDescription("probability of transition between two different copy number states(optional, default 0.00001)").hasArg().withArgName("FLOAT").create());
            options.addOption(OptionBuilder.withLongOpt("mutation_rate").withDescription("de novo mutation rate (optional, default 0.0001)").hasArg().withArgName("FLOAT").create());
            options.addOption(OptionBuilder.withLongOpt("min_distance").withDescription("minumum distance to merge two adjacent CNVs (optional, default 10K)").hasArg().withArgName("INT").create());
            options.addOption(OptionBuilder.withLongOpt("outlier").withDescription(" the predefined percentage of outliers (optional, default 0.025)").hasArg().withArgName("FLOAT").create());
            options.addOption(OptionBuilder.withLongOpt("gc_bin_size").withDescription("size of gc bin by percent (optional, default 1)").hasArg().withArgName("INT").create());
            options.addOption(OptionBuilder.withLongOpt("nt").withDescription("number of threads (optional, default 1)").hasArg().withArgName("INT").create());
            
            return options;
        } else {
            return null;
        }
    }

    private static boolean isValidated(CommandLine line, String cmd) {
        boolean tag = true;
        if (cmd.equals("preprocess")) {
            if (!line.hasOption("reference") || !(new File(line.getOptionValue("reference")).isFile())) {
                logger.error("The reference genome file is not correctly specified!");
                tag = false;
            }
            if (!line.hasOption("bams") || !(new File(line.getOptionValue("bams")).isFile())) {
                logger.error("The list of bam files is not correctly specified!");
                tag = false;
            }
            if (!line.hasOption("pedigree") || !(new File(line.getOptionValue("pedigree")).isFile())) {
                logger.error("The pedigree file is not correctly specified!");
                tag = false;
            }
            if (!line.hasOption("mappability") || !(new File(line.getOptionValue("mappability")).isFile())) {
                logger.error("The mappability file is not correctly specified!");
                tag = false;
            }
            if (!line.hasOption("output")) {
                logger.error("The output prefix is not correctly specified!");
                tag = false;
            }
        }
        if (cmd.equals("call")) {
            if (!line.hasOption("input")) {
                logger.error("The read count file got by the preprocess step is not correctly specified!");
                tag = false;
            }
            if (!line.hasOption("output")) {
                logger.error("The output CNV file is not correctly specified!");
                tag = false;
            }
            if (!line.hasOption("pedigree") || !(new File(line.getOptionValue("pedigree")).isFile())) {
                logger.error("The pedigree file is not correctly specified!");
                tag = false;
            }
            if (!line.hasOption("mappability") || !(new File(line.getOptionValue("mappability")).isFile())) {
                logger.error("The mappability file is not correctly specified!");
                tag = false;
            }
        }
        return tag;
    }

    private static void printHelp(String command) {
        System.out.println();
        String usage1 = "usage: java -jar TrioCNV.jar " + command + " [OPTIONS]\n\n"
                + "-R,--reference\t<FILE>\treference genome file (required)\n"
                + "-B,--bams\t<FILE>\tbam list file (required)\n"
                + "-P,--pedigree\t<FILE>\tpedigree file (required)\n"
                + "-M,--mappability\t<FILE>\tmappability file (required)\n"
                + "-O,--output\t<FILE>\toutput file (required)\n"
                + "   --window\t<INT>\twindow size (optional, default 200)\n"
                + "   --min_mapping_quality\t<INT>\tminumum mapping quality (optional,default 0)\n";
        String usage2 = "usage: java -jar TrioCNV.jar " + command + " [OPTIONS]\n\n"
                + "-I,--input\t<FILE>\tread count file got by the preprocess step (required)\n"
                + "-P,--pedigree\t<FILE>\tpedigree file (required)\n"
                + "-M,--mappability\t<FILE>\tmappability file (required)\n"
                + "-O,--output\t<FILE>\toutput structural variation file (required)\n"
                + "   --min_mappability\t<FLOAT>\tminumum mappability(optional, default 0)\n"
                + "   --mutation_rate\t<FLOAT>\tde novo mutation rate (optional, default 0.0001)\n"
                + "   --transition_prob\t<FLOAT>\tprobability of transition between two different copy number states(optional, default 0.00001)\n"
                + "   --min_distance\t<INT>\tminumum distance to merge two adjacent CNVs (optional, default 10K)\n"
                + "   --outlier\t<FLOAT>\tthe predefined percentage of outliers (optional, default 0.025)\n"
                + "   --gc_bin_size\t<INT>\tsize of gc bin by percent (optional, default 1)\n"
                + "   --nt\t<INT>\tnumber of threads (optional, default 1)\n";
        if (command.equals("preprocess")) {
            System.out.println(usage1);
        } else {
            System.out.println(usage2);
        }
    }
}

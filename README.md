#TrioCNV
#Introduction
TrioCNV is a tool designed to jointly detecting CNVs from WGS data in parent-offspring trios. It models read depth signal with the negative binomial regression to accommodate over-dispersion and considered GC content and mappability bias. It leverages parent-offspring relationship to apply Mendelian inheritance constraint while allowing for the rare incidence of de novo events. It uses a hidden Markov model (HMM) by combining the two aforementioned models to jointly perform CNV segmentation for the trio.
#Installation
If you want to run TrioCNV, you'll need:

1. Java 1.7+

2. Apache Maven (if you want to build the source)

3. R (Rscript exectuable must be set in the PATH environment variable)

4. Runiversal package(http://cran.rproject.org/web/packages/Runiversal/index.html) in R environment

5. The easiest way to get TrioCNV is to download the binary distribution from the TrioCNV github release page. Alternatively, you can build TrioCNV from source with Maven. 

	git clone --recursive https://github.com/yongzhuang/TrioCNV.git
	
	cd TrioCNV/
	
	mvn compile package

#Running 

usage: java -jar TrioCNV.jar <COMMAND> [OPTIONS] 

1. preprocess 

This command is to extract the information from the BAM file. 

	usage: java -jar TrioCNV.jar preprocess [OPTIONS] 

	-R,--reference  <FILE>  reference genome file (required)

	-B,--bams       <FILE>  bam list file (required)

	-P,--pedigree   <FILE>  pedigree file (required)

	-M,--mappability        <FILE>  mappability file (required)

	-O,--output     <FILE>  perfix of output file (required)

	   --window     <INT>   window size (optional, default 200)
	   
	   --min_mapping_quality        <INT>   minumum mapping quality (optional,default 0)

2. call 

This command is to jointly call CNVs from a parent-offspring trio.

	usage: java -jar TrioCNV.jar call [OPTIONS] 

	-I,--input      <FILE>  read count file got by the preprocess step (required)

	-P,--pedigree   <FILE>  pedigree file (required)

	-M,--mappability        <FILE>  mappability file (required)

	-O,--output     <FILE>  output structural variation file (required)

	   --min_mappability    <FLOAT> minumum mappability(optional, default 0)
	   
	   --mutation_rate      <FLOAT> de novo mutation rate (optional, default 0.0001)
	   
	   --transition_prob    <FLOAT> probability of transition between two different copy number states(optional, default 0.00001)
	   
	   --outlier	<FLOAT>	the predefined percentage of outliers (optional, default 0.025)
	   
	   --min_distance       <INT>   minumum distance to merge two adjacent CNVs (optional, default 10K)
	   
	   --nt <INT>   number of threads (optional, default 1)
	   
	   --gc_bin_size	<INT>	size of gc bin by percent (optional, default 1)

#File Instruction

1. bam list file (one columns) 

Column 1: path of .bam file

Example: 

	/path/Sample1.bam

	/path/Sample2.bam

	/path/Sample3.bam
	...

2) pedigree file

See (http://pngu.mgh.harvard.edu/~purcell/plink/data.shtml)

Note: The Individual ID must be same as the @RG SM tag of the bam file.


#Contact
<yzhuangliu@gmail.com>

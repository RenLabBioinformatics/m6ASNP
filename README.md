# m6ASNP
## 1.m6ASNP: a tool for annotating genetic variants by m6A function
<br/>
<p align=justify>&nbsp&nbspm6ASNP is an online service that predicts and annotates N6-methyladenosine alterations from genetic variants data such as germline SNPs 
or cancer somatic mutations. Based on the recently published miCLIP data, we trained two accurate prediction models for human and mouse 
using Random Forest algorithm. Combining genetic variants in the prediction model, we separately predicted the m6A status in reference 
sample and mutant sample. For those m6As that occurred in reference sample and lost in mutant sample, we defined them as m6A-loss 
alterations. Whilst in the opposite case, we defined them as m6A-gain alterations. Taking advantages of other annotation tools, we 
developed an analysis pipeline to interpret the landscape distribution of predicted m6A alterations at a whole genomic level. Multiple 
statistical diagrams and genome browser are also embed in the web server to provide visualization for all the analysis results.</p>
<br/>
<p align=center><img src="http://app.renlab.org/m6ASNP/img/PredictionModel.png"></p>
<br/>

## 2.Installation of m6ASNP
```
#Download from Github
git clone https://github.com/RenLabBioinformatics/m6ASNP.git
cd  m6AVarAnno

#Complie the source code
#Required the maven build tool was installed
cd m6AVarAnno
mvn package
```
<br/>

## 3.Example data
m6ASNP support an input of formatted text in VCF format or simple tabular format.
<br/>
* An example of VCF format:
```
##fileformat=VCFv4.1
##fileDate=20090805
##source=myImputationProgramV3.1
##reference=file:///seq/references/1000GenomesPilot-NCBI36.fasta
##contig=<ID=20,length=62435964,assembly=B36,md5=f126cdf8a6e0c7f379d618ff66beb2da,species="Homo sapiens",taxonomy=x>
##phasing=partial
##INFO=<ID=NS,Number=1,Type=Integer,Description="Number of Samples With Data">
##INFO=<ID=DP,Number=1,Type=Integer,Description="Total Depth">
##INFO=<ID=AF,Number=A,Type=Float,Description="Allele Frequency">
##INFO=<ID=AA,Number=1,Type=String,Description="Ancestral Allele">
##INFO=<ID=DB,Number=0,Type=Flag,Description="dbSNP membership, build 129">
##INFO=<ID=H2,Number=0,Type=Flag,Description="HapMap2 membership">
##FILTER=<ID=q10,Description="Quality below 10">
##FILTER=<ID=s50,Description="Less than 50% of samples have data">
##FORMAT=<ID=GT,Number=1,Type=String,Description="Genotype">
##FORMAT=<ID=GQ,Number=1,Type=Integer,Description="Genotype Quality">
##FORMAT=<ID=DP,Number=1,Type=Integer,Description="Read Depth">
##FORMAT=<ID=HQ,Number=2,Type=Integer,Description="Haplotype Quality">
#CHROM  POS ID  REF ALT QUAL    FILTER  INFO    FORMAT  NA00001 NA00002 NA00003
20  14370   rs6054257   G   A   29  PASS    NS=3;DP=14;AF=0.5;DB;H2 GT:GQ:DP:HQ:AD  0|0:48:1:51,51:0,1  1|0:48:8:51,51:4,4  1/1:43:5:.,.:1,4
20  17330   .   T   A   3   q10 NS=3;DP=11;AF=0.017 GT:GQ:DP:HQ 0|0:49:3:58,50  0|1:3:5:65,3    0/0:41:3
20  1110696 rs6040355   A   G,T 67  PASS    NS=2;DP=10;AF=0.333,0.667;AA=T;DB   GT:GQ:DP:HQ 1|2:21:6:23,27  2|1:2:0:18,2    2/2:35:4
20  1230237 .   T   .   47  PASS    NS=3;DP=13;AA=T GT:GQ:DP:HQ 0|0:54:7:56,60  0|0:48:4:51,51  0/0:61:2
20  1234567 microsat1   GTC G,GTCT  50  PASS    NS=3;DP=9;AA=G  GT:GQ:DP:AD 0/1:35:4:2,1,1  0/2:17:5:1,1,3  1/1:40:3:0,2,1
22  30421786    TR1 A   T   29  PASS    NS=3;DP=14;AF=0.5;DB;H2 GT:GQ:DP:HQ:AD  0|0:48:1:51,51:1,0  1|0:48:8:51,51:4,4  1/1:43:5:.,.:0,5
17  41244435    VBRCA1  T   C   30  PASS    NS=3;DP=14;AF=0.5;DB;H2 GT:GQ:DP:HQ:AD  0|0:48:3:51,51:3,0  1|0:48:8:51,51:3,5  1/1:43:6:.,.:0,6
22  29446079    .   A   G   67  PASS    NS=2;DP=10;AF=0.333,0.667;AA=T;DB   GT:GQ:DP:HQ 1|0:21:6:23,27  0|0:2:0:18,2    1/1:35:4
22  40814500    TR3 T   C   3   q10 NS=3;DP=11;AF=0.017 GT:GQ:DP:HQ 0|0:49:3:58,50  0|1:3:5:65,3    0/0:41:3
22  40815256    .   C   T   47  PASS    NS=3;DP=13;AA=T GT:GQ:DP:HQ 1|0:54:7:56,60  0|1:48:4:51,51  0/0:61:2
12  123466292   .   GGAAGAAGAA  G,GGAA,GGAAGAA  50  PASS    NS=3;DP=13;AA=T GT:GQ:DP:HQ:AD  0|1:48:4:51,51:2,2,0,0  1|2:21:6:23,27:0,2,4,0  3|1:48:8:51,51:0,3,0,5
```
<p align=justify><b>Note:</b>&nbspWe support the standard variant call format (VCF) version 4.2 specification. A lower VCF version are also well support by m6ASNP. A genotype field is required for prediction and multiple samples can be submitted in a single VCF file.</p>
<br/>

* An example of tabular format:
```
#Chromosome   Position    ID  Reference   Alteration  Sample
20  14370   rs6054257   G   A   NA00003
20  17330   .   T   A   NA00002
20  1110696 rs6040355   A   G,T NA00001
20  1110696 rs6040355   A   G,T NA00002
20  1110696 rs6040355   A   G,T NA00003
20  1230237 .   T   .   NA00001
20  1234567 microsat1   GTC GTCT    NA00003
22  30421786    TR1 A   T   NA00002
17  41244435    VBRCA1  T   C   NA00003
22  29446079    .   A   G   NA00003
22  40814500    TR3 T   C   NA00002
22  40815256    .   C   T   NA00001
22  40815256    .   C   T   NA00002
12  123466292   .   GGAAGAAGAA  G   NA00003
```
<p align=justify><b>Note:</b>&nbspThe simple tabular format requires six fixed fields at each line. Fields must be tab-separated. Lines prefixed with octothorpe (#) will be regarded as comments and ignored by the prediction tool. Also, all the fields in each feature line must contain a value; 'empty' columns should be denoted with a '.'</p>

* <p align=justify><b>Chromosome:</b>&nbspname of the chromosome or scaffold; chromosome names can be given with or without the 'chr' prefix.</p>

* <p align=justify><b>Position:</b>&nbspstartposition of the genetic variants, with sequence numbering starting at 1.</p>

* <p align=justify><b>ID:</b>&nbspidentifier of the genetic variants, i.e., rsID in dbSNP.</p>

* <p align=justify><b>Reference:</b>&nbspreference base(s). Each base must be one of A,C,G,T,N. Insertion denote as '-'.</p>

* <p align=justify><b>Alteration:</b>&nbspalternate base(s). Deletion denote as '-'.<p>

* <p align=justify><b>Sample:</b>&nbspSample ID or sample name regarding the genetic variants.</p>

<br/>

## 4.Contact
If you are having trouble please contact: Dr. Jian Ren (renjian.sysu@gmail.com), Dr. Zhixiang Zuo (zuozhx@sysucc.org.cn) or Dr. Yubin Xie (xieyb6@mail.sysu.edu.cn). We will try to resolve it.

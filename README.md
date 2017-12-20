# m6ASNP
## m6ASNP: a tool for annotating genetic variants by m6A function
<br/>
<p align=justify>m6ASNP is an online service that predicts and annotates N6-methyladenosine alterations from genetic variants data such as germline SNPs 
or cancer somatic mutations. Based on the recently published miCLIP data, we trained two accurate prediction models for human and mouse 
using Random Forest algorithm. Combining genetic variants in the prediction model, we separately predicted the m6A status in reference 
sample and mutant sample. For those m6As that occurred in reference sample and lost in mutant sample, we defined them as m6A-loss 
alterations. Whilst in the opposite case, we defined them as m6A-gain alterations. Taking advantages of other annotation tools, we 
developed an analysis pipeline to interpret the landscape distribution of predicted m6A alterations at a whole genomic level. Multiple 
statistical diagrams and genome browser are also embed in the web server to provide visualization for all the analysis results.</p>
<br/>
<img src="http://app.renlab.org/m6ASNP/img/PredictionModel.png">
<br/>

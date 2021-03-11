package org.acoli.glaser.metadata.pdf.util;

public class F1ScoreImplementation {

    public double implementF1score(double totalFiles, double foundMatches, double actualMatches){
        double differenceInMatches = foundMatches-actualMatches; // A positive Number, a negative number or zero depending on the accuracy
        double truePostives = foundMatches - differenceInMatches;
        double falsePositives;
        if(actualMatches < foundMatches){
            falsePositives = foundMatches - actualMatches;
        }else{
            falsePositives = 0;
        }
        double noMatches = totalFiles - foundMatches; //this is a little tricky, it could be possible, that totalFiles might not be varrying, in this case, the calculation needs to be adjusted
        double trueNegatives = noMatches + differenceInMatches;
        double falseNegatives;
        double actualNoMatch = totalFiles - noMatches;
        if(actualNoMatch < noMatches){
            falseNegatives = noMatches - actualMatches;
        }else{
            falseNegatives = 0;
        }

        double result = calculateF1Score(truePostives, falsePositives, trueNegatives, falseNegatives);
        return result;
    }

    //the method calculates the f1score
    public double calculateF1Score(double truePositives, double falsePositives, double trueNegatives, double falseNegatives){
        double precision = calculatePrecision(truePositives, falsePositives);
        double recall = calculateRecall(truePositives, falseNegatives);

        double f1Score = 2*((precision*recall)/(precision+recall));
        return f1Score;
    }

    //assisting method for calculateF1Score
    public double calculatePrecision(double truePositives, double falsePositives){
        double precision = truePositives/(truePositives+falsePositives);
        return precision;
    }

    //assisting method for calculateF1Score
    public double calculateRecall(double truePostives, double falseNegatives){
        double recall = truePostives/(truePostives + falseNegatives);
        return recall;
    }
}

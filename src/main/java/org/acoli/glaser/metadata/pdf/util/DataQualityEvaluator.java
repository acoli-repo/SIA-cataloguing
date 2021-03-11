package org.acoli.glaser.metadata.pdf.util;

public class DataQualityEvaluator {


    public double calculateF1Score(double truePositives, double falsePositives, double trueNegatives, double falseNegatives){
        double precision = calculatePrecision(truePositives, falsePositives);
        double recall = calculateRecall(truePositives, falseNegatives);

        double f1Score = 2*((precision*recall)/(precision+recall));
        return f1Score;
    }

    public double calculatePrecision(double truePositives, double falsePositives){
        double precision = truePositives/(truePositives+falsePositives);
        return precision;
    }

    public double calculateRecall(double truePostives, double falseNegatives){
        double recall = truePostives/(truePostives + falseNegatives);
        return recall;
    }
}

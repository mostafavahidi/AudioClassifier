package edu.cpp.cs.cs240;

import java.io.File;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {
	public static void main(String[] args) throws Exception {
		// FreqArrayConverter testOne = new FreqArrayConverter();
		// testOne.rawFreqCreator();
		// FreqArrayConverterFAST freqArr = new FreqArrayConverterFAST();
		// TrainingArffFileCreator trainingFile = new TrainingArffFileCreator();
		// TestArffFileCreator testFile = new TestArffFileCreator();
		// double[] compressedBand = new double[1048576];
		// testTwoFile.attributeExtractor(testTwo.compressBands(testTwo.rawFreqCreator(),
		// compressedBand, testTwo.rawFreqCreator().length, 1048576));

		// trainingFile.attributeExtractor(freqArr.linearFreqMatrixCreator(400));
		// testFile.attributeExtractor(freqArr.linearFreqMatrixCreator(200));

		// Weka classify = new Weka();
		//
		// classify.wekaMethod();

		AudioAPI test = new AudioAPI();
		File audioInput = new File("/users/Mostafa/Workspace/AudioClassifier/testDirUncut/test.wav");
		System.out.println(test.predictor(audioInput));

	}
}

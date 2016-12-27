package edu.cpp.cs.cs240;


public class Main {
	public static void main(String[] args) throws Exception{
//		FreqArrayConverter testOne = new FreqArrayConverter();
//		testOne.rawFreqCreator();
//		FreqArrayConverterFAST freqArr = new FreqArrayConverterFAST();
//		TrainingArffFileCreator trainingFile = new TrainingArffFileCreator();
//		TestArffFileCreator testFile = new TestArffFileCreator();
//		double[] compressedBand = new double[1048576];
//		testTwoFile.attributeExtractor(testTwo.compressBands(testTwo.rawFreqCreator(), compressedBand, testTwo.rawFreqCreator().length, 1048576));
		
//		trainingFile.attributeExtractor(freqArr.linearFreqMatrixCreator(400));
//		testFile.attributeExtractor(freqArr.linearFreqMatrixCreator(200));

		Weka classify = new Weka();
		
		classify.wekaMethod();
		
	}
}

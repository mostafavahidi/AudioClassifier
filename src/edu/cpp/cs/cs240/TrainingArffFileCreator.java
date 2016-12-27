package edu.cpp.cs.cs240;

import java.io.PrintWriter;
import java.util.stream.DoubleStream;

public class TrainingArffFileCreator {

	public void attributeExtractor(double[][] linearArrayMatrix) throws Exception {
		PrintWriter writer = new PrintWriter("TrainingAudio.arff", "UTF-8");
		writer.println("@relation Audio-weka.filters.unsupervised.instance.NonSparseToSparse");
		writer.println("@attribute 'Volume' real");
		writer.println("@attribute 'Number of Extreme Frequencies' real");
		writer.println("@attribute 'Spectrul Flux' real");
		writer.println("@attribute 'Type' {commercial, noncommercial}");
		writer.println("");
		writer.println("@data");

		for (int row = 0; row < linearArrayMatrix.length; row++) {
			// Creating Volume Attribute value.
			double sumFreq = 0;
			for (int a = 0; a < linearArrayMatrix[row].length; a++) {
				sumFreq += linearArrayMatrix[row][a];
			}
			double avgFreqValue = sumFreq / linearArrayMatrix[row].length;

			// Getting data for Volume attribute.
			double[] squaredLinearArray = new double[linearArrayMatrix[row].length];
			for (int j = 0; j < squaredLinearArray.length; j++) {
				squaredLinearArray[j] = Math.pow(linearArrayMatrix[row][j], 2.0);
				System.out.println("Creating volume attribute.");
			}
			double volume = DoubleStream.of(squaredLinearArray).sum();

			// Creating Silent Frequency attribute value.
			// double[] silentFreqArray = new double[32];
			// int numSilentFreq = 0;
			// for (int l = 0; l < 32; l++) {
			// silentFreqArray[l] = Math.pow(2.0, (l + 1) / 2.89) + 300;
			// System.out.println("Creating Silent Frequency attribute.");
			// }
			// for (int s = 0; s < silentFreqArray.length; s++) {
			// if (silentFreqArray[s] < 0.15 * avgFreqValue) {
			// numSilentFreq++;
			// }
			// System.out.println("Creating Silent Frequency 2 attribute.");
			// }
			// double silentFreq = numSilentFreq;

			// Creating Extreme Frequency attribute value.
			int numExtremeFreq = 0;
			for (int d = 0; d < linearArrayMatrix[row].length; d++) {
				if (linearArrayMatrix[row][d] < 0.2 * avgFreqValue || linearArrayMatrix[row][d] > 3 * avgFreqValue) {
					numExtremeFreq++;
				}
				System.out.println("Creating Extreme Frequency attribute.");
			}
			double extremeFreq = numExtremeFreq;

			// Creating Spectrul Flux attribute value.
			double specFlux = 0;
			double[] specFluxData = new double[(linearArrayMatrix[row].length) - (linearArrayMatrix[row].length / 4)];
			int indexCounter = 0;
			for (int j = 0; j < specFluxData.length; j++) {
				specFluxData[j] = Math.abs(linearArrayMatrix[row][indexCounter]
						- (linearArrayMatrix[row][indexCounter + (linearArrayMatrix[row].length / 4)]));
				indexCounter++;
			}
			double spectrulFlux = DoubleStream.of(specFluxData).sum();

			// Creating type attribute value
			String type = "";
			if (row < linearArrayMatrix.length / 2) {
				type = "commercial";
			} else {
				type = "noncommercial";
			}

			writer.print(volume);
			writer.print(",");
			// writer.print(silentFreq);
			// writer.print(",");
			writer.print(extremeFreq);
			writer.print(",");
			writer.print(spectrulFlux);
			writer.print(",");
			writer.print(type);
			writer.println();
		}

		writer.close();
		System.out.println("ARFF FILE CREATED!");
	}
}

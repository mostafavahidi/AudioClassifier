package edu.cpp.cs.cs240;

import java.io.File;
import java.util.stream.DoubleStream;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.NonSparseToSparse;

/**
 * Generates a sparse ARFF file.
 *
 * @author Mostafa
 */
@SuppressWarnings("deprecation")
public class ArffFileCreator {

	public void attributeExtractor(double[][] linearArrayMatrix) throws Exception {
		FastVector<Attribute> attributes;
		Instances dataSet;
		double[] values;
		attributes = new FastVector<Attribute>();
		attributes.addElement(new Attribute("Volume"));
		attributes.addElement(new Attribute("Energy Balance"));
		attributes.addElement(new Attribute("Number of Silent Frequencies"));
		attributes.addElement(new Attribute("Extreme Frequency Values"));
		attributes.addElement(new Attribute("Spectrul Flux"));
		attributes.addElement(new Attribute("Segment Length"));
		attributes.addElement(new Attribute("Music or not; 1 for music, -1 for ad"));

		dataSet = new Instances("Audio", attributes, 0);

		for (int row = 0; row < linearArrayMatrix.length; row++) {
			values = new double[dataSet.numAttributes()];
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
			values[0] = DoubleStream.of(squaredLinearArray).sum();
			// //Getting data for Energy Balance attribute.
			// for (int k = 0; k < linearArray.length; k+= 6){
			//
			// }
			// values[1] = ;
			// Getting data for Number of Silent Frequencies attribute.
			double[] silentFreqArray = new double[32];
			int numSilentFreq = 0;
			for (int l = 0; l < 32; l++) {
				silentFreqArray[l] = Math.pow(2.0, (l + 1) / 2.89) + 300;
				System.out.println("Creating Silent Frequency attribute.");
			}
			for (int s = 0; s < silentFreqArray.length; s++) {
				if (silentFreqArray[s] < 0.15 * avgFreqValue) {
					numSilentFreq++;
				}
				System.out.println("Creating Silent Frequency 2 attribute.");
			}
			values[2] = numSilentFreq;

			// Getting data for Extreme Frequency Values attribute
			int numExtremeFreq = 0;
			for (int d = 0; d < linearArrayMatrix[row].length; d++) {
				if (linearArrayMatrix[row][d] < 0.2 * avgFreqValue || linearArrayMatrix[row][d] > 3 * avgFreqValue) {
					numExtremeFreq++;
				}
				System.out.println("Creating Extreme Frequency attribute.");
			}
			values[3] = numExtremeFreq;

			// Getting data for Spectrul Flux attribute.
			double specFlux = 0;
			double[] specFluxData = new double[(linearArrayMatrix[row].length) - (linearArrayMatrix[row].length / 4)];
			int indexCounter = 0;
			for (int j = 0; j < specFluxData.length; j++) {
				specFluxData[j] = Math.abs(linearArrayMatrix[row][indexCounter]
						- (linearArrayMatrix[row][indexCounter + (linearArrayMatrix[row].length / 4)]));
				indexCounter++;
			}
			values[4] = DoubleStream.of(specFluxData).sum();
			//
			// //Getting data for Segment Length attribute.
			// values[5] = ;
			// dataSet.add(new DenseInstance(1.0, values));
			if (row < linearArrayMatrix.length / 2) {
				values[6] = -1;
			} else {
				values[6] = 1;
			}

			System.out.println("Process of creating Arff File.");

			dataSet.add(new DenseInstance(1.0, values));
		}
		NonSparseToSparse nonSparseToSparseInstance = new NonSparseToSparse();
		nonSparseToSparseInstance.setInputFormat(dataSet);
		Instances sparseDataset = Filter.useFilter(dataSet, nonSparseToSparseInstance);
		System.out.println(sparseDataset);
		ArffSaver arffSaverInstance = new ArffSaver();
		arffSaverInstance.setInstances(sparseDataset);
		arffSaverInstance.setFile(new File("Audio.arff"));
		arffSaverInstance.writeBatch();
		System.out.println("ARFF FILE CREATED.");
	}
}

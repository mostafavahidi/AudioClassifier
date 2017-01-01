package edu.cpp.cs.cs240;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.Stacking;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.pmml.jaxbbindings.DecisionTree;

public class Weka {
	public void wekaMethod() throws Exception {
		// Load dataset
		DataSource source = new DataSource("/users/Mostafa/Workspace/AudioClassifier/TrainingAudio.arff");
		Instances trainDataSet = source.getDataSet();

		// Set Class index to the last attribute
		trainDataSet.setClassIndex(trainDataSet.numAttributes() - 1);

		// Creating bayes classifier!
		NaiveBayesMultinomial bayes = new NaiveBayesMultinomial();
		bayes.buildClassifier(trainDataSet);

		Evaluation eval = new Evaluation(trainDataSet);

//		//Building Stacking model
//		Stacking stacker = new Stacking();
//		stacker.setMetaClassifier(new NaiveBayesMultinomial());
//		Classifier[] classifiers ={
//				new NaiveBayesMultinomial(),
//				new RandomForest(),
//				new DecisionStump(),
//				new NaiveBayes(),
//				new JRip(),
//				new HoeffdingTree()
//				
//				
//				
//		};
//		stacker.setClassifiers(classifiers);
//		stacker.buildClassifier(trainDataSet);
////		
//		Evaluation eval = new Evaluation(trainDataSet);
		
//		//Building Bagging model
//		Bagging bagger = new Bagging();
//		bagger.setClassifier(new NaiveBayesMultinomial());
//		bagger.setNumIterations(25);
//		bagger.buildClassifier(trainDataSet);
//		Evaluation eval = new Evaluation(trainDataSet);
//		
//		//Building AdaBoost model
//		AdaBoostM1 m1 = new AdaBoostM1();
//		m1.setClassifier(new J48());
//		m1.setNumIterations(20);
//		m1.buildClassifier(trainDataSet);
//		Evaluation eval = new Evaluation(trainDataSet);
		
		// Test dataset for evaluation.
		DataSource source1 = new DataSource("/users/Mostafa/Workspace/AudioClassifier/TestAudio.arff");
		Instances testDataSet = source1.getDataSet();

		// Set class index to the last attribute
		testDataSet.setClassIndex(testDataSet.numAttributes() - 1);
		// Now evaluate model.
		eval.evaluateModel(bayes, testDataSet);

		System.out.println(eval.toSummaryString("Evaluation results:\n", false));
		//
		// System.out.println("Correct % = " + eval.pctCorrect());
		// System.out.println("Incorrect % = " + eval.pctIncorrect());
		// System.out.println("Area Under Curve = " + eval.areaUnderROC(1));
		// System.out.println("kappa = " + eval.kappa());
		// System.out.println("Mean Absolute Error = " +
		// eval.meanAbsoluteError());
		// System.out.println("Root Mean Squared Error = " +
		// eval.rootMeanSquaredError());
		// System.out.println("Relative Absolute Error = " +
		// eval.relativeAbsoluteError());
		// System.out.println("Root Relative Squared Error = " +
		// eval.rootRelativeSquaredError());
		// System.out.println("Precision = " + eval.precision(1));
		// System.out.println("Recall = " + eval.recall(1));
		// System.out.println("fMeasure = " + eval.fMeasure(1));
		// System.out.println("Error Rate = " + eval.errorRate());
		//
		// The confusion Matrix.
		System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));

		System.out.println("============================");
		System.out.println("Actual Class, J48 Predicted");
		for (int i = 0; i < testDataSet.numInstances(); i++) {
			// Get class double value for current instance.
			double actualClass = testDataSet.instance(i).classValue();
			// Get class string value using the class index using the class' int
			// value.
			String actual = testDataSet.classAttribute().value((int) actualClass);
			// Get instance object of current instance.
			Instance newInst = testDataSet.instance(i);
			// Call classifyInstance, which returns a double value for the
			// class.
			double predJ48 = bayes.classifyInstance(newInst);
			// Use this value to get string value of the predicted class.
			String predString = testDataSet.classAttribute().value((int) predJ48);
			System.out.println(actual + ", " + predString);
		}

	}
}

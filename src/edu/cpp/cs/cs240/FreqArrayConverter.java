package edu.cpp.cs.cs240;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class FreqArrayConverter {
	
	
	
	/**
	 * @param rawFreqs
	 *            the array of logarithmic frequencies
	 * @param compressedBand
	 *            the output of linear frequencies
	 * @param numRaw
	 *            the size of rawFreqs
	 * @param numCompressed
	 *            the size of compressedBand
	 */
	public static void compressBands(double[] rawFreqs, double[] compressedBand, int numRaw, int numCompressed) {
		if (rawFreqs == null || compressedBand == null || numRaw < 1 || numCompressed < 1){
			System.out.println("The input array is not important/large enough.");
		}
		// Start with 1Hz frequency
		double centerFreq = 1.0;
		double lowerFreqBound, upperFreqBound;
		int lowerIndex, upperIndex;
		// Work out N, which is the number of steps per
		// octave in the compressed band
		// If numCompressed is 202 this works out to exactly 24
		// steps per octave i.e. each step is a quarter note.
		double N = (numCompressed - 2) / (8.0 + 1.0 / 3.0);
		// Note that we skip the first few octaves as these are
		// in a frequency range (0-20Hz) that does not interest us
		int freqIter = (int) N * 6;
		double ten_N_log = 10.0 * N * Math.log10(2.0);
		for (int i = 0; i < numCompressed; i++) {
			compressedBand[i] = 0;
			// First calculate the frequencies;
			// We start with a frequency around 20Hz
			centerFreq = Math.pow(2.0, (3.0 * (freqIter)) / ten_N_log);
			freqIter++;
			lowerFreqBound = centerFreq / Math.pow(2.0, 1.0 / (2.0 * N));
			upperFreqBound = centerFreq * Math.pow(2.0, 1.0 / (2.0 * N));
			// Then convert them to array indices using the formula
			// index = Frequency*arraySize/(maxFreq)*2
			// NOTE max Freq = 22050 as we are sampling at 44100Hz
			lowerIndex = (int) (lowerFreqBound * ((double) numRaw) / 44100.0);
			upperIndex = (int) (upperFreqBound * ((double) numRaw) / 44100.0);
			if (upperIndex > numRaw)
				upperIndex = numRaw;
			if (upperIndex == lowerIndex)
				lowerIndex--;
			if (lowerIndex < 0)
				lowerIndex = 0;
			// Finally make the compressed band index be the mean of values
			// in the uncompressed band
			for (int j = lowerIndex; j < upperIndex; j++) {
				compressedBand[i] += rawFreqs[j];
			}
			if (upperIndex != lowerIndex)
				compressedBand[i] /= upperIndex - lowerIndex;
		}
	}
	public static double[] rawFreqCreator(){
		//Create a global buffer size
		final int EXTERNAL_BUFFER_SIZE = 2097152;
		//128000

		//Get the location of the sound file
		File soundFile = new File("MoodyLoop.wav");

		//Load the Audio Input Stream from the file        
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		//Get Audio Format information
		AudioFormat audioFormat = audioInputStream.getFormat();

		//Handle opening the line
		SourceDataLine	line = null;
		DataLine.Info	info = new DataLine.Info(SourceDataLine.class, audioFormat);
		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		//Start playing the sound
		//line.start();

		//Write the sound to an array of bytes
		int nBytesRead = 0;
		byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
		while (nBytesRead != -1) {
			try {
		     		nBytesRead = audioInputStream.read(abData, 0, abData.length);

			} catch (IOException e) {
		     		e.printStackTrace();
			}
			if (nBytesRead >= 0) {
		     		int nBytesWritten = line.write(abData, 0, nBytesRead);
			}

		}

		//close the line      
		line.drain();
		line.close();
		
		//Calculate the sample rate
		float sample_rate = audioFormat.getSampleRate();
		System.out.println("sample rate = "+sample_rate);

		//Calculate the length in seconds of the sample
		float T = audioInputStream.getFrameLength() / audioFormat.getFrameRate();
		System.out.println("T = "+T+ " (length of sampled sound in seconds)");

		//Calculate the number of equidistant points in time
		int n = (int) (T * sample_rate) / 2;
		System.out.println("n = "+n+" (number of equidistant points)");

		//Calculate the time interval at each equidistant point
		float h = (T / n);
		System.out.println("h = "+h+" (length of each time interval in second)");
		
		//Determine the original Endian encoding format
		boolean isBigEndian = audioFormat.isBigEndian();

		//this array is the value of the signal at time i*h
		int x[] = new int[n];

		//convert each pair of byte values from the byte array to an Endian value
		for (int i = 0; i < n*2; i+=2) {
			int b1 = abData[i];
			int b2 = abData[i + 1];
			if (b1 < 0) b1 += 0x100;
			if (b2 < 0) b2 += 0x100;
			int value;

			//Store the data based on the original Endian encoding format
			if (!isBigEndian) value = (b1 << 8) + b2;
			else value = b1 + (b2 << 8);
			x[i/2] = value;
			
		}
		
		//do the DFT for each value of x sub j and store as f sub j
		double f[] = new double[n/2];
		for (int j = 0; j < n/2; j++) {

			double firstSummation = 0;
			double secondSummation = 0;

			for (int k = 0; k < n; k++) {
		     		double twoPInjk = ((2 * Math.PI) / n) * (j * k);
		     		firstSummation +=  x[k] * Math.cos(twoPInjk);
		     		secondSummation += x[k] * Math.sin(twoPInjk);
			}

		        f[j] = Math.abs( Math.sqrt(Math.pow(firstSummation,2) + 
		        Math.pow(secondSummation,2)) );

			double amplitude = 2 * f[j]/n;
			double frequency = j * h / T * sample_rate;
			System.out.println("frequency = "+frequency+", amp = "+amplitude);
		}
		System.out.println("DONE");
		return f;
		
	}

}

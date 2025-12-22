package spl.lae;

import java.io.IOException;

import memory.SharedVector;
import memory.VectorOrientation;
import parser.*;

public class Main {
	public static void main(String[] args) throws IOException {
        // Done: main
		if(args.length < 1) {
			System.err.println("No path to input JSON file provided.");
			return;
		}

		// Parse input arguments
		int numThreads = Integer.parseInt(args[0]);
		String inputFilePath = args[1];
		String outputFilePath = args[2];
		
		// Init Linear Algebra Engine and Input Parser
		LinearAlgebraEngine engine = new LinearAlgebraEngine(numThreads);
		InputParser parser = new InputParser();
		
		try { 
			ComputationNode node = parser.parse(inputFilePath);
			ComputationNode result =engine.run(node);
			OutputWriter.write(result.getMatrix(), outputFilePath);
		}
		catch (Exception e) {// Catch parsing exceptions
			OutputWriter.write(e.getMessage(), outputFilePath);
			return;
		}

		boolean testing = true;
		// Print worker report if in testing mode
		if(testing) {
			System.out.println("Worker report");
			System.out.println(engine.getWorkerReport());
		}
	}
}
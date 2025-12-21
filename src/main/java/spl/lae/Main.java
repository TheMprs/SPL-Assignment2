package spl.lae;

import java.io.IOException;

import memory.SharedVector;
import memory.VectorOrientation;
import parser.*;

public class Main {
	public static void main(String[] args) throws IOException {
        // TODO: main
		if(args.length < 1) {
			System.err.println("No path to input JSON file provided.");
			return;
		}

		// Parse input arguments
		String inputFilePath = args[0];
		String outputFilePath = args[1];
		int numThreads = Integer.parseInt(args[2]);

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

		boolean testing = false;
		// Print worker report if in testing mode
		if(testing) {
			System.out.println("Worker report");
			System.out.println(engine.getWorkerReport());
		}
	}
}
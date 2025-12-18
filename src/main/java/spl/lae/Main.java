package spl.lae;
import java.io.IOException;

import memory.SharedVector;
import memory.VectorOrientation;
import parser.*;

public class Main {
    public static void main(String[] args) throws IOException {
      // TODO: main

      // --- Vector Testing ---
      System.out.println("--- Vector Testing ---");
      double[] arrtest1 = {1,2,3};
      SharedVector vectortest1 = new SharedVector(arrtest1, VectorOrientation.ROW_MAJOR); 

      for (int i = 0; i < vectortest1.length(); i++) {
        System.out.print(vectortest1.get(i));
      }

    }
}
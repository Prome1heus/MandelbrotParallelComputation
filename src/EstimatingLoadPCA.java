import java.util.Random;

public class EstimatingLoadPCA {
    /*
     * Uses sample points in order to estimate how much computation an area needs
     * Tries to select areas which need a similar time to compute
     * For more information see pdf
     */


    private static void divide(int[]elements, int[] endvalues, int nThreads){
       int total = 0;
       //estimate total computation time
       for (int i=0; i<elements.length; i++) total += elements[i];
       total /= nThreads;

       //divide the area into vertical strips of similar computation effort
       int j=0;
       for (int i=0; i<nThreads-1; i++){
           int sum = 0;
           while (sum<total) sum += elements[j++];
           if (Math.abs(total-sum)>Math.abs(total-sum-elements[j])) j--;
           endvalues[i] = j;
       }
       endvalues[nThreads-1] = elements.length-1;
    }

    public static double computeValues(int[][] result, ComplexNumber[][] constants, int iterations, int startX, int stopX, int startY, int stopY, int nThreads){
        double startMillis = System.currentTimeMillis();
        int cols = stopX-startX+1;
        int rows = stopY-startY+1;

        //select one sample point per column and compute it
        Random r = new Random();
        int[] estimates = new int[cols];
        for (int i=0; i<cols; i++){
            int j = r.nextInt(rows);
            ComplexNumber c = constants[i][j];
            ComplexNumber z = new ComplexNumber(0, 0);
            int k;
            for (k=1; k <= iterations; k++){
                z.square();
                z.add(c);
                if (z.modulus()>=2.0) break;
            }
            estimates[i] = k;
        }

        //divide the area
        int[] endvalues = new int[nThreads];
        for (int i=0; i<nThreads; i++) endvalues[i]=-1;
        divide(estimates, endvalues, nThreads);

        //supervise computation threads
        ComputationThread[] threads = new ComputationThread[nThreads];
        threads[0] = new ComputationThread(result, constants, iterations, 0, endvalues[0], startY, stopY);
        for (int i=1; i<nThreads; i++){
            threads[i] = new ComputationThread(result, constants, iterations, endvalues[i-1]+1, endvalues[i], startY, stopY);
            threads[i].start();
        }try{
            for (int i=0; i<nThreads; i++) threads[i].join();
        }catch (InterruptedException e){
            return -1.0;
        }
        return System.currentTimeMillis() - startMillis;
    }
}

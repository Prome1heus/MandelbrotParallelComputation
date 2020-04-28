public class NaivePCA extends PCA {
    /*
     * Divides the area into subdivisions of equal size
     * Used to demonstrate the effect of better algorithms
     * For more Information see pdf
     */

    public static double computeValues(int[][] result, ComplexNumber[][] constants, int iterations, int startX, int stopX, int startY, int stopY, int nThreads){
        double startMillis = System.currentTimeMillis();
        ComputationThread[] threads = new ComputationThread[nThreads];
        int step = (stopX -startX)/nThreads;
        for (int i=0; i<nThreads; i++){
            threads[i] = new ComputationThread(result, constants, iterations, startX+i*step, startX+(i+1)*step, startY, stopY);
            threads[i].start();
        }try{
            for (int i=0; i<nThreads; i++) threads[i].join();
        }catch (InterruptedException e){
            return -1.0;
        }
        return System.currentTimeMillis() - startMillis;
    }
}

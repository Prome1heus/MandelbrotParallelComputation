import java.util.Random;

public class RandomLoadPCA {
    /*
     * Shuffles points of the area before assigning them
     * This method was ultimately not used for the comparision due to a lack of space to describe it
     */

    public static double computeValues(int[][] result, ComplexNumber[][] constants, int iterations, int startX, int stopX, int startY, int stopY, int nThreads){
        double startMillis = System.currentTimeMillis();
        int cols = stopX-startX+1;
        int[] map = new int[cols];
        int[] numbers = new int[cols];
        for (int i=0; i<cols; i++) numbers[i] = i;

        //map numbers
        Random r = new Random();
        for (int i=0; i<cols; i++){
            int n = r.nextInt(cols - i);
            map[i] = n;
            int temp = numbers[cols - i - 1];
            numbers[cols - i - 1] = numbers[n];
            numbers[n] = temp;
        }

        ComplexNumber[][] mappedConstants = new ComplexNumber[constants.length][constants[0].length];
        for (int i=0; i<cols; i++) mappedConstants[i] = constants[map[i]];
        ComputationThread[] threads = new ComputationThread[nThreads];
        //compute the shuffled sets of points
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

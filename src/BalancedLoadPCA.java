public class BalancedLoadPCA {
    /*
     * Divides the area evenly in subdivisions
     * Each thread works on a single subdivision
     * If a thread finishes and subdivisions are left, it starts working on that subdivison
     * For more information see pdf
     */

    private static int subdivisionsPerThread;

    public static double computeValues(int[][] result, ComplexNumber[][] constants, int iterations, int startX, int stopX, int startY, int stopY, int nThreads){
        double startMillis = System.currentTimeMillis();
        ComputationThread[] threads = new ComputationThread[nThreads * subdivisionsPerThread];
        int step = (stopX -startX)/(nThreads * subdivisionsPerThread);
        for (int i=0; i<nThreads*subdivisionsPerThread; i++){
            threads[i] = new ComputationThread(result, constants, iterations, startX+i*step, startX+(i+1)*step, startY, stopY);
            threads[i].start();

            //wait until thread is available
            while (ComputationThread.getThreadNumber()>=nThreads){
                try{
                    Thread.currentThread().sleep(3);
                }catch (InterruptedException e){
                    return -1.0;
                }
            }
        }try{
            for (int i=0; i<nThreads*subdivisionsPerThread; i++) threads[i].join();
        }catch (InterruptedException e){
            return -1.0;
        }
        return System.currentTimeMillis() - startMillis;
    }

    public static void setSubdivisionsPerThread(int subdivisionsPerThread) {
        BalancedLoadPCA.subdivisionsPerThread = subdivisionsPerThread;
    }
}

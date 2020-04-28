public class ComputationThread extends Thread {
    private int[][] result;
    private ComplexNumber[][] constants;
    private int iterations, startX, stopX, startY, stopY;
    private static int currThreadNumber = 0;

    private synchronized void incThreadNumber(){
        currThreadNumber++;
    }

    private synchronized void decThreadNumber(){
        currThreadNumber--;
    }

    public static synchronized int getThreadNumber(){
        return currThreadNumber;
    }

    public ComputationThread(int[][] result, ComplexNumber[][] constants, int iterations, int startX, int stopX, int startY, int stopY){
        this.result = result;
        this.constants = constants;
        this.iterations = iterations;
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
    }

    @Override
    public void run(){
        incThreadNumber();
        for (int x = startX; x <= stopX; x++){
            for (int y = startY; y <= stopY; y++){
                ComplexNumber c = constants[x][y];
                ComplexNumber z = new ComplexNumber(0, 0);

                //check if point is in the mandelbrot area (and visualization information)
                int i;
                for (i=1; i <= iterations; i++){
                    z.square();
                    z.add(c);
                    if (z.modulus()>=2.0) break;
                }
                result[x][y] = i;
            }
        }
        decThreadNumber();
    }

}

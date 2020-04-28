import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class AlgorithmComparision {

    static private void compare(PrintWriter writer, int iterations, ComplexNumber[][] cn, int nThreads, String savePath){
        int width = cn.length, height = cn[0].length;
        int[][] result = new int[width][height];

        //write the computed area to csv
        writer.print(cn[0][0] + ", " + cn[width-1][height-1] + ", ");

        //time per method needed to calculate the area using multiple threads
        double naiveTime = NaivePCA.computeValues(result, cn, iterations, 0, width-1, 0, height-1, nThreads);
        BalancedLoadPCA.setSubdivisionsPerThread(16);
        double balancedTime = BalancedLoadPCA.computeValues(result, cn , iterations, 0, width-1, 0, height-1, nThreads);
        double randomTime = RandomLoadPCA.computeValues(result, cn, iterations, 0, width-1, 0, height-1, nThreads);
        ComputationThread c = new ComputationThread(result, cn, iterations, 0, width-1, 0, height-1);
        double estimatedTime = EstimatingLoadPCA.computeValues(result, cn, iterations, 0, width-1, 0, height-1, nThreads);

        //reference time on a single thread
        double startMillis = System.currentTimeMillis();
        c.start();
        try{
            c.join();
        }catch (InterruptedException e){

        }
        double singleTime = System.currentTimeMillis()-startMillis;

        //save results
        MandelBrotImage.saveImage(result, savePath);
        writer.println(naiveTime + ", " + balancedTime + ", " + randomTime + ", " + estimatedTime + ", "  + singleTime + ", " + 100.0*(balancedTime/singleTime) + ", " + 100.0*(randomTime/singleTime) + ", " +100.0*(estimatedTime/singleTime) + ", " + 100.0*(naiveTime/singleTime));
    }

    /*
     * Generates evenly distributed points in a random area of the mandelbrot set
     * Used to create scenarios where different methods might be better suited
     */

    public static ComplexNumber[][] generateComplexField(ComplexNumber[][] cn, double scalingFactor){
        int width = cn.length, height = cn[0].length;
        Random r = new Random();

        //select area
        double startRe = scalingFactor* (-1.0 + r.nextDouble());
        double stopRe = scalingFactor* (1.0 - r.nextDouble());
        double startIm = scalingFactor* (-1.0 +r.nextDouble());
        double stopIm = scalingFactor* (1.0 - r.nextDouble());
        double stepRe = (stopRe-startRe)/width;
        double stepIm = (stopIm-startIm)/height;

        //calculate points in area
        for (int i=0; i<width; i++)
            for (int j=0; j<height; j++)
                cn[i][j] = new ComplexNumber(startRe + i*stepRe, startIm+j*stepIm);
        return cn;
    }

    static public void main(String[] args){
        try{

            //settings
            int images = 100;
            int width = 1920;
            int height = 1080;
            int iterations = 255;
            int nThreads = 4;
            double scalingFactor = 5;
            String csvPath = "output/csv/results.csv";
            String imagesPath = "output/images/img";

            //setup
            PrintWriter writer = new PrintWriter(new File(csvPath));
            ComplexNumber[][] cn = new ComplexNumber[width][height];
            writer.println("startValue, stopValue, naivePCA, balancedPCA, randomPCA, estimatingPCS, singleThread, balanced/single, random/single, estimated/single, naive/single");

            //compute sample areas for comparision
            for (int i=0; i<images; i++){
                compare(writer, iterations, generateComplexField(cn, scalingFactor), nThreads, imagesPath + i);
                System.out.println("finished image " + i + "/" + images);
            }

            writer.flush();
            writer.close();

        }catch (IOException e){
            System.out.println("An unexpectded error occured:" + e);
        }
    }
}

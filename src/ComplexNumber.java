public class ComplexNumber {
    public double Re, Im;

    public ComplexNumber(double Re, double Im){
        this.Re = Re;
        this.Im = Im;
    }

    public void add(ComplexNumber z){
        this.Re += z.Re;
        this.Im += z.Im;
    }

    public double modulus(){
        return Math.sqrt(Re*Re + Im*Im);
    }

    public void square(){
        double temp = Re;
        Re = Re*Re - Im*Im;
        Im = 2 * temp * Im;
    }

    @Override
    public String toString(){
        return ""  + Re + " + " + Im + "i";
    }
}

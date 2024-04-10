import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;

/**
 * PID Controller class,
 * NB: will only operate correctly if updates are made within one second from the last
 */
public class PID {
    
    private double  crntVal,
                    targetVal,
                    pExpo,
                    iExpo,
                    dExpo,
                    lastVal,
                    proportion,
                    integral,
                    derivative;
                   
    private Instant lastTime,
                    crntTime;
    
    private boolean writing;
    private PrintWriter writer;

    public PID(double value, double target, double pExponent, 
                                            double iExponent,
                                            double dExponent) {
        this.crntVal = value;
        this.targetVal = target;
        this.pExpo = pExponent;
        this.iExpo = iExponent;
        this.dExpo = dExponent;
        this.lastVal = value;
        this.integral = 0;
        this.derivative = 0;
        this.lastTime = Instant.now();
        this.crntTime = Instant.now();
        this.writing = false;
    }

    public void setCrntVal(double val) {
        this.lastVal = this.crntVal;
        this.crntVal = val;

        this.crntTime = Instant.now();
        double timeDiff = crntTime.minusNanos(this.lastTime.getNano()).getNano(); // calculate time since last update
        
        this.proportion = (this.targetVal - this.crntVal) * timeDiff/1000000000;
        
        this.integral += (this.lastVal - this.targetVal + (this.crntVal - this.lastVal)/2) //get value difference
                            * timeDiff/1000000000; //multiply by time difference

        double INFfixer = timeDiff < 1 ? 1 : timeDiff;
        this.derivative = (this.crntVal - this.lastVal)/(INFfixer);

        this.lastTime = Instant.now();
    }
    public void setTargetVal(double val) {this.targetVal = val;}

    public double getCrntVal() {return this.crntVal;}
    public double getTargetVal() {return this.targetVal;}
    public double getPExponent() {return this.pExpo;}
    public double getIExponent() {return this.iExpo;}
    public double getDExponent() {return this.dExpo;}


    public double calcDesiredVal(){
        return this.crntVal + this.pExpo*this.proportion + this.iExpo*this.integral + this.dExpo*this.derivative;
    }

    /**
     * renders the current state of the PID controller to the console
     */
    public void renderToConsole(){
        System.out.println("|  crntVal  |  targVal  |  dsrdVal  |     P     |     I     |     D     |");
        System.out.println("| % #8.2E | % #8.2E | % #8.2E | % #8.2E | % #8.2E | % #8.2E |"
                             .formatted(this.crntVal, this.targetVal, this.calcDesiredVal(),
                                        this.proportion, this.integral, this.derivative));
    }


    /**
     * attempts to start writing to a csv file
     * @return true if sucessful, 
     *         false if FileNotFoundException is thrown
     */
    public boolean startWriting() {
        try {
            this.writer = new PrintWriter(".\\PID_Data.csv");
        } catch (FileNotFoundException e){
            return false;
        }
        this.writing = true;
        writer.println("time, value, target, P, I, D");
        return true;
    }

    /**
     * renders the current state of the PID controller to a csv file for Microsoft Excel graphing
     * @requires startWriting() to have been called prior
     * @requiresa stopwriting() to be called to close the file
     */
    public void renderToFile(){
        if (this.writing){
            writer.println("%s, %E, %E, %E, %E, %E".formatted(this.crntTime.toString(), 
                                                              this.crntVal,
                                                              this.targetVal,
                                                              this.proportion,
                                                              this.integral,
                                                              this.derivative));
        }
    }

    public void stopWriting(){
        if (this.writing){
            this.writing = false;
            this.writer.close();
        }
    }
}

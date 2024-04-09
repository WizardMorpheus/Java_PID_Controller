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
                   
    private Instant lastTime;

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
        this.lastTime = Instant.MIN;
    }

    public void setCrntVal(double val) {
        this.lastVal = this.crntVal;
        this.crntVal = val;

        this.proportion = this.targetVal - this.crntVal;

        double timeDiff = Instant.now().minusNanos(this.lastTime.getNano()).getNano(); // calculate time since last update
        if (this.lastTime != Instant.MIN){
            this.integral += (this.lastVal - this.targetVal + (this.crntVal - this.lastVal)/2) //get value difference
                              * timeDiff/1000000; //multiply by time difference
        }

        this.derivative = (this.crntVal - this.lastVal)/(timeDiff);

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
}

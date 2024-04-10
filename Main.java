public class Main {

    private static void ClrScreen(){
        System.out.print("\033[H\033[2J");
    }

    public static void main(String[] args){
        PID pid = new PID(0, 10, 0.1, 0.5, 150);
        
        double power = 0;

        if (pid.startWriting()) {
            while (pid.isWriting()){

                // System.out.println(Input.hasNextLine());
                // if (Input.hasNextLine() && Input.nextLine().contains("e")) break;


                //render
                ClrScreen();
                //pid.renderToConsole();
                pid.renderToFile();
                //update
                power += (pid.calcDesiredVal() - power) * 0.001;
                pid.setCrntVal(pid.getCrntVal() + power);

                if (pid.targetReached()) pid.stopWriting();

            }
        }

    }

}
import java.util.Scanner;

public class Main {

    private static void ClrScreen(){
        System.out.print("\033[H\033[2J");
    }

    public static void main(String[] args){
        PID pid = new PID(0, 10, 1.5, 0.2, 0.1);
        
        if (pid.startWriting()) {
            while (pid.isWriting()){

                // System.out.println(Input.hasNextLine());
                // if (Input.hasNextLine() && Input.nextLine().contains("e")) break;


                //render
                ClrScreen();
                //pid.renderToConsole();
                pid.renderToFile();
                //update
                pid.setCrntVal(pid.calcDesiredVal());

                if (pid.targetReached()) pid.stopWriting();

            }
        }

    }

}
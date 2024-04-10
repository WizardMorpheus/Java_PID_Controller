import java.util.Scanner;

public class Main {

    private static void ClrScreen(){
        System.out.print("\033[H\033[2J");
    }

    public static void main(String[] args){
        PID pid = new PID(0, 10, 0.1, 0, 0);

        Scanner Input = new Scanner(System.in);
        
        if (pid.startWriting()) {
            while (true){

                // System.out.println(Input.hasNextLine());
                // if (Input.hasNextLine() && Input.nextLine().contains("e")) break;


                if (Double.isNaN(pid.getCrntVal())){
                    assert true;
                }

                if (pid.getCrntVal() - pid.getTargetVal() > -0.01 &&
                    pid.getCrntVal() - pid.getTargetVal() <  0.01) break;

                //render
                ClrScreen();
                pid.renderToConsole();
                pid.renderToFile();
                //update
                pid.setCrntVal(pid.calcDesiredVal());
            }
        }

        pid.stopWriting();
        Input.close();
    }

}
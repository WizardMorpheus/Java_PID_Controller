import java.util.Scanner;

public class Main {

    private static void ClrScreen(){
        System.out.print("\033[H\033[2J");
    }

    public static void main(String[] args){
        PID pid = new PID(0, 10, 0.1, 0, 0);

        Scanner Input = new Scanner(System.in);

        while (true){

            //render
            ClrScreen();
            pid.renderToConsole();

            while(!Input.hasNextLine());
            Input.nextLine();

            //update
            pid.setCrntVal(pid.calcDesiredVal());
        }
    }

}
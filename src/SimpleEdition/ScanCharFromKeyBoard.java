package SimpleEdition;

import java.util.Scanner;

public class ScanCharFromKeyBoard {
    private Scanner scan;
    private StreamWriter streamWriter;

    public ScanCharFromKeyBoard(StreamWriter streamWriter) {
        scan = new Scanner(System.in);
        this.streamWriter = streamWriter;
    }

    public void run() {
        char c = 'r';

        while (c != 'q') {
            c = scan.next().charAt(0);
            switch (c){
                case 'a':
                    this.streamWriter.write("left");
                    break;
                case 'd':
                    this.streamWriter.write("right");
                    break;
                case 'w':
                    this.streamWriter.write("up");
                    break;
                case 's':
                    this.streamWriter.write("down");
                    break;
            }
        }
    }
}

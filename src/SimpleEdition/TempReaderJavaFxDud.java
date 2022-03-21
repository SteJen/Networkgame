package SimpleEdition;

public class TempReaderJavaFxDud extends Thread{
    private final StreamReader streamReader;

    public TempReaderJavaFxDud(StreamReader streamReader) {
        this.streamReader = streamReader;
    }

    @Override
    public void run() {
        while (true) {
            String msg = this.streamReader.read();
            if (msg != null) {
                System.out.println(msg);
            }
        }
    }
}

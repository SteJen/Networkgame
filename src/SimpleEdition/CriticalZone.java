package SimpleEdition;

import java.util.ArrayDeque;
import java.util.Queue;

public class CriticalZone {
    private Queue<String> messages = new ArrayDeque<>();

    public synchronized void push(String msg) {
        messages.add(msg);
    }

    public synchronized String poll() {
        if (!messages.isEmpty()) {
            return messages.poll();
        }
        return "";
    }
}
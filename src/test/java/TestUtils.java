import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestUtils {

    public static String captureSystemOut(Runnable codeBlock) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream newOut = new PrintStream(outputStream)) {
            System.setOut(newOut);
            codeBlock.run();
            System.out.flush();
        } finally {
            System.setOut(originalOut);
        }

        return outputStream.toString().trim();
    }
}

package uub.staticLayer;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
public class ColouredLogger extends ConsoleHandler {

    @Override
    public void publish(LogRecord record) {
        String message = getFormatter().format(record);
        String coloredMessage = addColor(record.getLevel(), message);
        System.out.println(coloredMessage);
    }

    private String addColor(Level level, String message) {
        String colorCode;
        switch (level.getName()) {
            case "SEVERE":
                colorCode = "\u001B[31m"; // Red
                break;
            case "WARNING":
                colorCode = "\u001B[37m"; // Yellow
                break;
            case "FINEST'":
            	colorCode = "";
            case "INFO":
                colorCode = "\u001B[35m"; // SkyBlueGreen
                break;
            case "FINE":
            case "FINER":
                colorCode = "\u001B[1;37m"; // Cyan
                break;
            default:
                colorCode = "\u001B[0m"; // Reset color
                break;
        }
        return colorCode + message + "\u001B[0m"; // Reset color at the end
    }
}
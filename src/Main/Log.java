package Main;


import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Log {


    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");

    // COLORES
    private static final String COLOR_BLUE = "\033[0;94m";
    private static final String COLOR_RED = "\033[0;91m";
    private static final String COLOR_YELLOW = "\033[0;93m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static final String COLOR_GRAY = "\033[0;90m";

    //MENSAJES
    private static final String infoMsg = COLOR_GRAY + "[" + COLOR_BLUE + "i" + COLOR_GRAY + "] " + COLOR_RESET;
    private static final String warnMsg = COLOR_GRAY + "[" + COLOR_YELLOW + "*" + COLOR_GRAY + "] " + COLOR_RESET;
    private static final String errMsg = COLOR_GRAY + "[" + COLOR_RED + "!" + COLOR_GRAY + "] " + COLOR_RESET;

    private static void logmsg(PrintStream p, String lbl, String msg) {
        LocalDateTime now = LocalDateTime.now();
        p.println(lbl + COLOR_GRAY + "["+ f.format(now) + "]: " + COLOR_RESET + msg);
    }

    public static void INFO(String msg) {
        logmsg(System.out, infoMsg, msg);
    }

    public static void WARN(String msg) {
        logmsg(System.out, warnMsg, msg);
    }

    public static void ERR(String msg) {
        logmsg(System.err, errMsg, msg);
    }
}

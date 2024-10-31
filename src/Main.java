import java.io.File;
import java.io.IOException;
import java.util.Scanner;

// import org.junit.*;

public class Main {
    public static String currentDirectory = System.getProperty("user.dir");
    public static final String homeDirectory = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException {
        // print hello
        System.out.println("Hello, welcome to the terminal");
        Scanner in = new Scanner(System.in);

        while (Terminal.isRunning()) {
            Terminal terminal = new Terminal();
            terminal.pwd();
            String command = in.nextLine();
            boolean pipe = true;
            int commandNumber = 0;
            int lastSlash = 0;
            String nextCommand = "";
            while (pipe && commandNumber < 2) {
                commandNumber++;
                if (commandNumber == 1) {
                    lastSlash = command.indexOf("|");
                }
                if (lastSlash != -1 && commandNumber == 1) { // check if there pipe to repeate the loop for the second
                    // command
                    nextCommand = command.substring(lastSlash + 2, command.length());
                    command = command.substring(0, lastSlash);
                } else
                    pipe = false;

                String[] splited = command.split("\\s+"); // split by space
                Parser parser = new Parser(splited);
                String cmd = parser.getCmd();

                if (cmd.equalsIgnoreCase("pwd")) {
                    if (parser.getFirstArg().equals("")) {
                        System.out.println("Your current directory is:");
                        terminal.pwd();
                        System.out.println();
                    } else
                        System.out.println("pwd doesn't take any parameters");
                } else if (cmd.equalsIgnoreCase("mkdir")) {
                    if (parser.getSecondArg().equals("")) {
                        terminal.mkdir(parser.getFirstArg());
                    } else
                        System.out.println("mkdir takes only one parameter");
                } else if (cmd.equalsIgnoreCase("rmdir")) {
                    if (parser.getSecondArg().equals("")) {
                        terminal.rmdir(parser.getFirstArg());
                    } else
                        System.out.println("rmdir takes only one parameter");
                } else if (cmd.equalsIgnoreCase("cd")) {
                    if (parser.getSecondArg().equals("")) {
                        terminal.cd(parser.getFirstArg(), Main.currentDirectory);
                    } else
                        System.out.println("cd takes only one parameter");
                } else if (cmd.equalsIgnoreCase("ls")) {
                    boolean showAll = parser.isShowAll();
                    boolean reverseOrder = parser.isReverseOrder();
                    terminal.ls(new File(currentDirectory), showAll, reverseOrder);
                } else if (cmd.equalsIgnoreCase("touch")) {
                    String[] touchArgs = new String[splited.length - 1];
                    System.arraycopy(splited, 1, touchArgs, 0, splited.length - 1);
                    terminal.touch(touchArgs);
                } else if (cmd.equalsIgnoreCase("mv")) {
                    String[] mvArgs = new String[splited.length - 1];
                    System.arraycopy(splited, 1, mvArgs, 0, splited.length - 1);
                    terminal.mv(mvArgs);
                } else if (cmd.equalsIgnoreCase("rm")) {
                    String[] rmArgs = new String[splited.length - 1];
                    System.arraycopy(splited, 1, rmArgs, 0, splited.length - 1);
                    terminal.rm(rmArgs);
                } else if (cmd.equalsIgnoreCase("cat")) {
                    terminal.cat(parser.getFirstArg(), parser.getSecondArg());
                } else if (cmd.equalsIgnoreCase("help")) {
                    terminal.helpCommand();
                } else if (cmd.equalsIgnoreCase("exit")) {
                    terminal.exitCommand();
                    break;
                } else {
                    System.out.println("Unknown command: " + cmd);
                }
                if (lastSlash != -1) {
                    command = nextCommand;
                }
            }
        }
    }
}

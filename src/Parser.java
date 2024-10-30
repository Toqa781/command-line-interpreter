public class Parser {
    private String cmd;
    private String[] args;
    private boolean showAll = false;
    private boolean reverseOrder = false;
    public Parser(String[] command){
        cmd=command[0];
        args=command;
        parseFlags();
    }
    public String getCmd(){
        return cmd;
    }
    public String getFirstArg(){
        if(args.length<2){
            return "";
        }
        else return args[1];
    }
    public String getSecondArg(){
        if(args.length<3)
            return "";
        else return args[2];
    }
    private void parseFlags() {
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-a":
                    showAll = true;
                    break;
                case "-r":
                    reverseOrder = true;
                    break;
                default:
                    break;
            }
        }
    }
    public boolean isShowAll() {
        return showAll;
    }
    public boolean isReverseOrder() {
        return reverseOrder;
    }
}

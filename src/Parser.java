public class Parser {
    private String cmd;
    private String[] args;
    public Parser(String[] command){
        cmd=command[0];
        args=command;
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
}

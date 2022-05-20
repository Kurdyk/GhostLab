package Utils;

import java.util.HashMap;

public class CommandValidator {

    public static HashMap<String, Integer> sizes = new HashMap<>() {
    };

    /**
     * initiate valid command's list
     */
    public static void init() {
        sizes.put("NEWPL", 3);
        sizes.put("REGIS", 4);
        sizes.put("START", 1);
        sizes.put("SIZE?", 2);
        sizes.put("LIST?", 2);
        sizes.put("GAME?", 1);
        sizes.put("UNREG", 1);
        sizes.put("QUITS", 1);
        sizes.put("MALL?", 2);
        sizes.put("UPMOV", 2);
        sizes.put("DOMOV", 2);
        sizes.put("LEMOV", 2);
        sizes.put("RIMOV", 2);
    }

    public static boolean validate(String command){
        String[] args = command.split("\\u0020");
        return sizes.containsKey(args[0]) && sizes.get(args[0]) == args.length;
    }
}

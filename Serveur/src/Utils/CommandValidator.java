package Utils;

import java.util.Map;

public class CommandValidator {

    public static Map<String, Integer> sizes = Map.of(
            "NEWPL", 3,
            "REGIS", 4,
            "START", 1,
            "SIZE?", 2,
            "LIST?", 2,
            "GAME?", 1,
            "UNREG", 1,
            "QUITS", 1
    );

    public static boolean validate(String command){
        String[] args = command.split("\\u0020");
        return sizes.containsKey(args[0]) && sizes.get(args[0]) == args.length;
    }
}

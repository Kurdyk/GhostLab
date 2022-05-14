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
            "UPMOV", 2,
            "DOMOV", 2,
            "LEMOV", 2,
            "RIMOV", 2
    );

    public static boolean valiate(String command){
        String[] args = command.split(" ");
        return sizes.containsKey(args[0]) && sizes.get(args[0]) == args.length;
    }
}

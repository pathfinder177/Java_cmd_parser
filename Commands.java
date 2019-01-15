import java.io.*;
import java.util.*;

class FlagParser {
	private final Set<Character> foundFlags = new HashSet<Character>();

	// ls -la -R a b c -> {l, a, R}, 2
	public int parse(String[] args) {
		int flagIndex = 0;
		while (flagIndex < args.length) {
			String s = args[flagIndex];
			if(s.equals("-")) {
				break;
			}
			if(s.startsWith("-")) {
				char[] tmp = s.toCharArray();
				for(int i = 1; i < tmp.length; i++) {
					foundFlags.add(tmp[i]);
				}
			}
			else {
				break;
			}
			flagIndex++;
		}
		return flagIndex;	
	}

	public boolean containsFlag(char c) {
		return foundFlags.contains(c);
	}			
}

public class Commands {
	private static String getCD() {
		return System.getProperty("user.dir");
	}

	private static void ls(String[] args) {
		if (args.length == 0) {
			String cd = getCD();
			File cdF = new File(cd);
			String[] paths = cdF.list();
			for (String s : paths) {
				System.out.println(s);
			}
		} else {
			FlagParser fp = new FlagParser();
			fp.parse(args);
			if(fp.containsFlag('l')) {
				File cdF = new File(getCD());
				for(File path : cdF.listFiles()) {
					Date mtime = new Date(path.lastModified());
					String type = path.isDirectory() ? "d" : "-";
					System.out.printf("%s %s\t%s\n", type, mtime, path);	
				}
			}
		}
	}

	public static void main(String[] args) {
		if (args.length <= 0) {
			System.err.println("No enough aguments");
			System.exit(1);
		}
		String command = args[0];
		String[] cmdargs = Arrays.copyOfRange(args, 1, args.length);
		switch (command) {
		case "ls":
			ls(cmdargs);
			break;
		case "cat":
			break;
		default:
			System.err.println("Invalid command");
			System.exit(1);
		}
	}
}

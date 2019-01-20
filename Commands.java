import java.io.*;
import java.util.*;

class FlagParser {
	private final Set<Character> foundFlags = new HashSet<Character>();

	// ls -la -R a b c -> {l, a, R}, 2
	public int parse(String[] args) {
		int flagIndex = 0;
		while (flagIndex < args.length) {
			String s = args[flagIndex];
			if (s.equals("-")) {
				break;
			}
			if (s.startsWith("-")) {
				char[] tmp = s.toCharArray();
				for (int i = 1; i < tmp.length; i++) {
					foundFlags.add(tmp[i]);
				}
			} else {
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

	private static void printEntry(File path, FlagParser fp) {
		if (!path.exists()) {
			System.err.println(path.getName() + " doesn't exist");
			return;
		}
		if (fp.containsFlag('l')) {
			Date mtime = new Date(path.lastModified());
			String type = path.isDirectory() ? "d" : "-";
			System.out.printf("%s %s\t%s\n", type, mtime, path);
		} 
		else if(fp.containsFlag('R')) {

		}
		else {
			System.out.printf("%s\n", path.getName());
		}
	}

	private static void ls(FlagParser fp, String[] args) {
		if (args.length == 0) {
			args = new String[] { getCD() };
		}

		for (String path : args) {
			File fpath = new File(path);
			if (fpath.isDirectory() && !fp.containsFlag('d')) {
				for (File subentry : fpath.listFiles())
					printEntry(subentry, fp);
			} else {
				printEntry(fpath, fp);
			}
		}
	}

	// TODO tac
	private static void cat(FlagParser fp, String[] args) throws IOException {
		byte[] buffer = new byte[4096];	
		if(args.length > 0) {
			for(String s : args) {
				//found origin of error below
				//install git on win7
				FileInputStream fi = new FileInputStream(s);
				
				int bytesRead = 0;
				while((bytesRead = fi.read(buffer)) != -1) {
					System.out.write(buffer, 0, bytesRead);
				}

				fi.close();
			}
		}
		else {
			int bytesRead = 0;
			while((bytesRead = System.in.read(buffer)) != -1) {
				System.out.write(buffer, 0, bytesRead);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		
		if (args.length <= 0) {
			System.err.println("No enough aguments");
			System.exit(1);
		}
		String command = args[0];
		String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);

		FlagParser fp = new FlagParser();
		int idx = fp.parse(cmdArgs);

		String[] cmdNonflags = Arrays.copyOfRange(/*check on debugger*/cmdArgs, idx, cmdArgs.length);

		switch (command) {
		case "ls":
			ls(fp, cmdNonflags);
			break;
		case "cat":
			cat(fp, cmdNonflags);
			break;
		default:
			System.err.println("Invalid command");
			System.exit(1);
		}
	}
}

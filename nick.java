// nick.java
// 2013.01.19 Hiroki Tanioka taniokah@gmail.com

import java.io.*;
import java.util.*;

public class nick {
	private final static int datasize = 100000;	// max data sets
	
	public static void main(String[] args) {
		try {
			File f = new File(args[0]);
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader in = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(in);
			
			Map<String, Set<String>> dic = new TreeMap<String, Set<String>>();
			store(br, dic);	// store names into dictionary
			br.close();
			
			Set<String> groups = new TreeSet<String>();
			unique(dic, groups);
			output(dic, groups);
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	static void store(BufferedReader br, Map<String, Set<String>> dic) throws Exception {
		for (int i = 0; i < datasize; i++) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			if (line.length() <= 0) {
				continue;
			}
			//System.err.println(line);
			String[] names = line.split("=");
			List<String> namelist = Arrays.asList(names);
			Set<String> keyset = new TreeSet<String>(namelist);
			Set<String> groups = new TreeSet<String>();
			groups.addAll(keyset);	// set salvage point
			
			salvage(dic, keyset, groups);
			update(dic, groups);
		}
	}
	
	static void salvage(Map<String, Set<String>> dic, Set<String> keyset, Set<String> groups) {
		Iterator<String> it = keyset.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (key == null || "".equals(key)) {
				continue;
			}
			Set<String> group = dic.get(key);
			if (group == null) {
				continue;
			}
			groups.addAll(group);
		}
	}
	
	static void update(Map<String, Set<String>> dic, Set<String> groups) {
		Set<String> keyset = groups;
		Iterator<String> it = keyset.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (key == null || "".equals(key)) {
				continue;
			}
			Set<String> group = dic.get(key);
			if (group == null) {
				group = new TreeSet<String>();
			}
			group.addAll(groups);
			dic.put(key, group);	// update group to key
		}
	}
	
	static void unique(Map<String, Set<String>> dic, Set<String> groups) {
		// unique results
		Iterator<Set<String>> it = dic.values().iterator();
		while (it.hasNext()) {
			Set<String> group = it.next();
			if (group == null || group.size() <= 0) {
				continue;
			}
			StringBuilder names = new StringBuilder();
			Iterator<String> git = group.iterator();
			while (git.hasNext()) {
				String name = git.next();
				if (name == null || "".equals(name)) {
					continue;
				}
				if (names.length() > 0) {
					names.append("=");
				}
				names.append(name);
			}
			//System.err.println(names);
			groups.add(names.toString());
		}
	}
	
	static void output(Map<String, Set<String>> dic, Set<String> groups) {
		Iterator<String> it = groups.iterator();
		int ucount = 0;
		while (it.hasNext()) {
			String group = it.next();
			if (group == null || "".equals(group)) {
				continue;
			}
			System.out.println(group);
			ucount++;
		}
		//System.err.println("uniq count = " + ucount);
	}
}

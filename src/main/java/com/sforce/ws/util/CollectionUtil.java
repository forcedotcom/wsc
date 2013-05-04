package com.sforce.ws.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {

	public static <T> List<T> asList(Iterator<T> it) {
		List<T> list = new ArrayList<T>();
		while (it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
	
	public static <T extends Named> T findByName(Iterator<T> it, String name) {
		while (it.hasNext()) {
			T t = it.next();
			if (t.getName().equals(name))
				return t;
		}
		return null;
	}
	
	public static <T extends Named> T findByName(List<T> list, String name) {
		for (T t : list) {
			if (t.getName().equals(name))
				return t;
		}
		return null;
	}
	
}

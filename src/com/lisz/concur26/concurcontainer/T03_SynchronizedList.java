package com.lisz.concur26.concurcontainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class T03_SynchronizedList {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		List<String> syncList = Collections.synchronizedList(list);//加锁
	}

}


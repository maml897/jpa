package com.wish.jxzl.controller.report;

import java.util.Arrays;
import java.util.List;


public class Main
{
	public static void main(String[] args)
	{
		List<Integer> list =Arrays.asList(2,1,2,4,3);
		
		list.sort((x,y)->{
			return x-y;
		});
		
		System.out.println(list);
		
		
		List<String> list2 =Arrays.asList("a","b");
		list2.sort((x,y)->{
			return y.compareTo(x);
		});
		System.out.println(list2);
		
		int sum=list.stream().mapToInt(x->x).sum();
		System.out.println(sum);
	}
}

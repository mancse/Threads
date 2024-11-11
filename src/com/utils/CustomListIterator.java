package com.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CustomListIterator implements Iterator<String>
{
    List<String> list = null;
    int i =0;
    public CustomListIterator(List<String> list)
    {
    	this.list = list;
    }
	@Override
	public boolean hasNext() 
	{
		while(i < list.size())
		{
			if (list.get(i).startsWith("a"))
				return true;
			i++;
		}
		return false;
	}

	@Override
	public String next() 
	{
		String ret = null;
		if (hasNext())
			ret = list.get(i++);
		return ret;
	}

	@Override
	public void remove() {
	}
	
	public static void main(String[] args) 
	{
        List<String> list = Arrays.asList("alice", "bob", "abigail", "charlie");
        CustomListIterator itr = new CustomListIterator(list);
        
        while(itr.hasNext())
        {
        	System.out.print(" "+itr.next());
        }
	}

}

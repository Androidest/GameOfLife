package com.Androidest.GameOfLife;

public class Coords
{
	class node
	{
		int x;
		int y;
		node next;
		node(int x,int y){this.x=x;this.y=y;next=null;}
	}
	
	public node head;
	public node tail;
	public node i;
	
	Coords()
	{
		head=new node(-1,-1);
		tail=head;
		i=head;
	}
	
	public void add(int x,int y)
	{
		if(tail.next!=null)
		{
			tail=tail.next;
			tail.x=x;
			tail.y=y;
			return;
		}
		tail.next=new node(x,y);
		tail=tail.next;
	}
	
	public void remove(int x,int y)
	{
		i=head;
		while(!i.next.equals(tail))
		{
			if(i.next.x==x&&i.next.y==y)
			{
				i.next=i.next.next;
				return;
			}i=i.next;
		}
		tail=i; 
		i.next=null;
	}
	
	public void reset() {i=head;}
	
	public void clear()
	{
		i=head;
		tail=head;
	}
}


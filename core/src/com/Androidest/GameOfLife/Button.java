package com.Androidest.GameOfLife;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.*;

public class Button
{
	protected float x1; 
	protected float y1;
	protected float x2;
	protected float y2;
	protected float width;
	protected float height;
	protected Texture texture0;
	protected Texture texture1;
	public    Texture texture;
	public    float frameSpeed;
	public static SpriteBatch batch;

	Button(float x,float y,float width,float height,String pic0,String pic1)
	{
		x1 = x; y1 = y;
		x2 = x+width;
		y2 = y+height;
		this.width  = width;
		this.height = height;
		texture0 = new Texture(Gdx.files.internal(pic0));
		texture1 = new Texture(Gdx.files.internal(pic1));
		texture  = texture0;
	}

	boolean isIn(int x,int y)
	{
		if(x1<x && x<x2 && y1<y && y<y2)
			return true;
		return false;
	}
	
	void draw()
	{
		batch.draw(texture,x1,y1,width,height);
	}
	
	public static void setBatch(SpriteBatch batch1)
	{
		batch=batch1;
	}

}

class dButton extends Button
{
	private float initialX;
	private float maxX;
	
	
	dButton(float x,float y,float width,float height,String pic0,String pic1)
	{
		super(x,y,width,height,pic0,pic1);
		initialX = x1;
		frameSpeed=0;
		maxX = Gdx.graphics.getWidth()*1.01f;
	}
	
	void draw()
	{
		if(frameSpeed!=0)
		{
			x1+=frameSpeed;
			if(x1<initialX || x1>maxX)
			{
				x1-=frameSpeed;
				frameSpeed=0;
			}
		}
		batch.draw(texture,x1,y1,width,height);
	}
}


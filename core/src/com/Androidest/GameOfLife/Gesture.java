package com.Androidest.GameOfLife;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;

public class Gesture 
{
	public static OrthographicCamera camera;
	private float screenRatio;
	private float moveRatio1;
	private float moveRatio;
	private float oldzoom;
	private boolean isTap;
	private int   a;
	private int   b; 
	private float c;
	public  int   touchedX; 
	public  int   touchedY;
	public boolean panEnable;
	
	Gesture(OrthographicCamera camera)
	{
		this.camera = camera;
		screenRatio = Gdx.graphics.getHeight()/Gdx.graphics.getWidth();
		moveRatio1  = 300f/Gdx.graphics.getWidth();
		moveRatio   = moveRatio1;
		panEnable   = true;
		isTap=false;
		
	}
	
	public void control()
	{
		if(!Gdx.input.isTouched(0)||!Gdx.input.isTouched(1))
		{
			oldzoom=camera.zoom;
			if(!Gdx.input.isTouched()) 
				return;
			if (Gdx.input.isTouched(0)&&!Gdx.input.isTouched(1))
			{
				if(Gdx.input.justTouched())
					isTap=true;
				if(Math.abs(Gdx.input.getDeltaY(0))>2 && Math.abs(Gdx.input.getDeltaX(0))>2)
					isTap=false;
				if(panEnable) pan();
				return;
			}
		}
		if(Gdx.input.isTouched(0)&&Gdx.input.isTouched(1)&&!Gdx.input.isTouched(2))
		{
			isTap=false;
			zoom();
		}
	}
	
	public boolean getTap()
	{	
		if(!Gdx.input.isTouched()&&isTap) 
		{
			positionDetector();
			isTap=false;
			return true;
		}
		return false;
	}
	
	public void positionDetector()
	{
		touchedX=(int)(((Gdx.input.getX(0)-Gdx.graphics.getWidth()/2)
			*moveRatio+camera.position.x)/6);
		touchedY=(int)(((Gdx.graphics.getHeight()/2-Gdx.input.getY(0))
			*moveRatio+camera.position.y)/6);
	}

	public void zoom()
	{
		if(Gdx.input.justTouched())
		{
			a=Gdx.input.getX(0)-Gdx.input.getX(1);
			b=Gdx.input.getY(0)-Gdx.input.getY(1);
			oldzoom*=(float) Math.sqrt(a*a+b*b);
			return;
		}
		if((Gdx.input.getDeltaY(0)+Gdx.input.getDeltaX(0)+
		+Gdx.input.getDeltaY(1)+Gdx.input.getDeltaX(1))==0)
		     return;
			 
		a=Gdx.input.getX(0)-Gdx.input.getX(1);
		b=Gdx.input.getY(0)-Gdx.input.getY(1);
		c=oldzoom/(float) (Math.sqrt(a*a+b*b));
		
		if(c<0.4||8<c) return;
		moveRatio=c*moveRatio1;
		camera.zoom=c;
	}
	
	public void pan()
	{
		camera.position.x-=moveRatio*Gdx.input.getDeltaX(0);
		camera.position.y+=moveRatio*Gdx.input.getDeltaY(0);
	}
	
}


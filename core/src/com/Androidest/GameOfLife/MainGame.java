package com.Androidest.GameOfLife;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;


public class MainGame
{
	private byte[][]  world;
	private Coords    tmp;
	private Coords    nextlifes;
	private Coords    probability;
	private Gesture   gesture;
	private int       function;
	private int       longPress;
	private int 	  drawState;  
	private float     frameTime;
	private float 	  maxFrameTime;
	private float     speed;
	private Sound     setSound;
	private Sound     buttonSound;
	
	public  Coords    lifes;
	public  Button[]  buttons;
	int x,y;    float w,h,sumy;
	 
	MainGame(OrthographicCamera camera,SpriteBatch batch)
	{
		function    = 1;
		frameTime   = 0;
		longPress   = -1;
		maxFrameTime= 0.16f;
		speed       = Gdx.graphics.getWidth() * 0.005f;
		probability = new Coords();
		nextlifes   = new Coords();
		lifes       = new Coords();
		tmp         = new Coords();
		gesture     = new Gesture(camera);
		buttons     = new Button[5];
		setSound    = Gdx.audio.newSound(Gdx.files.internal("setSound.WAV"));
		buttonSound    = Gdx.audio.newSound(Gdx.files.internal("button.wav"));
		Button.setBatch(batch);
		
		x=Gdx.graphics.getWidth();
		y=Gdx.graphics.getHeight(); 
		buttons[4]  = new dButton(x-x*0.01f-y*0.14f,y*0.02f, y*0.14f, y*0.14f, "lock0.png","lock1.png");
		buttons[3]  = new Button(x*0.018f, sumy =y*0.04f, x*0.056f, y*0.37f, "low0.png",   "low1.png"); 
		buttons[2]  = new Button(x*0.018f, sumy+=y*0.39f, x*0.056f, y*0.37f, "fast0.png",  "fast1.png");
		buttons[1]  = new Button(x*0.01f,  sumy+=y*0.42f, y*0.14f,  y*0.14f, "reset0.png", "reset1.png");
		buttons[0]  = new Button(x-x*0.01f-y*0.14f, sumy, y*0.14f,  y*0.14f, "play.png",  "pause.png"); 
		
		world = new byte[2000][2000];
		for(int i=0;i<2000;++i)
			for(int j=0;j<2000;++j)
				world[i][j]=0;
	}

	void runGame()
	{
		gesture.control();
		if(gesture.getTap())
			switch(checkButtons())
			{
				case -1 : if(function == 1) setLife(); break;
				case  0 : if(function == 0) {function=1; buttons[0].texture=buttons[0].texture0; buttons[4].frameSpeed=-speed; break;}
					      if(function == 1) {function=0; buttons[0].texture=buttons[0].texture1; buttons[4].frameSpeed=speed;  break;}
					 gesture.panEnable=true; function=0; buttons[0].texture=buttons[0].texture1; buttons[4].frameSpeed=speed; buttons[4].texture=buttons[4].texture0; break;
				case  1 : if(function == 0) {function=1; buttons[0].texture=buttons[0].texture0; buttons[4].frameSpeed=-speed;} resetGame();  break;
				case  2 : if(maxFrameTime > 0) maxFrameTime-=0.01f; break;
				case  3 : if(maxFrameTime < 1) maxFrameTime+=0.01f; break;
				case  4 : if(function == 1)  {function=2; gesture.panEnable=false; buttons[4].texture=buttons[4].texture1; break;}
						                      function=1; gesture.panEnable=true;  buttons[4].texture=buttons[4].texture0; break;
			}
	
		if( Gdx.input.isTouched(0) && !Gdx.input.isTouched(1) )
		{
			if(Gdx.input.justTouched())
			{
				longPress = checkButtons();
				if(longPress == -1 && function == 2)
					{ gesture.positionDetector();
					  drawState = world[gesture.touchedX][gesture.touchedY]; }
				else if(longPress != -1)
				{
					drawState = -1;
					if(longPress !=0 && longPress!=4)
						buttons[longPress].texture=buttons[longPress].texture1; 
					if(longPress==1)
						Gdx.input.vibrate(200);
					else Gdx.input.vibrate(23);
					buttonSound.play(0.6f);
				}
			}
			if(longPress==2 && 0<maxFrameTime) maxFrameTime-=0.005;
			if(longPress==3 && maxFrameTime<1) maxFrameTime+=0.005;
		}
		else if(!Gdx.input.isTouched(0) && longPress > 0 && longPress != 4)
			buttons[longPress].texture=buttons[longPress].texture0;
			
		switch(function)
		{
			case  0 : startLife(); return;
			case  1 : return; 
			case  2 : drawLifes();
		}
	}
	
	void drawLifes()
	{
		if( Gdx.input.isTouched(0) && !Gdx.input.isTouched(1) && drawState != -1)
		{
			gesture.positionDetector();
			x=gesture.touchedX;
			y=gesture.touchedY;
			if(drawState == 0)
			   {if( world[x][y] == 0 )
				{
					setSound.play(0.03f);
					lifes.add(x, y);
					world[x][y] = 10;
					return;
				}}
			else if( world[x][y] == 10 )
			{
				setSound.play(0.03f);
				lifes.remove(x, y);
				world[x][y] = 0;
			}				
		}
	}
	
	int checkButtons()
	{
		for(int i=0; i<buttons.length; ++i)
		{
			if(buttons[i].isIn(Gdx.input.getX(0),
			Gdx.graphics.getHeight()-Gdx.input.getY(0)))
				return i;
		}return -1;
	}

	void setLife()
	{
		x=gesture.touchedX;
		y=gesture.touchedY;
		setSound.play(0.03f);
		if( world[x][y] == 0 )
		{
			lifes.add(x, y);
			world[x][y] = 10;
			return;
		}
		lifes.remove(x, y);
		world[x][y] = 0;
	}
	
	void startLife()
	{
		frameTime+=Gdx.graphics.getDeltaTime();
		if(frameTime<maxFrameTime) return; frameTime=0;
		
		lifes.reset();
		while(!lifes.i.equals(lifes.tail))
		{	
			lifes.i=lifes.i.next;
			x = lifes.i.x;
			y = lifes.i.y;
			
			if(world[--x][--y]==0) probability.add(x,y); ++world[x][y];			
			if(world[  x][++y]==0) probability.add(x,y); ++world[x][y];			
			if(world[  x][++y]==0) probability.add(x,y); ++world[x][y];			
			if(world[++x][  y]==0) probability.add(x,y); ++world[x][y];	
			if(world[++x][  y]==0) probability.add(x,y); ++world[x][y];			
			if(world[  x][--y]==0) probability.add(x,y); ++world[x][y];			
			if(world[  x][--y]==0) probability.add(x,y); ++world[x][y];			
			if(world[--x][  y]==0) probability.add(x,y); ++world[x][y];
		}
		lifes.reset();
		while(!lifes.i.equals(lifes.tail))
		{
			lifes.i=lifes.i.next;
			if( world[lifes.i.x][lifes.i.y] == 12 || world[lifes.i.x][lifes.i.y] == 13 ) 
			{	
				if(1<lifes.i.x && lifes.i.x<1999 && 1<lifes.i.y && lifes.i.y<1999 )
				{
					nextlifes.add(lifes.i.x,lifes.i.y); 
					world[lifes.i.x][lifes.i.y] = 10;
				}
				else world[lifes.i.x][lifes.i.y] = 0;
			}
			else world[lifes.i.x][lifes.i.y] = 0;
		}
		
		probability.reset();
		while(!probability.i.equals(probability.tail))
		{
			probability.i=probability.i.next;
			if( world[probability.i.x][probability.i.y] == 3 ) 
			{
				if(1<probability.i.x && probability.i.x<1999 
				&& 1<probability.i.y && probability.i.y<1999 )
				{
					nextlifes.add(probability.i.x,probability.i.y); 
					world[probability.i.x][probability.i.y] = 10;
				}
				else world[probability.i.x][probability.i.y] = 0;
			}
			else world[probability.i.x][probability.i.y] = 0;	
		}
		tmp=lifes;
		lifes=nextlifes;
		nextlifes=tmp;
		nextlifes.clear();
		probability.clear();
	}
	
	void resetGame()
	{
		lifes.reset();
		while(!lifes.i.equals(lifes.tail))
		{
			lifes.i=lifes.i.next;	
			world[lifes.i.x][lifes.i.y] = 0;
		}lifes.clear();
		gesture.camera.position.x=1000*6;
		gesture.camera.position.y=1000*6;
	}
}



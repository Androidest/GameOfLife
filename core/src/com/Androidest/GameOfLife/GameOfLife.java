package com.Androidest.GameOfLife;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;


public class GameOfLife implements ApplicationListener
{
	SpriteBatch   batch;
	ShapeRenderer shapeRenderer;
	public static OrthographicCamera camera;	
	MainGame  mainGame;
	Button[]  buttons;

	@Override
	public void create()
	{        
		camera        = new  OrthographicCamera();
		batch         = new  SpriteBatch();
		mainGame      = new  MainGame(camera,batch);
		shapeRenderer = new  ShapeRenderer();
		shapeRenderer.setColor(0, 1f, 0, 1);  //green
		buttons = mainGame.buttons;
	}

	@Override
	public void render()
	{       
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(camera.combined);
		mainGame.lifes.reset();
		while(!mainGame.lifes.i.equals(mainGame.lifes.tail))
			{
				mainGame.lifes.i=mainGame.lifes.i.next;
				shapeRenderer.rect(mainGame.lifes.i.x*6,mainGame.lifes.i.y*6,5,5);
			}
		shapeRenderer.end();
		
		batch.begin();
			for(int i=0; i<buttons.length; ++i)	
			{
				buttons[i].draw();
			}
		batch.end();
		
		mainGame.runGame();
	}

	@Override
	public void resize(int width, int height)
	{
		if (width < height)
			camera.setToOrtho(false, 300 * width/height, 300);
		else
			camera.setToOrtho(false, 300, 300 * height/width);
		camera.position.x=1000*6;
		camera.position.y=1000*6;
	}
	
	@Override
	public void dispose()
	{
		shapeRenderer.dispose();
		batch.dispose();
	}
	
	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
}

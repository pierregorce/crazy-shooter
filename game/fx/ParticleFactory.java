package game.fx;

import game.entitiy.Character;
import game.entitiy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleFactory extends Actor
{
	private ParticleEffect particleEffect;

	public ParticleFactory(Character entities)
	{
		super();
		
		particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal("images/particle.p"), Gdx.files.internal("images"));
		particleEffect.setPosition(entities.getX(), entities.getY());
		particleEffect.start();
		//GameStage.shake=true;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		particleEffect.draw(batch);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		particleEffect.update(delta);
	}
	
	
	//TODO ADD GRAVITY
	
	
	
	
}

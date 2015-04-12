package screen.loading;

import java.util.Random;

import screen.MyGdxGame;
import screen.loading.MovableObject.ResetMethod;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.oasix.crazyshooter.Timer;

public class LoadingGroup extends Group
{
	private MovableObject	layer_front;
	private MovableObject	layer_building_1;
	private MovableObject	layer_building_2;
	private MovableObject	layer_building_3;
	private MovableObject	layer_building_4;
	private MovableObject	layer_building_5;
	private MovableObject	layer_building_6;
	private MovableObject	layer_background;
	private MovableObject	layer_moon;
	private MovableObject[]	layer_clouds	= new MovableObject[15];
	private Animation		logo_animation;

	private float			cloudSpeedMax	= 3f;
	private float			cloudSpeedMin	= 0.5f;

	public LoadingGroup()
	{
		layer_background = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-background.png")), ResetMethod.RESET);
		layer_moon = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-moon.png")), ResetMethod.RESET);
		layer_moon.setX(-75);

		layer_building_6 = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-layer-5.png")), ResetMethod.LOOP);
		layer_building_5 = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-layer-4.png")), ResetMethod.LOOP);
		layer_building_4 = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-layer-3.png")), ResetMethod.LOOP);
		layer_building_3 = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-layer-2.png")), ResetMethod.LOOP);
		layer_building_2 = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-layer-1.png")), ResetMethod.LOOP);
		layer_building_1 = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-layer-0.png")), ResetMethod.LOOP);
		layer_front = new MovableObject(this, new Texture(Gdx.files.internal("images/loading/loading-front.png")), ResetMethod.RESET);

		Texture[] cloudTexture = new Texture[5];

		for (int i = 0; i < cloudTexture.length; i++)
		{
			cloudTexture[i] = new Texture(Gdx.files.internal("images/loading/loading-nuage-" + i + ".png"));
		}

		for (int i = 0; i < layer_clouds.length; i++)
		{
			layer_clouds[i] = new MovableObject(this, cloudTexture[new Random().nextInt(cloudTexture.length)], ResetMethod.RESET);
			layer_clouds[i].putMoveSpeed(new Random().nextFloat() * (cloudSpeedMax - cloudSpeedMin) + cloudSpeedMin);
			layer_clouds[i].setX(new Random().nextInt(MyGdxGame.VIRTUAL_WIDTH));
			layer_clouds[i].setY(new Random().nextInt(500) + 500);
		}

		TextureRegion[] logo_texture = new TextureRegion[12];

		for (int i = 0; i < logo_texture.length; i++)
		{
			logo_texture[i] = new TextureRegion(new Texture(Gdx.files.internal("images/loading/logo/logo (" + (i + 1) + ").png")));
		}

		logo_animation = new Animation(0.05f, new Array<TextureRegion>(logo_texture));
		logo_animation.setPlayMode(PlayMode.LOOP_REVERSED);

		//
		// layer_front.setPosition(layer_front.getX(), 10);
		// layer_building_1.setPosition(layer_building_1.getX(), 10);
		// layer_building_2.setPosition(, 10);
		// layer_building_3.setPosition(10, 10);
		// layer_building_4.setPosition(10, 10);
		// layer_building_5.setPosition(10, 10);
		// layer_background.setPosition(10, 10);
		// layer_moon.setPosition(10, 10);
		//
		// for (Image image : layer_clouds)
		// {
		// layer_clouds.setPosition(10, 10);
		// }
		logoBright.resetForLunchImmediatly();
	}

	private Timer	logoBright	= new Timer(1f);

	@Override
	public void act(float delta)
	{
		super.act(delta);
		layer_moon.setY(290);

		layer_front.putMoveSpeed(0);
		float way = -1;
		layer_building_1.putMoveSpeed(0f * way);
		layer_building_2.putMoveSpeed(2.5f * way);
		layer_building_3.putMoveSpeed(2f * way);
		layer_building_4.putMoveSpeed(1.3f * way);
		layer_building_5.putMoveSpeed(1f * way);
		layer_building_6.putMoveSpeed(0.7f * way);
		layer_background.putMoveSpeed(0);
		layer_moon.putMoveSpeed(1.2f);
	}

	float	time	= 0;

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		TextureRegion t = logo_animation.getKeyFrame(time);
		batch.draw(t, 450, 580, t.getRegionWidth() * 4, t.getRegionHeight() * 4);
		time += Gdx.graphics.getDeltaTime();
	}
}

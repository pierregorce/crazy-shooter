package screen.upgrade;

import java.util.Random;

import screen.MyGdxGame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class UpgradeStage extends Stage
{

	private UpgradeGroup	upgradeGroup;

	private Vector3			cameraPosition;
	// private float cameraSmooth = 0.07f;
	private static boolean	shake			= false;			// Permet de faire un shake de l'écran
	private static float	shakeRadius		= 25;
	private int				randomAngle;
	private Vector2			offsetVector	= new Vector2();

	public UpgradeStage()
	{

		setViewport(new ExtendViewport(MyGdxGame.VIRTUAL_WIDTH, MyGdxGame.VIRTUAL_HEIGHT));
		upgradeGroup = new UpgradeGroup();
		addActor(upgradeGroup);
		cameraPosition = getCamera().position;

	}

	public static void cameraShake(int value)
	{
		shake = true;
		shakeRadius = value;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (!shake)
		{
			cameraPosition.x = MyGdxGame.VIRTUAL_WIDTH / 2;
			cameraPosition.y = (MyGdxGame.VIRTUAL_HEIGHT / 2);
		}

		// Controle le shake ----------------------------------------------------------------------------------------------------------
		if (shake) // si shake
		{
			randomAngle += (180 + new Random().nextInt(180) - 60); // pick new angle
			offsetVector.set((float) (Math.sin(randomAngle) * shakeRadius), (float) (Math.cos(randomAngle) * shakeRadius)); // create offset 2d vector
			cameraPosition.x += offsetVector.x;
			cameraPosition.y += offsetVector.y;
			shakeRadius *= 0.9f; // diminish radius each frame
		}

		if (shakeRadius < 5)
		{
			shake = false;
		}
	}

}

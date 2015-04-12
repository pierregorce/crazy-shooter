package screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * 
 * Classe a invoqué dans le screen manager lors d'une demande de changement de screen. Le screen suivant vient de la droite et le screen courant part vers la gauche.
 * 
 * @author Pierre
 *
 */
public class TransitionScreen implements Screen
{
	private MovableScreen m_current;

	public TransitionScreen(final Game game, MovableScreen current, final MovableScreen next)
	{
		System.out.println("Initialisation du transition screen");
		// TODO PLACE HERE SOUND TRANSITION.
		m_current = current;

		// ---------------------------- Definition de l'action pour le screen courant (part vers la gauche)

		current.getStage().getRoot().addAction(Actions.sequence(Actions.alpha(1), Actions.fadeOut(0.5f), Actions.run(new Runnable()
		{
			@Override
			public void run()
			{
				// ---------------------------- Definition de l'action pour le screen suivant (provient de la droite)
				System.out.println("next");
				game.setScreen(next);
				next.getStage().getRoot().addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
				// Actions.moveBy(0, -500);
				// Actions.moveBy(0, 500, 0.5f);

			}
		})));

	}

	@Override
	public void render(float delta)
	{
		m_current.render(delta);
	}

	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void show()
	{

	}

	@Override
	public void hide()
	{

	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void dispose()
	{

	}
}

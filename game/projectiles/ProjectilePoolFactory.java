package game.projectiles;

import com.badlogic.gdx.utils.Pool;

public class ProjectilePoolFactory
{
	private static ProjectilePoolFactory	projectilePoolFactory;

	public Pool<Projectile>					projectilePool	= new Pool<Projectile>()
															{
																@Override
																protected Projectile newObject()
																{
																	return new Projectile(null);
																}
															};

	public static ProjectilePoolFactory getInstance()
	{
		if (projectilePoolFactory == null)
		{
			projectilePoolFactory = new ProjectilePoolFactory();
		}
		return projectilePoolFactory;
	}

	public static void reCreatePool()
	{
		projectilePoolFactory = new ProjectilePoolFactory();
	}
}

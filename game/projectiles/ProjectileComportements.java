package game.projectiles;

import game.entitiy.Character;
import game.entitiy.Enemies;
import game.entitiy.PhysicalEntity;
import game.fx.ExplosionParticle;
import game.fx.ParticleColors;
import globals.Projectiles;

import java.util.Random;

import ressources.DrawableAnimation;
import ressources.R;
import ressources.Ressource;
import ressources.S;
import ressources.S.TyrianSound;
import utilities.enumerations.Direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Pools;
import com.oasix.crazyshooter.GameStage;
import com.oasix.crazyshooter.GlobalController;
import com.oasix.crazyshooter.Player;
import com.oasix.crazyshooter.Timer;

public class ProjectileComportements
{
	public class Straight extends ProjectileComportementFrame
	{
		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			GenericComportementList.goStraight_Initialisation(projectile, characterSender);
		}

		@Override
		protected void comportement_act(Projectile projectile)
		{
			GenericComportementList.goStraight_Act(projectile);
		}
	}

	public class Straight_Inverted extends Straight
	{
		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			super.comportement_init(projectile, characterSender);
			// Lance un projectile normal
			Projectile projectile_bis = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
			projectile_bis.construct(Projectiles.PLAYER_BASIC_GUN);

			projectile_bis.init(characterSender);
			projectile_bis.direction = projectile_bis.getOpositeDirection();
			GlobalController.bulletControllerFriendly.addActor(projectile_bis);
		}
	}

	public class Straight_Piercing extends Straight
	{
		@Override
		protected void comportement_act(final Projectile projectile)
		{
			super.comportement_act(projectile);

			Action action = null;

			action = Actions.run(new Runnable()
			{
				@Override
				public void run()
				{
					if (!projectile.active)
					{
						S.c().play(TyrianSound.soundEffect_weapons_railGun, GlobalController.player, projectile);
					}
				}
			});

			GenericComportementList.setActivation(projectile, action);

		}
	}

	public class Flame extends ProjectileComportementFrame
	{
		// private int yLongOffset = new Random().nextInt(4);

		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			projectile.setSize(projectile.getWidth() / 1.8f, projectile.getHeight() / 1.8f);
			GenericComportementList.goStraight_Initialisation(projectile, characterSender);
			projectile.disablePhysics();
			final float TIME_ALIVE = 0.30f;
			final float MAX_SIZE = projectile.getWidth() * 1.4f;
			projectile.addAction(Actions.alpha(1));
			projectile.addAction(Actions.alpha(0.7f, TIME_ALIVE));
			projectile.addAction(Actions.sizeBy(MAX_SIZE, MAX_SIZE, TIME_ALIVE));
			projectile.addAction(Actions.delay(TIME_ALIVE, Actions.removeActor()));
		}

		@Override
		protected void comportement_act(Projectile projectile)
		{
			GenericComportementList.goStraight_Act(projectile);
			// Ajoute la rotation
			projectile.rotation += 90;
		}
	}

	public class Target_Player_Action extends ProjectileComportementFrame
	{
		@Override
		protected void comportement_init(final Projectile projectile, Character characterSender)
		{
			projectile.disablePhysics();
			Player player = ((Enemies) characterSender).getPlayer();
			Action move = Actions.moveTo(player.getCenterX(), player.getCenterY(), 0.5f);
			projectile.addAction(new SequenceAction(move, new RunnableAction()
			{
				@Override
				public void run()
				{
					projectile.remove();
				}
			}));
		}
	}

	public class Straight_Bazooka extends Straight
	{
		@Override
		protected void comportement_endingEffect(Projectile projectile, Character characterReceiving)
		{
			S.c().play(TyrianSound.soundEffect_weapons_bazookaExplosion, characterReceiving, projectile);
			int quantity = Projectiles.PLAYER_BAZOOKA_EXPLOSION.quantityPerShoot;

			for (int i = 0; i < quantity / 2; i++)
			{
				int xRandomization = new Random().nextInt(100) - 50;
				int yRandomization = new Random().nextInt(20);

				Projectile projectileExplosion = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
				projectileExplosion.construct(Projectiles.PLAYER_BAZOOKA_EXPLOSION);
				projectileExplosion.init(projectile.direction, new Vector2(projectile.getX() + xRandomization, projectile.getY() + yRandomization));
				projectileExplosion.setX(projectileExplosion.getX() + xRandomization);
				projectileExplosion.setY(projectileExplosion.getY() + yRandomization);
				GlobalController.bulletControllerFriendly.addActor(projectileExplosion);
			}

			for (int i = 0; i < quantity / 2; i++)
			{
				int xRandomization = new Random().nextInt(200) - 100;
				int yRandomization = new Random().nextInt(20);

				Projectile projectileExplosion = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
				projectileExplosion.construct(Projectiles.PLAYER_BAZOOKA_EXPLOSION);
				projectileExplosion.init(projectile.direction, new Vector2(projectile.getX() + xRandomization, projectile.getY() + yRandomization));
				// projectileExplosion.init(characterReceiving);
				projectileExplosion.setX(projectileExplosion.getX() + xRandomization);
				projectileExplosion.setY(projectileExplosion.getY() + yRandomization);
				GlobalController.bulletControllerFriendly.addActor(projectileExplosion);
			}
		}
	}

	public class Fall_Meteore extends Physic
	{

		public Fall_Meteore(int xSpeed, int xModulation, int ySpeed, int yModulation)
		{
			super(xSpeed, xModulation, ySpeed, yModulation);
		}

		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			projectile.enablePhysics();
			projectile.setJump(false); // TODO TEST
		}

		@Override
		protected void comportement_endingEffect(Projectile projectile, Character characterReceiving)
		{
			GameStage.cameraShake(GameStage.SMALL_SHAKE);
			S.c().play(TyrianSound.soundEffect_meteorLand, characterReceiving, projectile);

			// Only effect, le degat est déja dans la bomb...
			for (int i = 0; i < 15; i++)
			{
				for (int j = 0; j < 4; j++)
				{
					int xRandomization = new Random().nextInt(100) - 50;
					int yRandomization = new Random().nextInt(30);
					int height = 60;
					Ressource r = new Ressource(new DrawableAnimation(0.16f, R.c().fx_explode_circle), projectile.getCenterX() - height / 2 + xRandomization, projectile.getCenterY() - height / 2 + yRandomization,
							height, true);
					r.setColor(ParticleColors.getInstance().getGreenWaste()[new Random().nextInt(ParticleColors.getInstance().getGreenWaste().length)]);
					GlobalController.fxController.addActor(r);
				}

				// Add 2 particles by explosion
				for (int j = 0; j < 2; j++)
				{
					ExplosionParticle explosionParticle = new ExplosionParticle();
					explosionParticle.init(projectile.getCenterX(), projectile.getCenterY(), ParticleColors.getInstance().getGreenWaste());
					GlobalController.particleController.addActor(explosionParticle);
				}
			}
			projectile.remove();
		}

	}

	public class Physic extends ProjectileComportementFrame
	{
		private int				ySpeed;
		private int				xSpeed;
		private int				xSpeedMax;
		private int				xSpeedMin;
		private int				ySpeedMax;
		private int				ySpeedMin;
		private int				rebound;
		private TextureRegion[]	finalAnimation	= null;

		public Physic(int xSpeed, int xModulation, int ySpeed, int yModulation)
		{
			super();
			this.ySpeed = ySpeed;
			this.xSpeed = xSpeed;

			xSpeedMax = xSpeed + xModulation;
			xSpeedMin = xSpeed - xModulation;
			ySpeedMax = ySpeed + yModulation;
			ySpeedMin = ySpeed - yModulation;
		}

		public Physic(int xSpeed, int xModulation, int ySpeed, int yModulation, int rebound)
		{
			this(xSpeed, xModulation, ySpeed, yModulation);
			this.rebound = rebound;
		}

		public Physic(int xSpeed, int xModulation, int ySpeed, int yModulation, int rebound, TextureRegion[] finalAnimation)
		{
			this(xSpeed, xModulation, ySpeed, yModulation, rebound);
			this.finalAnimation = finalAnimation;
		}

		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			projectile.enablePhysics();

			this.xSpeed = new Random().nextInt(xSpeedMax - xSpeedMin) + xSpeedMin;
			this.ySpeed = new Random().nextInt(ySpeedMax - ySpeedMin) + ySpeedMin;

			projectile.maxJumpSpeedY = ySpeed;
			projectile.maxVelocityX = xSpeed;

			projectile.setJump(true);
			projectile.setWalk(true);

			projectile.rebound = rebound;
		}

		@Override
		protected void comportement_act(Projectile projectile)
		{
			super.comportement_act(projectile);

			if (projectile.getCollisionBlock() != null)
			{
				if (projectile.rebound > 0)
				{
					projectile.setJump(true);
					projectile.maxJumpSpeedY *= 0.9;
					projectile.maxVelocityX *= 0.65;
					projectile.rebound--;
				} else
				{
					if (projectile.getVelocity().y <= 0)
					{
						projectile.setWalk(false);
						projectile.doEnddingEffect(null);
					}
				}
			}
		}

		@Override
		protected void comportement_endingEffect(Projectile projectile, Character characterReceiving)
		{
			super.comportement_endingEffect(projectile, characterReceiving);

			if (finalAnimation != null)
			{
				int height = 50;
				Ressource r = new Ressource(new DrawableAnimation(0.12f, finalAnimation), projectile.getCenterX() - height / 2, projectile.getCenterY() - height / 2, height, true);
				r.setColor(1, 1, 1, 0.3f);
				GlobalController.fxController.addActor(r);
			}

			projectile.remove();
		}

	}

	public class PhysicMud extends Physic
	{
		public PhysicMud(int xSpeed, int xModulation, int ySpeed, int yModulation, int rebound)
		{
			super(xSpeed, xModulation, ySpeed, yModulation, rebound);
		}

		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			super.comportement_init(projectile, characterSender);
			projectile.direction = Direction.values()[new Random().nextInt(2)];
			projectile.setColor(ParticleColors.getInstance().getVioletWaste()[new Random().nextInt(ParticleColors.getInstance().getVioletWaste().length)]);
			projectile.setSize(projectile.getWidth() * 1.5f, projectile.getHeight() * 1.5f);
		}

		@Override
		protected void comportement_act(Projectile projectile)
		{
			super.comportement_act(projectile);
			GenericComportementList.setActivation(projectile, null);
		}

		@Override
		protected void comportement_endingEffect(Projectile projectile, Character characterReceiving)
		{
			// super.comportement_endingEffect(projectile, characterReceiving);
		}
	}

	public class Physic_Grenade extends Physic
	{
		private Timer	timer	= new Timer(0.10f);

		public Physic_Grenade(int xSpeed, int xModulation, int ySpeed, int yModulation)
		{
			super(xSpeed, xModulation, ySpeed, yModulation);
		}

		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			super.comportement_init(projectile, characterSender);
			timer.resetForLunchImmediatly();
			projectile.rebound = new Random().nextInt(2) + 1; // 1 ou 2 rebonds
		}

		@Override
		protected void comportement_act(final Projectile projectile)
		{
			// super.comportement_act(projectile); Pas de super, le comportement est un peu particulier

			if (projectile.getCollisionBlock() != null)
			{
				if (projectile.rebound > 0)
				{
					projectile.setJump(true);
					projectile.maxJumpSpeedY *= 0.9;
					projectile.maxVelocityX *= 0.65;
					projectile.rebound--;
				} else
				{
					if (projectile.getVelocity().y <= 0 && projectile.getActions().size == 0)
					{
						projectile.setWalk(false);
						DelayAction delayAction = Actions.delay(0.75f);
						RunnableAction runnableAction = new RunnableAction()
						{
							@Override
							public void run()
							{
								GameStage.cameraShake(GameStage.SMALL_SHAKE);
								projectile.doEnddingEffect(null); // Il n'y a pas de character recevant l'event
							}
						};
						projectile.addAction(new SequenceAction(delayAction, runnableAction));
					}
				}
			} else
			{
				// do smoke
				if (timer.doAction(Gdx.graphics.getDeltaTime()))
				{
					int height = 50;
					Ressource r = new Ressource(new DrawableAnimation(0.12f, R.c().fx_pop), projectile.getCenterX() - height / 2, projectile.getCenterY() - height / 2, height, true);
					r.setColor(1, 1, 1, 0.5f);
					GlobalController.fxController.addActor(r);
				}
			}
		}

		@Override
		protected void comportement_endingEffect(Projectile projectile, Character characterReceiving)
		{
			// super.comportement_endingEffect(projectile, characterReceiving);

			// Add White Explosion On Grenade
			S.c().play(TyrianSound.soundEffect_weapons_grenadeLauncherGunExplode, GlobalController.player, projectile);
			int height = 70;
			Ressource r = new Ressource(new DrawableAnimation(0.14f, R.c().fx_explode_circle), projectile.getCenterX() - height / 2, projectile.getCenterY() - height / 2, height, true);
			r.setColor(1, 1, 1, 1f);
			GlobalController.fxController.addActor(r);

			for (int i = 0; i < Projectiles.PLAYER_GRENADE_EXPLOSION.quantityPerShoot; i++)
			{
				int xRandomization = new Random().nextInt(160) - 80;
				int yRandomization = new Random().nextInt(50);

				Projectile projectileExplosion = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
				projectileExplosion.construct(Projectiles.PLAYER_GRENADE_EXPLOSION);

				projectileExplosion.init(projectile.direction, new Vector2(projectile.getX() + xRandomization, projectile.getY() + yRandomization));
				projectileExplosion.setX(projectileExplosion.getX() + xRandomization);
				projectileExplosion.setY(projectileExplosion.getY() + yRandomization);
				GlobalController.bulletControllerFriendly.addActor(projectileExplosion);

				// projectileExplosion.init(characterReceiving);

				// if (characterReceiving == null)
				// {
				// // Cas d'un truc qui explose tout seul
				// projectileExplosion.setX(projectile.getCenterX() + xRandomization);
				// projectileExplosion.setY(projectile.getCenterY() + yRandomization);
				// GlobalController.bulletControllerFriendly.addActor(projectileExplosion);
				//
				// } else
				// {
				// projectileExplosion.setX(projectileExplosion.getX() + xRandomization);
				// projectileExplosion.setY(projectileExplosion.getY() + yRandomization);
				// GlobalController.bulletControllerFriendly.addActor(projectileExplosion);
				// }
				// Add 2 particles by explosion
				for (int j = 0; j < 2; j++)
				{
					ExplosionParticle explosionParticle = new ExplosionParticle();
					explosionParticle.init(projectile.getCenterX(), projectile.getCenterY(), ParticleColors.getInstance().getExplosionColor());
					GlobalController.particleController.addActor(explosionParticle);
				}
			}
			projectile.remove();
		}
	}

	public class Explosion extends ProjectileComportementFrame
	{
		private int	width;
		private int	height;

		private int	widthMax;
		private int	widthMin;
		private int	heightMax;
		private int	heightMin;
		private int	sizeModulation;

		public Explosion(int width, int height)
		{
			this.width = width;
			this.height = height;
		}

		public Explosion(int width, int height, int sizeModulation)
		{
			this(width, height);
			this.sizeModulation = sizeModulation;
			widthMax = width + sizeModulation;
			widthMin = width - sizeModulation;
			heightMax = height + sizeModulation;
			heightMin = height - sizeModulation;
		}

		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			projectile.disablePhysics();

			if (sizeModulation > 0)
			{
				width = new Random().nextInt(widthMax - widthMin) + widthMin;
				height = new Random().nextInt(heightMax - heightMin) + heightMin;
			}

			projectile.setWidth(width);
			projectile.setHeight(height);
			projectile.rotation = 45;
			projectile.setColor(ParticleColors.getInstance().getExplosionColor()[new Random().nextInt(ParticleColors.getInstance().getExplosionColor().length)]);
		}
	}

	public class BazookaExplosion extends Explosion
	{
		public BazookaExplosion(int width, int height)
		{
			super(width, height);
		}

		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			super.comportement_init(projectile, characterSender);
			projectile.setColor(new Color(1, 1, 1, 0.4f));
		}
	}

	public class Rocket extends ProjectileComportementFrame
	{
		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{

			Player p = (Player) characterSender;

			int height = 35;
			Ressource r = new Ressource(new DrawableAnimation(0.14f, R.c().fx_explode_circle), p.getCorretedX(-20) - height / 2, projectile.getCenterY() - height / 2, height, true);
			r.setColor(1, 1, 1, 0.5f);
			GlobalController.fxController.addActor(r);

			Projectile projectile_one = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
			projectile_one.construct(Projectiles.PLAYER_ROCKET_SIMPLE);
			projectile_one.init(characterSender);
			projectile_one.precision = 0;
			GlobalController.bulletControllerFriendly.addActor(projectile_one);

			Projectile projectile_bis = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
			projectile_bis.construct(Projectiles.PLAYER_ROCKET_SIMPLE);
			projectile_bis.init(characterSender);
			projectile_bis.precision = 3.5f;
			GlobalController.bulletControllerFriendly.addActor(projectile_bis);

			Projectile projectile_ter = Pools.get(Projectile.class, Projectile.PROJECTILE_POOL_SIZE).obtain();
			projectile_ter.construct(Projectiles.PLAYER_ROCKET_SIMPLE);
			projectile_ter.init(characterSender);
			projectile_ter.precision = -3.5f;
			GlobalController.bulletControllerFriendly.addActor(projectile_ter);

			if (projectile.direction == Direction.RIGHT_DIRECTION)
			{
				projectile_bis.rotation = 17;
				projectile_ter.rotation = -17;
			} else
			{
				projectile_bis.rotation = -17;
				projectile_ter.rotation = 17;

			}

		}

		@Override
		protected void comportement_act(Projectile projectile)
		{
			super.comportement_act(projectile);
			// Dans le act car il n'y a pas encore de group parent dans le init
			projectile.active = false;
			projectile.remove();
			projectile.reset();
		}

	}

	public class SimpleRocket extends Straight
	{
		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			super.comportement_init(projectile, characterSender);
			projectile.effect = new Timer(0.04f);
		}

		@Override
		protected void comportement_act(Projectile projectile)
		{
			super.comportement_act(projectile);
			// do smoke
			if (projectile.effect.doAction(Gdx.graphics.getDeltaTime()))
			{
				int height = 35;
				Ressource r = new Ressource(new DrawableAnimation(0.1f, R.c().fx_pop), projectile.getCenterX() - height / 2, projectile.getCenterY() - height / 2, height, true);
				r.setColor(1, 1, 1, 0.5f);
				GlobalController.fxController.addActor(r);
			}

		}

		@Override
		protected void comportement_endingEffect(Projectile projectile, Character characterReceiving)
		{
			super.comportement_endingEffect(projectile, characterReceiving);
			S.c().play(TyrianSound.soundEffect_weapons_rocketLaucherGunExplode, GlobalController.player, projectile);
			projectile.active = false;
			projectile.reset();
			projectile.remove();
		}
	}

	private static class GenericComportementList
	{
		public static void goStraight_Initialisation(Projectile projectile, Character characterSender)
		{
			projectile.disablePhysics();

			if (projectile.projectilesType.quantityPerShoot > 1)
			{
				int xRandomization = new Random().nextInt(50);
				int yRandomization = new Random().nextInt(20);
				if (projectile.direction == Direction.LEFT_DIRECTION)
				{
					projectile.setX(projectile.getX() - xRandomization);
				} else
				{
					projectile.setX(projectile.getX() + xRandomization);
				}
				projectile.setY(projectile.getY() + yRandomization);
			}

			float MAX_PRECISION = 0.6f;
			float MIN_PRECISION = 0.1f;
			int[] way = { -1, 1 };
			projectile.precision = (new Random().nextFloat() * (MAX_PRECISION - MIN_PRECISION) + MIN_PRECISION) * way[new Random().nextInt(way.length)];
		}

		public static void goStraight_Act(Projectile projectile)
		{
			float increment = (projectile.projectilesType.speed / PhysicalEntity.speedCoefficient) * Gdx.graphics.getDeltaTime();

			if (projectile.direction == Direction.RIGHT_DIRECTION)
			{
				projectile.setX(projectile.getX() + increment);
			} else
			{
				projectile.setX(projectile.getX() + -increment);
			}

			projectile.setY(projectile.getY() + projectile.precision);
		}

		public static void setActivation(final Projectile projectile, Action action)
		{
			if (projectile.getActions().size == 0)
			{
				Action action1 = Actions.delay(0.15f);
				Action action2 = Actions.run(new Runnable()
				{
					@Override
					public void run()
					{
						projectile.active = true;
					}
				});

				if (action == null)
				{
					projectile.addAction(Actions.sequence(action1, action2));
				} else
				{
					projectile.addAction(Actions.sequence(action1, action, action2));
				}
			}
		}
	}

	public class Bomb extends Physic
	{
		public Bomb(int xSpeed, int xModulation, int ySpeed, int yModulation)
		{
			super(xSpeed, xModulation, ySpeed, yModulation);
		}

		@Override
		protected void comportement_init(Projectile projectile, Character characterSender)
		{
			super.comportement_init(projectile, characterSender);
			projectile.setDisableBlockCollision(true);
		}

		@Override
		protected void comportement_endingEffect(Projectile projectile, Character characterReceiving)
		{
			GameStage.cameraShake(GameStage.SMALL_SHAKE);
			S.c().play(TyrianSound.soundEffect_meteorLand, characterReceiving, projectile);
			// Only effect, le degat est déja dans la bomb...

			for (int i = 0; i < 4; i++)
			{
				int xRandomization = new Random().nextInt(100) - 50;
				int yRandomization = new Random().nextInt(30);
				int height = 60;
				Ressource r = new Ressource(new DrawableAnimation(0.16f, R.c().fx_explode_circle), projectile.getCenterX() - height / 2 + xRandomization, projectile.getCenterY() - height / 2 + yRandomization, height,
						true);
				r.setColor(ParticleColors.getInstance().getGreenWaste()[new Random().nextInt(ParticleColors.getInstance().getGreenWaste().length)]);
				GlobalController.fxController.addActor(r);
			}
			// Add 4 particles
			for (int j = 0; j < 4; j++)
			{
				ExplosionParticle explosionParticle = new ExplosionParticle();
				explosionParticle.init(projectile.getCenterX(), projectile.getCenterY(), ParticleColors.getInstance().getGreenWaste());
				GlobalController.particleController.addActor(explosionParticle);
			}
			super.comportement_endingEffect(projectile, characterReceiving);
		}
	}
}

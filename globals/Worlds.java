package globals;

import ressources.R;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum Worlds
{
	WOLRD_1(R.c().world_images[0], "Green valleys", "1 - 10", "The butcher", "Rookie", 3, false),
	WOLRD_2(R.c().world_images[1], "Forge enfer", "11 - 20", "Dumblebee", "Basic", 5, false),
	WOLRD_3(R.c().world_images[2], "The ascenssion", "21 - 25", "Sherkan", "Dangerous", 6, false),
	WOLRD_4(R.c().world_images[3], "Syn City", "26 - 36", "The kraken", "Hell", 7, false),
	WOLRD_5(R.c().world_images[4], "The depths of putricides", "36 - 46", "Ratchet", "Hell", 8, false),
	WOLRD_6(R.c().world_images[5], "Deep Russia", "46 - X", "Da colonel", "Inferno", 9, false);

	public TextureRegion	level;
	public String			name;
	public String			wave;
	public String			finalBoss;
	public String			plateformType;
	public int				onComplete;
	public boolean			isCompleted;

	// isCompleted permettra de ne plus toucher l'or des boss plusieurs fois ainsi que les étoiles de fin de world.

	private Worlds(TextureRegion level, String name, String wave, String finalBoss, String plateformType, int onComplete, boolean isCompleted)
	{
		this.level = level;
		this.name = name;
		this.wave = wave;
		this.finalBoss = finalBoss;
		this.plateformType = plateformType;
		this.onComplete = onComplete;
		this.isCompleted = isCompleted;
	}
}

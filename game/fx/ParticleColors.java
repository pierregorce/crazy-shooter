package game.fx;

import com.badlogic.gdx.graphics.Color;

public class ParticleColors
{

	private static ParticleColors particleColor;

	public static ParticleColors getInstance()
	{
		if (particleColor == null)
		{
			particleColor = new ParticleColors();
		}
		return particleColor;
	}

	private Color[] bloodColor = new Color[5];
	private Color[] iceColor = new Color[5];
	private Color[] greenColor = new Color[5];
	private Color[] explosion = new Color[6];
	private Color[] violetWaste = new Color[5];
	private Color[] greenWaste = new Color[5];

	private ParticleColors()
	{
		bloodColor[0] = new Color(255 / 255f, 11 / 255f, 11 / 255f, 1);
		bloodColor[1] = new Color(204 / 255f, 35 / 255f, 35 / 255f, 1);
		bloodColor[2] = new Color(145 / 255f, 36 / 255f, 36 / 255f, 1);
		bloodColor[3] = new Color(170 / 255f, 23 / 255f, 23 / 255f, 1);
		bloodColor[4] = new Color(133 / 255f, 23 / 255f, 23 / 255f, 1);

		iceColor[0] = new Color(203 / 255f, 223 / 255f, 244 / 255f, 1);
		iceColor[1] = new Color(228 / 255f, 255 / 255f, 255 / 255f, 1);
		iceColor[2] = new Color(254 / 255f, 254 / 255f, 255 / 255f, 1);
		iceColor[3] = new Color(183 / 255f, 206 / 255f, 229 / 255f, 1);
		iceColor[4] = new Color(205 / 255f, 254 / 255f, 254 / 255f, 1);

		greenColor[0] = new Color(127 / 255f, 223 / 255f, 4 / 255f, 1);
		greenColor[1] = new Color(41 / 255f, 155 / 255f, 76 / 255f, 1);
		greenColor[2] = new Color(34 / 255f, 183 / 255f, 55 / 255f, 1);
		greenColor[3] = new Color(96 / 255f, 221 / 255f, 52 / 255f, 1);
		greenColor[4] = new Color(154 / 255f, 246 / 255f, 36 / 255f, 1);

		explosion[0] = new Color(250 / 255f, 228 / 255f, 156 / 255f, 1);
		explosion[1] = new Color(237 / 255f, 180 / 255f, 46 / 255f, 1);
		explosion[2] = new Color(251 / 255f, 125 / 255f, 32 / 255f, 1);
		explosion[3] = new Color(244 / 255f, 90 / 255f, 38 / 255f, 1);
		explosion[4] = new Color(217 / 255f, 81 / 255f, 66 / 255f, 1);
		explosion[5] = new Color(164 / 255f, 164 / 255f, 164 / 255f, 1);

		violetWaste[0] = new Color(51 / 255f, 51 / 255f, 153 / 255f, 1);
		violetWaste[1] = new Color(51 / 255f, 255 / 255f, 255 / 255f, 1);
		violetWaste[2] = new Color(51 / 255f, 51 / 255f, 255 / 255f, 1);
		violetWaste[3] = new Color(102 / 255f, 102 / 255f, 255 / 255f, 1);
		violetWaste[4] = new Color(51 / 255f, 153 / 255f, 255 / 255f, 1);

		greenWaste[0] = new Color(255 / 255f, 146 / 255f, 51 / 255f, 1);
		greenWaste[1] = new Color(255 / 255f, 247 / 255f, 51 / 255f, 1);
		greenWaste[2] = new Color(105 / 255f, 165 / 255f, 38 / 255f, 1);
		greenWaste[3] = new Color(159 / 255f, 255 / 255f, 51 / 255f, 1);
		greenWaste[4] = new Color(183 / 255f, 255 / 255f, 102 / 255f, 1);
	}

	public Color[] getBloodColor()
	{
		return bloodColor;
	}

	public Color[] getIceColor()
	{
		return iceColor;
	}

	public Color[] getGreenColor()
	{
		return greenColor;
	}

	public Color[] getExplosionColor()
	{
		return explosion;
	}

	public Color[] getVioletWaste()
	{
		return violetWaste;
	}

	public Color[] getGreenWaste()
	{
		return greenWaste;
	}

}

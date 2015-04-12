package utilities;

public class Methods
{
	/**
	 * Renvoie la largeur calculé pour une hauteur voulue et les dimmensions initiales
	 * 
	 * @param finalHeight
	 * @param initialWidth
	 * @param initialHeight
	 * @return
	 */
	public static float scaleByHeight(float finalHeight, float initialWidth, float initialHeight)
	{
		return initialWidth * finalHeight / initialHeight;
	}

	/**
	 * Renvoie la hauteur calculée pour une largeur voulue et les dimmensions initiales
	 * 
	 * @param finalWidth
	 * @param initialWidth
	 * @param initialHeight
	 * @return
	 */
	public static float scaleByWidth(float finalWidth, float initialWidth, float initialHeight)
	{
		return initialHeight * finalWidth / initialWidth;
	}
}

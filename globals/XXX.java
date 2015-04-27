package globals;

public class XXX
{
	// idée d'un panneau sur un block avec un pop up tutorial
	// idée armor assez simple a integrer
	// base64code - reprendre le file save un peu mal codé...
	// Pools.free(changeEvent);

	// *************CODE
	// reprendre position des projectiles lors du tirs des enemys (anchor) idem player, faire ça en mode debug
	// anciens ennemies comportement pas bon
	// boss3 fix comportement (trop simple as alex)
	// bug fail level ?
	// faire le try catch sur les levels pour ne pas niquer les anciens jouers
	// verifier la puissance des armes...

	// ************DESSIN
	// Animation de saut du player
	// renomer les iregular particle et les mettres dans primitive/fx, idem pour white square...
	// BOSS 4 Scientist = boss 4, blizard / mega laser percing / fiole avec tapis au sol ?
	// pourrait poser des fioles au sol et ?

	// - ********* PAS TROP GENANT
	// bullet minigun bof...
	// projectile tapis effect de collision moyen...

	// ----------------------------------------------------------- IDEA PATTERN
	//
	//
	//
	// ---------------------------------BASE PATTERN
	// WAVE TIME (as soon as all monster have pop)
	// no more wave time
	//
	// QUANTITY (add multipline line for increase quantity)
	// few = 2 - 3
	// many = 4 -6
	// lot = 6 - 10
	// enorm = 15-20
	//
	// POPTIME (default)
	// quick = 0.5-2s
	// medium = 3-4s
	// slow = 6-7s
	//
	// DELAY
	// unspecified = 0
	// late (default) = 2
	// late_x2 (user_specified) = 4
	// late_x3 (user_specified) = 6
	//
	// ---------------------------------COMBINATED PATTERN
	// enemy_discover : few medium
	// enemy_fastSurcharge : enorm quick
	// enemy_normal : many medium (late_x2 random)
	// enemy_rappel : few slow late_x2
	// enemy_overTime : lot medium (late_x2 random)

}
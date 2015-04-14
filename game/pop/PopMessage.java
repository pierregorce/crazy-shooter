package game.pop;

import game.entitiy.Entities;

import java.util.Random;

import ressources.ActionFactory;
import ressources.R;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class PopMessage extends Group
{
	private Label			label;
	private static String[]	messagesExplosion	= { "KBOOM!", "POW!" };
	private static String[]	messagesPlayer		= { "@#?!!!", "AACK!", "ARGH!", "SHIT!" };
	private static String[]	messagesBoss1		= { "BAM BAM", "BWABWA!", "?#@*&%!" };
	private static String[]	messagesBoss2		= { "BZZ BZZ", "ZZZBBB !", "?&%!" };
	private static String[]	messagesBoss3		= { "TAA DAA", "WISH!" };
	private static String[]	messagesBoss3_phase	= { "ZOU" };
	private static String[]	messagesBoss4		= { "KRIGT", "KRAK" };
	private static String[]	messageKnight		= { "BLOCK", "PROTECT" };
	private static String[]	messageLoseLife		= { "-" };
	private static String[]	messageHeal			= { "+" };

	private static float	critSizeIncrease	= 1.3f;

	private MessageType		m_messageType;

	public enum MessageType
	{
		ENEMY_LOSE_LIFE(messageLoseLife, 3, 0.5f, Color.WHITE),
		PLAYER_LOSE_LIFE(messageLoseLife, 4, 0.8f, Color.RED),
		EXPLOSION(messagesExplosion, 5, 0.95f, Color.ORANGE),
		PLAYER_HIT(messagesPlayer, 4, 0.95f, Color.RED),
		BOSS_CRY_1(messagesBoss1, 5, 0.7f, Color.WHITE),
		HEAL(messageHeal, 4, 0.7f, Color.GREEN),
		BOSS_CRY_2(messagesBoss2, 5, 0.7f, Color.ORANGE),
		BOSS_CRY_3(messagesBoss3, 4, 0.7f, Color.PINK),
		BOSS_CRY_4(messagesBoss4, 4, 0.7f, Color.MAROON),
		BOSS_PHASE_3(messagesBoss3_phase, 4, 0.7f, Color.PINK),
		KNIGHT(messageKnight, 4, 1.2f, Color.ORANGE);

		private String[]	messages;
		private int			scale;
		public float		depopDelay;
		private Color		color;

		private MessageType(String[] messages, int scale, float depopDelay, Color color)
		{
			this.messages = messages;
			this.scale = scale;
			this.depopDelay = depopDelay;
			this.color = color;
		}

		public String getMessage()
		{
			return messages[new Random().nextInt(messages.length)];
		}

		public float getScale()
		{
			float variation = new Random().nextFloat() * 1.2f;
			int way = new Random().nextInt(1);
			int ways[] = { -1, 1 };
			return scale + ways[way] * variation;
		}

		public Color getColor()
		{
			return color;
		}

	}

	public PopMessage(String value, Entities entities, MessageType messageType)
	{
		this.m_messageType = messageType;
		BitmapFont bitmapFont = R.c().fontTypeViolet;

		label = new Label("", new LabelStyle(bitmapFont, null));

		switch (messageType) {
		case ENEMY_LOSE_LIFE:
			label.setText(messageType.getMessage() + value);
			break;
		case PLAYER_LOSE_LIFE:
			label.setText(messageType.getMessage() + value);
			break;
		case HEAL:
			label.setText(messageType.getMessage() + value);
			break;
		default:
			label.setText(messageType.getMessage());
			break;
		}

		int xVariation = new Random().nextInt(40) - 20;
		int yVariation = new Random().nextInt(30) + 20;

		label.setFontScale(messageType.getScale());

		Vector2 dest = new Vector2(entities.getCenterX() + xVariation, entities.getTop() + yVariation);
		SequenceAction moveAction = ActionFactory.getMoveToAction(dest, messageType.depopDelay);

		label.setPosition(entities.getCenterX(), entities.getTop());
		addActor(label);

		SequenceAction a = new SequenceAction(moveAction, new RemoveActorAction());
		label.addAction(a);
		label.setColor(messageType.color);
		label.setRotation(20);
		label.rotateBy(20);
	}

	public PopMessage(Entities entities, MessageType messageType)
	{
		this("", entities, messageType);
	}

	public void setCritical()
	{
		label.setFontScale(m_messageType.getScale() * critSizeIncrease);
		label.setColor(new Color(91 / 255f, 123 / 255f, 138 / 255f, 1));
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (getChildren().size == 0)
		{
			remove();
		}
	}
}

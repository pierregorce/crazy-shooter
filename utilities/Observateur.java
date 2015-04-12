package utilities;


//Interface impl�ment�e par tous les observateurs.
public interface Observateur
{
     // M�thode appel�e automatiquement lorsque l'�tat (position ou pr�cision) du GPS change.
     public boolean touchDown(float x, float y, Observable o);
     public boolean touchUp(float x, float y, Observable o);
}


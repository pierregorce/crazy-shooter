package utilities;


//Interface implémentée par tous les observateurs.
public interface Observateur
{
     // Méthode appelée automatiquement lorsque l'état (position ou précision) du GPS change.
     public boolean touchDown(float x, float y, Observable o);
     public boolean touchUp(float x, float y, Observable o);
}



public class Die {

    //Defining Die class to be able to have die in the game

    Preferences preferencesPointerDie;
    public Die(Preferences _preferences){
        this.preferencesPointerDie=_preferences;

    }

    public void rollDie (Preferences preferences){
        this.preferencesPointerDie=preferences;
        int die = (int)(Math.random()*11);
        while(die<5){
            die = (int)(Math.random()*11);
        }
        preferencesPointerDie.dieCount=die;


    }
}

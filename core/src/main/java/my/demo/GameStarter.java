package my.demo;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameStarter extends Game {


    @Override
    public void create() {
        setScreen(new SecondScreen());
    }
}

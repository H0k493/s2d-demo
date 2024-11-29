package my.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import my.demo.widget.AdjustableContainer;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen extends AbstractScreen {
    private Stage stage;
    private TextButton button;

    SpriteBatch batch;
    boolean shown = false;
    AdjustableContainer heroTable;

    @Override
    public void show() {
        batch = new SpriteBatch();
        heroTable = getComposer().getHeroesWindow();
        button = getComposer().getButton();
        button.setPosition(10, 10);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                shown = !shown;
                ((TextButton)event.getListenerActor()).setText(shown ? "Close" : "Open");
                if (shown) {
                    stage.addActor(heroTable);
                    getComposer().invalidate();
                    heroTable.pack();
                    centerActor(heroTable);
                    heroTable.setVisible(true);
                } else {
                    heroTable.setVisible(false);
                    heroTable.remove();
                }
            }
        });
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
        batch.begin();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("Screen width [" + Gdx.graphics.getWidth() + "]");
        System.out.println("Screen height [" + Gdx.graphics.getHeight() + "]");
        stage.getViewport().update(width, height, true);
        getComposer().resize(width, height);
        reattach();
        if (shown) {
            heroTable.pack();
            centerActor(heroTable);
        }
    }

    private void reattach() {
        boolean isAttached = heroTable.remove();
        getComposer().invalidate();
        if (isAttached) {
            stage.addActor(heroTable);
        }
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }

    private void centerActor (Actor actor) {
        actor.setPosition((stage.getWidth() - actor.getWidth()) / 2,
            (stage.getHeight() - actor.getHeight()) / 2);
    }
//    com.ray3k.stripe.DraggableList
}

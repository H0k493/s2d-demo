package my.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/** Second screen of the application. Displayed after the application is created. */
public class ThirdScreen implements Screen {
    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));;
    private Drawable img = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img.png"))));
    private Drawable frame = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("frame_2.png"))));

    @Override
    public void show() {
        Table root = new Table();
        Table hMenu = createTable();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        stage.setRoot(root);
        stage.addActor(hMenu);
        stage.setDebugAll(true);
    }

    private float getW(final Actor a, final float p) {
        return get(a, () -> a.getWidth(), p);
    }

    private float getH(final Actor a, final float p) {
        return get(a, () -> a.getHeight(), p);
    }

    private float get(final Actor a, final Supplier<Float> extractor, final float p) {
        System.out.println(p + "% of " + extractor.get() + " is " + (extractor.get() * p));
        return extractor.get() * p;
    }

    private Table createTable() {
        final Table menuElement = new Table();
        menuElement.setFillParent(true);
        Actor header = new TextButton("Open", skin);
        menuElement.add(header).height(Gdx.graphics.getHeight() * 0.1f).center().row();
        menuElement.add().grow();
        return menuElement;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.getViewport().apply(true);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    }


    private float getSH(float p) {
        if (p == 1) return Gdx.graphics.getHeight();
        return get(null, () -> (float) Gdx.graphics.getHeight(), p);
    }
}

package my.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** Second screen of the application. Displayed after the application is created. */
public class SecondScreen implements Screen {
    private Stage stage;
    private TextButton button;
    boolean shown = false;
    Container tbl;
    private Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));;
    private Drawable img = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img.png"))));
    @Override
    public void show() {
        tbl = createContainer();
        button = new TextButton("Open", skin);
        button.setPosition(10, 10);
        button.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
            shown = !shown;
            ((TextButton) event.getListenerActor()).setText(shown ? "Close" : "Open");
            if (shown) {
                stage.addActor(tbl);
                tbl.pack();
                centerActor(tbl);
                tbl.setVisible(true);
            } else {
                tbl.setVisible(false);
                tbl.remove();
            }
            }
        });
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
    }

    private Container createContainer() {
        VerticalGroup vBar = new VerticalGroup();
        ScrollPane scroll = new ScrollPane(vBar, getScrollPaneStyle()) ;
        scroll.setDebug(true, true);
        scroll.setOverscroll(false, true);
        Container<ScrollPane> c = new Container<ScrollPane>(scroll) {
            @Override
            public float getPrefHeight() {
                return Gdx.graphics.getHeight() * 1/1.5f;
            }
        };
        for (int i = 1; i < 30; i++) {
            vBar.addActor(createItem(i));
        }
        return c;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("Screen width [" + Gdx.graphics.getWidth() + "]");
        System.out.println("Screen height [" + Gdx.graphics.getHeight() + "]");
        stage.getViewport().update(width, height, true);
        if (shown) {
            tbl.invalidateHierarchy();
            centerActor(tbl);
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
    }

    private void centerActor (Actor actor) {
        actor.setPosition((stage.getWidth() - actor.getWidth()) / 2,
            (stage.getHeight() - actor.getHeight()) / 2);
    }

    private ScrollPane.ScrollPaneStyle getScrollPaneStyle() {
        ScrollPane.ScrollPaneStyle style = skin.get("default", ScrollPane.ScrollPaneStyle.class);
        style.background = null;
        style.hScroll = null;
        style.hScrollKnob = null;
        style.vScroll = null;
        style.vScrollKnob = null;
        return style;
    }

    private Label.LabelStyle getLabelSkin() {
        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = skin.getFont("font-label");
        ls.fontColor = skin.getColor("white");
        return ls;
    }

    private Actor createItem(int index) {
        Table table = new Table();
        table.setName("heroItem_" + index);
//        table.setDebug(true, true);

        Image image = new Image(img);
        image.setScaling(Scaling.fit);
        table.add(image).center().pad(Value.Fixed.valueOf(getPad()))
            .width(Value.Fixed.valueOf(getImgWidth())).height(Value.Fixed.valueOf(getImgWidth()));

        Label label = new Label("Some label " + index, skin);
        table.add(label).growX().left().padLeft(Value.Fixed.valueOf(getPad())).padRight(Value.Fixed.valueOf(getPad()));

        Label bTxt = new Label("Button " + index, skin);
        Button button = new Button(bTxt, skin);
        table.add(button).width(Value.Fixed.valueOf(getButtonWidth())).padLeft(Value.Fixed.valueOf(getPad()))
            .padRight(Value.Fixed.valueOf(getPad())).center();
        return table;
    }

    private float getButtonWidth() {
        return Gdx.graphics.getWidth() / 3.75f * 0.32f;
    }

    private float getImgWidth() {
        return Gdx.graphics.getWidth() / 3.75f * 0.18f;
    }

    private float getPad() {
        return Gdx.graphics.getWidth() / 3.75f * 0.02f;
    }
}

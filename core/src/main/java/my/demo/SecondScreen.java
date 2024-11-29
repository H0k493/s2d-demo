package my.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.layout.FlowGroup;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.ViewportWidget;
import com.ray3k.tenpatch.TenPatchDrawable;
//import io.github.fourlastor.harlequin.ui.ParallaxImage;
import my.demo.parallax.ParallaxImage;
import my.demo.parallax.ParallaxLayer;

import java.util.function.Supplier;

/** Second screen of the application. Displayed after the application is created. */
public class SecondScreen implements Screen {
    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));;
    Texture imgTexture = new Texture(Gdx.files.internal("img.png"));
    private Drawable img = new TextureRegionDrawable(new TextureRegion(imgTexture));

    Texture frameTexture = new Texture(Gdx.files.internal("frame_2.png"));
    private Drawable frame = new TextureRegionDrawable(new TextureRegion(frameTexture));

    private TextureRegion d10region = new TextureRegion(new Texture(Gdx.files.internal("item-bg2.png")));
    private TenPatchDrawable d10 = new TenPatchDrawable();
    private final Camera ortho = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    Viewport parallaxViewport = new ScreenViewport(ortho);

    private BitmapFont font;
    private ParallaxLayer[] layers;
    private SpriteBatch batch;
    @Override
    public void show() {
        batch = new SpriteBatch();
        loadParallax();
        init10patch();
        initFonts();
        initStage();
        Stack mainContent = new Stack();
        mainContent.add(new ViewportWidget(parallaxViewport));//createParallaxGroup());
        Table hMenu = createHeroesMenu(mainContent);
        createMainMenu(mainContent);
        stage.addActor(hMenu);
    }

    private void initStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Stack rootStack = new Stack();
        rootStack.setSize(getSW(1), getSH(1));
        stage.setRoot(rootStack);
        stage.setDebugAll(true);
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fnt/JetBrainsMono-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = generate(generator);
        generator.dispose();
    }

    private BitmapFont generate(FreeTypeFontGenerator generator) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = new StringBuilder()
            .append("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
            .append("abcdefghijklmnopqrstuvwxyz")
            .append("0123456789")
            .append(".,:;!?*#/\\(){}[]<>-_'\"")
            .append("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЧЦШЩЬЪЫЭЮЯ")
            .append("абвгдеёжзийклмнопрстуфхчцшщьъыэюя")
            .toString();
        parameter.size = 20;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        return generator.generateFont(parameter);
    }

    private void init10patch() {
        d10.setRegion(d10region);
        d10.setHorizontalStretchAreas(new int[] {20, 101});//{15, 106});
        d10.setVerticalStretchAreas(new int[] {29, 76});//{24, 81});
        d10.setTiling(false);
        d10.setMinWidth(118);//128);
        d10.setMinHeight(86);//96);
//        d10.setPadding(10, 10, 10, 10);
        d10.setOffsetX(0);
        d10.setOffsetY(0);
//        playMode: 2
//        crushMode: 0
    }

    private float getW(final Actor a, final float p) {
        return get(a, () -> a.getWidth(), p);
    }

    private float getH(final Actor a, final float p) {
        return get(a, () -> a.getHeight(), p);
    }

    private float get(final Actor a, final Supplier<Float> extractor, final float p) {
        return extractor.get() * p;
    }

    private PopTable.PopTableStyle getPopTableStyle() {
        PopTable.PopTableStyle style  = new PopTable.PopTableStyle();
        style.background = null;
        style.stageBackground = null;
        return style;
    }

    private void createMainMenu(final Group g) {
        float pad = getSW(0.035f);
        TextButton ib = new TextButton("M", getButtonStyle());
        ib.getLabel().setFontScale(1.5f);
//        ib.setDisabled(true);
        com.ray3k.stripe.PopTable pt = new PopTable(getPopTableStyle());
        pt.setVisible(false);
        pt.setDraggable(false);
        pt.show(stage);
        Table mainMenu = new Table() {
            @Override
            public void setVisible(boolean visible) {
                super.setVisible(visible);
                if (!visible) {
                    pt.setVisible(false);
                }
            }
        };
        mainMenu.setFillParent(true);
        mainMenu.setVisible(true);
        mainMenu.add(ib).expand().padRight(pad).padBottom(pad).bottom().right();
        FlowGroup menuGroup = new FlowGroup(false) {
            @Override
            public float getPrefWidth() {
                return getSW(0.5f);
            }
        };
        menuGroup.setName("mainMenu");
        pt.add(menuGroup);
        for (int i = 0; i < 8; i++)
            addMenuItem(menuGroup);
        pt.setAttachOffsetY(getSW(0.0025f));
        pt.attachToActor(ib, Align.topRight, Align.topLeft);
        ib.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
               pt.setVisible(!pt.isVisible());
           }
        });
        g.addActor(mainMenu);
    }

    private void addMenuItem(Group g) {
        Image image = new Image(img);
        float sz = getSW(0.1f);
        Table container = new Table() {
            @Override
            public float getPrefWidth() {
                return sz;
            }
            @Override
            public float getPrefHeight() {
                return sz;
            }
        };
        image.setScaling(Scaling.fit);
        image.setVisible(true);
        container.add(image).pad(get(null, () -> sz, 0.05f));
        g.addActor(container);
    }

    private boolean heroListVisibility = false;
    private void toggleHeroListVisibility(Group parent, Actor[] toggleable) {
        heroListVisibility = !heroListVisibility;
        setChildVisibility(parent, toggleable);
    }

    public boolean getHeroListVisibility() {
        return heroListVisibility;
    }

    private Table createHeroesMenu(Stack root) {
        final Table menuElement = new Table();
        menuElement.setFillParent(true);
        HorizontalGroup headerElement = new HorizontalGroup() {
            @Override
            public float getPrefHeight() {
                return getSH(0.1f);
            }
        };
        TextButton open = new TextButton("Open", getButtonStyle());
        TextButton addItem = new TextButton("Add item", getButtonStyle());
//        TextButton complete = new TextButton("Complete", getButtonStyle());
        open.getLabel().setFontScale(18 / 20f);
        addItem.getLabel().setFontScale(18 / 20f);
//        complete.getLabel().setFontScale(18 / 20f);
        headerElement.addActor(open);
        headerElement.addActor(addItem);
//        headerElement.addActor(complete);
        menuElement.add(headerElement).center().growX().row();
        Table heroList = createHeroList();
        root.add(heroList);
        Table actionTable = getHeroActions();
        root.add(actionTable);
        menuElement.add(root).grow();
        Actor[] heroMenu = new Actor[] {heroList, actionTable};
        open.addListener(new ClickListener() {
               @Override
               public void clicked(InputEvent event, float x, float y) {
                   toggleHeroListVisibility(root, heroMenu);
                   ((TextButton) open).setText(getHeroListVisibility() ? "Close" : "Open");
               }
        });
        addItem.addListener(new ClickListener() {
            private int counter = 0;
            private int[] pos = new int[] {1, 3, 5};
            @Override
            public void clicked(InputEvent event, float x, float y) {
                VerticalGroup vBar = stage.getRoot().findActor("heroesList");
                int p = pos[counter % 3];
                vBar.addActorAt(p + 1, createItem(p,  + p + "." + counter));
                counter++;
            }
        });
//        complete.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Group g = stage.getRoot().findActor("completeHeroes");
//                g.addActor(createCompleteItem());
//            }
//        });
        return menuElement;
    }

    private Group createParallaxGroup() {
        Stack parallaxStack = new Stack();
        parallaxStack.add(new ParallaxImage(new TextureRegion(new Texture("bg\\jungle\\layer 1.png")), 0.1f, 0.0f));
        parallaxStack.add(new ParallaxImage(new TextureRegion(new Texture("bg\\jungle\\layer 2.png")), 0.2f, 0.0f));
        parallaxStack.add(new ParallaxImage(new TextureRegion(new Texture("bg\\jungle\\layer 3.png")), 0.3f, 0.0f));
        parallaxStack.add(new ParallaxImage(new TextureRegion(new Texture("bg\\jungle\\layer 4.png")), 0.5f, 0.0f));
        parallaxStack.add(new ParallaxImage(new TextureRegion(new Texture("bg\\jungle\\layer 5.png")), 0.8f, 0.0f));
        parallaxStack.add(new ParallaxImage(new TextureRegion(new Texture("bg\\jungle\\layer 6.png")), 1f, 0.0f));
        return parallaxStack;
    }

    private boolean childInList(Actor[] toggleable, Actor child) {
        for (Actor t : toggleable) {
            if (child == t) {
                return true;
            }
        }
        return false;
    }

    private void setChildVisibility(Group parent, Actor[] toggleable) {
        for (Actor a : parent.getChildren()) {
            a.setVisible(childInList(toggleable, a) ? heroListVisibility : !heroListVisibility);
        }
    }

    private Actor createCompleteItem(String label) {
        Image image = new Image(img);
        Image iFrame = new Image(frame);
        Label l = new Label(label, getFont());
        l.setFontScale(12 / 20f);
        Stack cStack = new Stack();
        cStack.addActor(image);
        cStack.addActor(iFrame);
        cStack.addActor(l);
        float sz = getSW(0.25f);
        Table container = new Table() {
            @Override
            public float getPrefWidth() {
                return sz;
            }

            @Override
            public float getPrefHeight() {
                return sz;
            }
        };
        image.setScaling(Scaling.fit);
        iFrame.setScaling(Scaling.fit);
        image.setVisible(true);
        iFrame.setVisible(true);
        container.add(cStack).pad(get(null, () -> sz, 0.125f));
        return container;
    }

    private Actor createCompleteHeroes() {
        FlowGroup g = new FlowGroup(false) {
            @Override
            public float getPrefWidth() {
                return getSW(1);
            }
        };
        g.setName("completeHeroes");
        return g;
    }


    private Table createHeroList() {
        VerticalGroup vBar = new VerticalGroup();
        vBar.setName("heroesList");
        vBar.space(0);
        ScrollPane scroll = new ScrollPane(vBar, getScrollPaneStyle()) ;
        scroll.setupOverscroll(getSW(0.35f), 150, 750);
        scroll.setOverscroll(false, true);
        Table container = new Table();
        container.add(scroll).top().grow();
        // placeholder
        vBar.addActor(createHeroPlaceholder());
        // for max upgraded heroes
        vBar.addActor(createCompleteHeroes());
        for (int i = 1; i < 30; i++) {
            vBar.addActor(createItem(i));
        }
        container.setVisible(false);
        return container;
    }

    private Actor createHeroPlaceholder() {
        Table placeHolder = new Table();
        placeHolder.row().height(getSW(0.035f));
        return placeHolder.add().getTable();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        parallaxViewport.apply(false);
//        drawParallax(batch, Gdx.graphics.getDeltaTime());
        stage.getViewport().apply(true);
        stage.draw();
//        drawTestText();
        batch.begin();
        batch.setColor(Color.WHITE);
        drawWithTint();
//        fonts.get(18).draw(batch, "Русский текст - посмотрим!", 0, 25);
        batch.end();
    }
    private final float[] verts = new float[20];
    private void drawWithTint() {
        int i = 0;
        verts[i++] = 10;
        verts[i++] = 10;
        verts[i++] = Color.PINK.mul(batch.getColor()).toFloatBits();
        verts[i++] = 20;
        verts[i++] = 20;

        verts[i++] = 10;
        verts[i++] = 10 + 100;
        verts[i++] = Color.BLUE.mul(batch.getColor()).toFloatBits();
        verts[i++] = 20;
        verts[i++] = 120;

        verts[i++] = 110;
        verts[i++] = 110;
        verts[i++] = Color.GREEN.mul(batch.getColor()).toFloatBits();
        verts[i++] = 120;
        verts[i++] = 120;

        verts[i++] = 110;
        verts[i++] = 10;
        verts[i++] = Color.RED.mul(batch.getColor()).toFloatBits();
        verts[i++] = 120;
        verts[i++] = 20;
        batch.draw(imgTexture, verts, 0, verts.length);
    }
    private void drawTestText() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fnt/JetBrainsMono-Regular.ttf"));
        batch.begin();
        for (int i = 8; i <= 40; i++) {
            BitmapFont f = font;
            String str = "(" + i + ")0123456789'0123456789'0123456789'0123456789'0123456789'0123456789'0123456789'0123456789'0123456789'0123456789";
            Label l = new Label(str, getFont());
            l.setPosition(0, 25 * (i - 7));
            l.setFontScale(i / 20f);
            l.draw(batch, 1.0f);
        }
        batch.end();
        generator.dispose();
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

    private ScrollPane.ScrollPaneStyle getScrollPaneStyle() {
        ScrollPane.ScrollPaneStyle style = skin.get("default", ScrollPane.ScrollPaneStyle.class);
        style.background = null;
        style.hScroll = null;
        style.hScrollKnob = null;
        style.vScroll = null;
        style.vScrollKnob = null;
        return style;
    }

    private Actor createItem(int index) {
        return createItem(index, "" + index);
    }

    private Actor createItem(int index, String title) {
        Table table = new Table() {
            @Override
            public float getPrefWidth() {
                return getSW(1);
            }
        };
        table.setName("heroItem_" + title);
        table.setBackground(d10);

        table.pad(0);

        Image image = new Image(img);
        Image iFrame = new Image(frame);
        image.setScaling(Scaling.fit);
        iFrame.setScaling(Scaling.fit);
        image.setVisible(true);
        iFrame.setVisible(true);
        table.stack(image, iFrame).center().pad(getSW(0.035f))
            .width(getSW(0.20f)).height(getSW(0.20f));

        Label label = new Label("Some label " + title, getFont());
        table.add(label).growX().left();

        TextButton button = new TextButton("Button " + title, getButtonStyle());
        table.add(button).width(getSW(0.30f)).padLeft(getSW(0.035f))
            .padRight(getSW(0.035f)).center();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                moveToComplete(event.getListenerActor());
            }
        });
        return table;
    }

    private void moveToComplete(Actor actor) {
        Group g = stage.getRoot().findActor("completeHeroes");
        Group parent = actor.getParent();
        int index = getChildIndex(parent.getParent(), parent);
        parent.getParent().removeActor(parent);
        g.addActorAt(index < 0 ? 0 : index, createCompleteItem(((TextButton)actor).getText().toString()));
    }

    private int getChildIndex(final Group parent, final Actor children) {
        int idx = 0;
        for (Actor a : parent.getChildren()) {
            if (a.getName() == null) {
                idx++;
                continue;
            }
            if (a.getName().startsWith("heroItem_") && a == children) return idx;
            idx++;
        }
        return -1;
    }

    private Label.LabelStyle getFont() {
        Label.LabelStyle ls = skin.get("default", Label.LabelStyle.class);
        ls.font = font;
        return ls;
    }

    private TextButton.TextButtonStyle getButtonStyle() {
        TextButton.TextButtonStyle bs = skin.get("default", TextButton.TextButtonStyle.class);
        bs.font = font;
        return bs;
    }

    private float getSW(float p) {
        if (p == 1) return Gdx.graphics.getWidth();
        return get(null, () -> (float) Gdx.graphics.getWidth(), p);
    }

    private float getSH(float p) {
        if (p == 1) return Gdx.graphics.getHeight();
        return get(null, () -> (float) Gdx.graphics.getHeight(), p);
    }

    private Table getHeroActions() {
        Table table = new Table() {
            @Override
            public float getPrefWidth() {
                return getSW(1);
            }
        };
        Table actions = new Table();
        float pad = getSW(0.035f);
        actions.add(new TextButton("Action1", skin)).top().padLeft(pad).padRight(pad).left();
        actions.add(new TextButton("Action2", skin)).top().padLeft(pad).padRight(pad);
        actions.add(new TextButton("Action3", skin)).top().padLeft(pad).padRight(pad).right();
        table.add(actions).growX().row();
        table.add().grow();
        table.setVisible(false);
        return table;
    }

    private void loadParallax() {
        layers = new ParallaxLayer[6];
        layers[0] = new ParallaxLayer(new Texture("bg\\jungle\\layer 1.png"), 0.1f, ortho);
        layers[1] = new ParallaxLayer(new Texture("bg\\jungle\\layer 2.png"), 0.2f, ortho);
        layers[2] = new ParallaxLayer(new Texture("bg\\jungle\\layer 3.png"), 0.3f, ortho);
        layers[3] = new ParallaxLayer(new Texture("bg\\jungle\\layer 4.png"), 0.5f, ortho);
        layers[4] = new ParallaxLayer(new Texture("bg\\jungle\\layer 5.png"), 0.8f, ortho);
        layers[5] = new ParallaxLayer(new Texture("bg\\jungle\\layer 6.png"), 1.0f, ortho);

    }

    private void drawParallax(final SpriteBatch batch, final float delta) {
        if (layers == null) return;
        int speed = 250;
        ortho.position.x -= speed * Gdx.graphics.getDeltaTime();
        ortho.update();
        batch.setProjectionMatrix(ortho.combined);
        batch.begin();
        for (ParallaxLayer layer : layers) {
            layer.render(batch);
        }
        batch.end();
    }
}

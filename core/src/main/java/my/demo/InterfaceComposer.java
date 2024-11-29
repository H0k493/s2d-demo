package my.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import my.demo.widget.AdjustableContainer;

import java.util.function.Consumer;

public class InterfaceComposer {
    ResourceAccessor accessor;
    GraphicState state;

    public InterfaceComposer(final ResourceAccessor accessor) {
        this.accessor = accessor;
        state = new GraphicState();
    }

    private AdjustableContainer heroesWindow;

    public AdjustableContainer getHeroesWindow() {
        if (heroesWindow == null) {
            heroesWindow = createHeroesWindow();
        }
        return heroesWindow;
    }

    private AdjustableContainer createHeroesWindow() {
        VerticalGroup vBar = new VerticalGroup();
        ScrollPane scroll = new ScrollPane(vBar, getScrollPaneStyle());
        scroll.setDebug(true, false);
        scroll.setOverscroll(false, true);
        for (int i = 1; i < 30; i++) {
            vBar.addActor(createItem(i, heroesWindow));
        }
        heroesWindow = new AdjustableContainer<ScrollPane>(scroll, () -> Gdx.graphics.getWidth() / 3.75f, () -> Gdx.graphics.getHeight() / 1.5f);
        return heroesWindow;
    }

    public void invalidate() {
        heroesWindow.invalidate();
        System.out.println("Invalidate called!");
        if (state.dirty) {
            System.out.println("Dirty is set. Invalidate!");
            invalidateHeroListContainer();
            state.dirty = false;
        } else {
            System.out.println("Dirty is clear. No need to invalidate");
        }
    }

    private void invalidateHeroListContainer() {
        dump = state.dirty;
        if (heroesWindow.isVisible()) {
            String prefix = "heroItem_";
            iterate("heroItem_", (a) -> updateHeroItem(a));
            heroesWindow.invalidateHierarchy();
        }
    }
    boolean dump = true;

    private void updateHeroItem(final Actor a) {
        Table t = (Table) a;
        float pad = state.viewportWidth * 1/4f * 0.02f;
        float c1w = state.viewportWidth * 1/4f * 0.18f;
        float c3w = state.viewportWidth * 1/4f * 0.32f;
        float c2w = state.viewportWidth * 1/4f - c1w - c3w;
        if (dump) {
            System.out.println("Pad is [" + pad + "]");
            System.out.println("Cell 1 width/height is [" + c1w + "]");
            System.out.println("Cell 2 width is [" + c2w + "]");
            System.out.println("Cell 3 width is [" + c3w + "]");
        }
        t.getCells().get(0).width(c1w).height(c1w).pad(pad).center();
        t.getCells().get(2).width(c3w).center().padLeft(pad).padRight(pad);
        t.getCells().get(1).padLeft(pad).padRight(pad).left().prefWidth(c2w);
        dump = false;
    }

    private void iterate(final String prefix, Consumer<Actor> updater) {
        int index = 1;
        Actor a = null;
        do {
            a = heroesWindow.findActor(prefix + index);
            if (a != null) {
                updater.accept(a);
            }
            index++;
        } while (a != null);
    }
    private Actor createItem(int index, WidgetGroup root) {
        Table table = new Table();
        table.setName("heroItem_" + index);
//        table.setDebug(true, true);

        Image image = new Image(accessor.getAvatar());
        Image iFrame = new Image(accessor.getFrame());
        image.setScaling(Scaling.fit);
        iFrame.setScaling(Scaling.fit);
        Stack stack = new Stack();
        stack.add(image);
        stack.add(iFrame);
        stack.setName("heroImage_" + index);
        table.stack(image, iFrame).center();

        Label label = new Label("Hero name " + index, accessor.getSkin());
        label.setAlignment(Align.left);
        table.add(label).growX();

        Label buttonText = new Label("Buy Hero " + index, accessor.getSkin());
        Button button = new Button(buttonText, accessor.getSkin());
        button.setName("buyButton" + index);

        table.add(button);
        return table;
    }

    private float getValue(final Actor a) {
        Value dim = Value.percentWidth(0.25f);;
        System.out.println("Get value is [" + dim.get(a) + "]");
        return dim.get(a);
    }

    private ScrollPane.ScrollPaneStyle getScrollPaneStyle() {
        ScrollPane.ScrollPaneStyle style = accessor.getSkin().get("default", ScrollPane.ScrollPaneStyle.class);
        style.background = null;
        style.hScroll = null;
        style.hScrollKnob = null;
        style.vScroll = null;
        style.vScrollKnob = null;
        return style;
    }

    private Label.LabelStyle getLabelSkin() {
        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = accessor.getSkin().getFont("font-label");
        ls.fontColor = accessor.getSkin().getColor("white");
        return ls;
    }

    public TextButton getButton() {
        return new TextButton("Open", accessor.getSkin());
    }

    public void updateHeroSize(Stage stage, Table heroTable) {
        int idx = 1;
        Group g = null;
        float dim = heroTable.getPrefWidth() * 0.2f;
        do {
            String name = "heroImage_" + idx;
            System.out.println("Looking for [" + name + "]");
            g = stage.getRoot().findActor(name);
            if (g != null) {
                System.out.println("Updating [" + name + "] for size [" + dim + "]");
                g.setSize(dim, dim);
            } else {
                System.out.println("Didn't find [" + name + "]");
            }
            idx++;
        } while (g != null);
    }

    public void resize(final int width, final int height) {
        state.resize(width, height);
    }

    private static class GraphicState {
        int viewportWidth;
        int viewportHeight;
        boolean dirty = true;
        public void resize(int viewportWidth, int viewportHeight) {
            System.out.println("Old viewport width [" + this.viewportWidth + "] old viewport height [" + this.viewportHeight + "]");
            System.out.println("Viewport width [" + viewportWidth + "] viewport height [" + viewportHeight + "]");
            this.dirty = !(this.viewportWidth == viewportWidth && this.viewportHeight == viewportHeight);
            this.viewportWidth = viewportWidth;
            this.viewportHeight = viewportHeight;
        }
    }
}

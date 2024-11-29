package my.demo.widget;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;

import java.util.function.Supplier;


public class AdjustableContainer<T extends Actor> extends WidgetGroup {
    private boolean prefSizeInvalid;
    private float prefWidth;
    private float prefHeight;
    private float lastPrefWidth;
    private float lastPrefHeight;
    private Actor actor;
    private Supplier<Float> hRatio;
    private Supplier<Float> vRatio;

    private AdjustableContainer() {
        setTouchable(Touchable.childrenOnly);
        setTransform(false);
    }

    public AdjustableContainer(T actor, Supplier<Float> ratio) {
        this(actor, ratio, ratio);
    }

    public AdjustableContainer(T actor, Supplier<Float> hRatio, Supplier<Float> vRatio) {
        this();
        setActor(actor);
        this.hRatio = hRatio;
        this.vRatio = vRatio;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        prefSizeInvalid = true;
    }

    private void calcPrefSize() {
        prefSizeInvalid = false;
        if (vRatio != null) {
            prefWidth = hRatio.get();
        }
        if (hRatio != null) {
            prefHeight = vRatio.get();
        }
    }

    @Override
    public float getPrefWidth() {
        if (prefSizeInvalid) calcPrefSize();
        return prefWidth;
    }

    @Override
    public float getPrefHeight() {
        if (prefSizeInvalid) calcPrefSize();
        return prefHeight;
    }

    @Override
    public void layout() {
        if (actor == null) return;

        if (prefSizeInvalid) calcPrefSize();
        if (prefHeight != lastPrefHeight) {
            lastPrefHeight = prefHeight;
            invalidateHierarchy();
        }
        if (prefWidth != lastPrefWidth) {
            lastPrefWidth = prefWidth;
            invalidateHierarchy();
        }

        actor.setBounds(0, 0, getWidth(), getHeight());
        if (actor instanceof Layout) ((Layout) actor).validate();
    }

    public void setActor(T actor) {
        this.actor = actor;
        clearChildren();
        super.addActor(actor);
    }

    /** @deprecated Container may have only a single child.
     * @see #setActor(Actor) */
    @Deprecated
    public void addActor (Actor actor) {
        throw new UnsupportedOperationException("Use Container#setActor.");
    }

    /** @deprecated Container may have only a single child.
     * @see #setActor(Actor) */
    @Deprecated
    public void addActorAt (int index, Actor actor) {
        throw new UnsupportedOperationException("Use Container#setActor.");
    }

    /** @deprecated Container may have only a single child.
     * @see #setActor(Actor) */
    @Deprecated
    public void addActorBefore (Actor actorBefore, Actor actor) {
        throw new UnsupportedOperationException("Use Container#setActor.");
    }

    /** @deprecated Container may have only a single child.
     * @see #setActor(Actor) */
    @Deprecated
    public void addActorAfter (Actor actorAfter, Actor actor) {
        throw new UnsupportedOperationException("Use Container#setActor.");
    }

    public boolean removeActor (Actor actor) {
        if (actor == null) throw new IllegalArgumentException("actor cannot be null.");
        if (actor != this.actor) return false;
        setActor(null);
        return true;
    }

    public boolean removeActor (Actor actor, boolean unfocus) {
        if (actor == null) throw new IllegalArgumentException("actor cannot be null.");
        if (actor != this.actor) return false;
        this.actor = null;
        return super.removeActor(actor, unfocus);
    }

    public Actor removeActorAt (int index, boolean unfocus) {
        Actor actor = super.removeActorAt(index, unfocus);
        if (actor == this.actor) this.actor = null;
        return actor;
    }
}

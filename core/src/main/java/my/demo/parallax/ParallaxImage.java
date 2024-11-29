package my.demo.parallax;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

import java.util.Objects;

public class ParallaxImage extends Actor {

    private final Vector2 factor;

    private final Vector2 currentDelta = new Vector2();
    private final TiledDrawable drawable;

    public ParallaxImage(TextureRegion textureRegion, float factorX, float factorY) {
        this(new TiledDrawable(textureRegion), factorX, factorY);
    }

    public ParallaxImage(TextureRegionDrawable textureRegionDrawable, float factorX, float factorY) {
        this(new TiledDrawable(textureRegionDrawable), new Vector2(factorX, factorY));
    }

    public ParallaxImage(TiledDrawable drawable, Vector2 factor) {
        super();
        setBounds(0f, 0f, drawable.getMinWidth(), drawable.getMinHeight());
        setPosition(0f, 0f);
        this.drawable = drawable;
        this.factor = factor;
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        drawable.setScale(scaleXY);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        currentDelta.x = -(getWidgetCenterX() * factor.x);
        currentDelta.y = -(getWidgetCenterY() * factor.y);
    }

    private float getWidgetCenterX() {
        return getX() + getWidth() / 2;
    }
    private float getWidgetCenterY() {
        return getY() + getHeight() / 2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float targetWidth = getWidth() * getScaleX();
        float targetHeight = getHeight() * getScaleY();
        float startX = getWidgetCenterX() - getWidth() / 2;//camera.position.x - camera.viewportWidth / 2;
        float startY = getWidgetCenterY() - getHeight() / 2;//camera.position.y - camera.viewportHeight / 2;
        float maxX = getWidth() + startX;//camera.viewportWidth + startX;
        float maxY = getWidth() + startY;//camera.viewportHeight + startY;
        float dX = currentDelta.x % targetWidth;
        float dY = currentDelta.y % targetHeight;
        float x = startX + dX - targetWidth;
        float y = startY + dY - targetHeight;
        drawable.draw(batch, x, y, maxX - x, maxY - y);
    }
}

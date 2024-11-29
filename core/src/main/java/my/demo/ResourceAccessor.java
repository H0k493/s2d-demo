package my.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

public class ResourceAccessor implements Disposable {
    private Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));;
    private Drawable avatar = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("frame_2.png"))));
    private Drawable frame = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img.png"))));

    public Skin getSkin() {
        return skin;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public Drawable getFrame() {
        return frame;
    }

    @Override
    public void dispose() {
        skin.dispose();
    }
}

package my.demo.parallax;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParallaxLayer {
    Texture texture;
    float factor;
    Camera camera;

    public ParallaxLayer(Texture texture, float factor, Camera camera) {
       this.texture = texture;
       this.factor = factor;
       this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
       this.camera = camera;
    }

    public void render(SpriteBatch batch) {
        int xOffset = (int) (camera.position.x * factor);
        int yOffset = (int) (camera.position.y * factor);
        TextureRegion region = new TextureRegion(texture);
        region.setRegionX(xOffset % texture.getWidth());
        region.setRegionY(yOffset % texture.getHeight());
        region.setRegionWidth((int) camera.viewportWidth);
        region.setRegionHeight(texture.getHeight());
        batch.draw(region, camera.position.x - camera.viewportWidth/2, camera.position.y - camera.viewportHeight/2);
    }
}

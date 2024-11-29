package my.demo;

import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {
    private ResourceAccessor accessor = new ResourceAccessor();
    private InterfaceComposer composer = new InterfaceComposer(accessor);
    public InterfaceComposer getComposer() {
        return composer;
    }
}

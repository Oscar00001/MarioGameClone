package engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener keyInstance;
    private boolean keyPressed[] = new boolean [350];
    private KeyListener (){

    }



    public static KeyListener get() {
        if (KeyListener.keyInstance == null){
            KeyListener.keyInstance = new KeyListener();
        }
        return KeyListener.keyInstance;
    }
    /**
     *
     * @param window = the window in mem
     * @param key = the key presed
     * @param scanCode = the
     * @param action = is it pressed or released
     * @param mods =  maybe there are two buttons clicked
     */
    public static void keyCallback (long window, int key, int scanCode, int action, int mods){
        if (action == GLFW_PRESS){
            get().keyPressed[key] = true;
        }
        else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode){
        if(keyCode < get().keyPressed.length){
            return get().keyPressed[keyCode];
        }
        else {
            return false;
        }
    }

}

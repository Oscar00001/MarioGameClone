package engine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

//import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    int width, height;
    String title;
    private static Window window = null;
    private long glfwWindow; //memory adress where the window is in the mem space
    private float r,g,b,a;
    private boolean fadeToBlack = false;
    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario Game";
        this.r = 1;
        this.b = 1;
        this.g = 1;
        this.a = 1;
    }
    public static Window get() {
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run (){
        System.out.println("Hello " + Version.getVersion());
        init();
        loop();
        //free the mem
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //terminate GLFW and free error callbacks
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        //error callbacks
        GLFWErrorCallback.createPrint(System.err).set();
        //ini GLFW
        if (!glfwInit()){
            throw new IllegalStateException("Unable to init GLFW");

        }
        //defaul options for GLFW before it all gets loaded
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL, NULL);
        if(glfwWindow == NULL) {
            throw new IllegalStateException("Failed GLFW Window");
        }

        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallback);
        //make openGL context current
        glfwMakeContextCurrent(glfwWindow);
        //enable v-sync
        glfwSwapInterval(1);
        //make widow visible
        glfwShowWindow(glfwWindow);
        //create context
        GL.createCapabilities();
    }

    public void loop(){
        while (!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            glClearColor(this.r,this.g,this.b,this.a);
            //            glClearColor(1.0f,0.0f,0.0f,1.0);
//            glClear(GL_COLOR_BUFFER_BIT);
//
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(glfwWindow);

            if(fadeToBlack){
                this.r = Math.max(this.r - 0.01f,0);
                this.g = Math.max(this.g - 0.01f,0);
                this.b = Math.max (this.b - 0.01f,0);
//                this.a = Math.max(this.a - 0.1f,0);
                System.out.println("The r is ==" + r);
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                System.out.println("GFKJKJLSFSDKLJFH");
                fadeToBlack = true;
            }
        }
    }
}
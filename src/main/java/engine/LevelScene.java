package engine;

public class LevelScene extends Scene{
    public LevelScene(){
        System.out.println("This is the constructor of the Level Scene ");

        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }

    @Override
    public void update(float dt) {

    }
}

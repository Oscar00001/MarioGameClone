package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private String vertexSource;
    private String filepath;
    private String fragmentSource;

    public Shader(String filepath){
        this.filepath = filepath;

        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)"); //will only match #type fragment & #type vertex
            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index,endOfLine).trim();

            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\r\n",index);
            String secondPattern = source.substring(index,endOfLine).trim();

            if(firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            }
            else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            }
            else{
                throw new IOException("Unexpected token '" + firstPattern + " ' in '" + filepath + " '");
            }


            if(secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            }
            else if(secondPattern.equals("fragment")){
                fragmentSource = splitString[2];

            }
            else{
                throw new IOException("Unexpected token '" + firstPattern + " ' in '" + filepath + " '");
            }

        }
        catch(IOException e){
            e.printStackTrace();
            assert false: "Error could not ope file for shader: '" + filepath + "'";
        }
        System.out.println(vertexSource + "This will show you all of the vertexInfo");
        System.out.println(fragmentSource+ "This will show you all of the fragmentInfo");
    }

    /**
     * Complie and link shaders. Using file path when the object is created and String which holds information
     * about either vertex or fragment.
     */
    public void compile(){
        int vertexID, fragmentID;

        //Compile and link the shaders
        //load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass the shader source to the GPU
        glShaderSource(vertexID,vertexSource);
        glCompileShader(vertexID);
        ///check for the erros
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("Error:'" + filepath+"',\n \t  Vertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false: "";
        }
        //Compile and link the shaders
        //load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass the shader source to the GPU
        glShaderSource(fragmentID,fragmentSource);
        glCompileShader(fragmentID);
        ///check for the erros
        success = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH);
            System.out.println("Error:'" + filepath+"',\n \t  Fragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false: "";
        }


        //create program and link shader & check for error
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID,fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH);
            System.out.println("Error:'" + filepath+"',\n \t  Linking of shader  failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID,len));
            assert false: "";
        }
    }

    public void use(){
        glUseProgram(shaderProgramID); // Using it here (init)
    }

    public void detach(){
        glUseProgram(0); // freeing it here (init)

    }
}

package engine;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private String vertexShaderSrc ="#version 330 core\n" +
            "layout(location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "void main () {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos,1.0);\n" +
            "}";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID,fragmentID,shaderProgram;
    private float[] vertexArray = {
            //position               // color
            0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            0.5f,  0.5f, 0.0f ,      0.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            -0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };
    // Note: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    x        x

                    x        X
             */

            // we have to go counter clockwise


            2, 1, 0, // Top right triangle
//            0, 1, 3 // bottom left triangle
    };
    private int vaoID, vboID, eboID;

    public LevelEditorScene() {

    }

    @Override
    public void init(){
        //Compile and link the shaders
        //load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass the shader source to the GPU
        glShaderSource(vertexID,vertexShaderSrc);
        glCompileShader(vertexID);
        ///check for the erros
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("Error: 'defaultShader.glsl',\n \t  Vertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false: "";
        }
        //Compile and link the shaders
        //load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass the shader source to the GPU
        glShaderSource(fragmentID,fragmentShaderSrc);
        glCompileShader(fragmentID);
        ///check for the erros
        success = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH);
            System.out.println("Error: 'defaultShader.glsl',\n \t  Fragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false: "";
        }

        //create program and link shader & check for error
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertexID);
        glAttachShader(shaderProgram,fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgram,GL_INFO_LOG_LENGTH);
            System.out.println("Error: 'defaultShader.glsl',\n \t  Linking of shader  failed");
            System.out.println(glGetProgramInfoLog(shaderProgram,len));
            assert false: "";
        }
        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays(); // working with certain vaoId, so telling gl to make the vertex
        glBindVertexArray(vaoID);  // working with certain object, so tell gl everything below is for the vaoID

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);//buffer of the array length
        vertexBuffer.put(vertexArray).flip(); // put it in the correct orignation

        //Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID); // workjing withour buffer
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW); // working with array buffer, using specifc buffer for this ID, and this is static and we dont need to change it


        //=========================================Step 2=================================================

        //We will then create one of the Triangle here.
        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }


    @Override
    public void update(float dt) {
        //=========================================Step 3=================================================
        // Bind shader program
        glUseProgram(shaderProgram);
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}

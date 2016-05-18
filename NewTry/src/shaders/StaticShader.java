package shaders;

public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/shaders/fragmentShader";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader";
	
	public StaticShader(int vboID) {
		super(VERTEX_FILE, FRAGMENT_FILE, vboID);
	}

	@Override
	protected void bindAttributes(int vboID) {
		super.bindAttribute(vboID, "position");
	}

}

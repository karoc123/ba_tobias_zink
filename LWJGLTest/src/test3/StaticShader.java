package test3;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/test3/vertexShader";
	private static final String FRAGMENT_FILE = "src/test3/fragmentShader";
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		
	}


}

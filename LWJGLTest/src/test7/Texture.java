package test7;

import java.nio.ByteBuffer;

public class Texture {
	
	int TextureID;
	int width;
	int height;
	ByteBuffer buf;

	public Texture(int textureID, int width, int height, ByteBuffer buf) {
		super();
		TextureID = textureID;
		this.width = width;
		this.height = height;
		this.buf = buf;
	}

	public int getTextureID() {
		return TextureID;
	}

}

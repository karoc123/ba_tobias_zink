package de.oth.blocklib.textures;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

/**
 * Loads textures from png files
 * !!ONLY PNG AT THE MOMENT!!
 */
public class TextureLoader {
	private static IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);

	/**
	 * Makes a tex file from a image.
	 * !!ONLY PNG AT THE MOMENT!!
	 * @param filetype
	 * @param in
	 * @return a Texture object
	 */
	public static Texture getTexture(String filetype, InputStream in) {
		Texture tex;
		try {
			PNGDecoder decoder = null;
			try {
				decoder = new PNGDecoder(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Debug
			System.out.println("Texture loaded, " + " width: " + decoder.getWidth() + 
					" height: " + decoder.getHeight());

			ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			try {
				decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buf.flip();
			int id = createTextureID();
			tex = new Texture(id, decoder.getWidth(), decoder.getHeight(), buf);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tex;
	}

	private static int createTextureID() {
		GL11.glGenTextures(textureIDBuffer);
		return textureIDBuffer.get(0);
	}
}

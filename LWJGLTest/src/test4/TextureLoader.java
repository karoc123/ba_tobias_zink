package test4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureLoader {

	
	public static Texture getTexture(String filetype, FileInputStream in) {
		Texture tex;
		try {
		   PNGDecoder decoder = null;
		try {
			decoder = new PNGDecoder(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		   System.out.println("width="+decoder.getWidth());
		   System.out.println("height="+decoder.getHeight());
		 
		   ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
		   try {
			decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   buf.flip();
		   tex = new Texture(1, decoder.getWidth(), decoder.getHeight(), buf);
		   GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		   
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
}

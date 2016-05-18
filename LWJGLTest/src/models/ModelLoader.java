package models;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import helper.Maths;
import renderer.Loader;

/**
 * TODO: merge with OBJ Loader or delete class
 *
 */
public class ModelLoader {

	private static Loader loader;
	
	public ModelLoader(Loader loader){
		ModelLoader.loader = loader;
	}

//	/**
//	 * Calculate Normals (for light) from triangle out of three points
//	 * @param p1
//	 * @param p2
//	 * @param p3
//	 * @return Normal Vector
//	 */
//	private static Vector3f calculateSurfaceNormal(Vector3f p1, Vector3f p2, Vector3f p3){
//		Vector3f u = Maths.vectorSubtraction(p2, p1);
//		Vector3f v = Maths.vectorSubtraction(p3, p1);
//		
//		Vector3f normal = new Vector3f(0,0,0);
//		normal.x = (u.y*v.z) - (u.z*v.y);
//		normal.y = (u.z*v.x) - (u.x*v.z);
//		normal.z = (u.x*v.y) - (u.y*v.x);
//		
//		return normal;
//	}
//	
//	private static float[] vector3fListToFloatArray(List<Vector3f> list){
//		List<Float> floatList = new ArrayList<Float>();
//		for (Vector3f point : list) {
//			floatList.add(point.x);
//			floatList.add(point.y);
//			floatList.add(point.z);
//		}
//		float[] floatArray = new float[floatList.size()];
//		
//		int i = 0;
//		for (Float f : floatList) {
//		    floatArray[i++] = (f != null ? f : Float.NaN);
//		}
//		
//		return floatArray;
//	}
//	
//	private static float[] generateNormals(float[] vertices, int[] indices){
//		Vector3f p1 = null, p2 = null, p3;
//		int counter = 0;
//		List<Vector3f> normals = new ArrayList<Vector3f>();
//		
//		for (int point : indices) {
//			if(counter > 1){
//				normals.add(calculateSurfaceNormal(p1, p2, getPointFromVertices(vertices, point)));
//				counter = 0;
//			} else if (counter == 1){
//				p2 = getPointFromVertices(vertices, point);
//				counter++;
//			} else{
//				p1 = getPointFromVertices(vertices, point);
//				counter++;
//			}
//		}
//		return vector3fListToFloatArray(normals);
//	}
//	
//	private static Vector3f getPointFromVertices(float[] vertices, int indice){
//		Vector3f point = new Vector3f(0,0,0);
//		point.x = vertices[indice*3];
//		point.y = vertices[(indice*3)+1];
//		point.z = vertices[(indice*3)+2];
//		
//		return point;
//		
//	}
	
	public RawModel loadCube(){
		float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		
		float[] textureCoords = {
				
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0

				
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,
				
				4,5,7,
				7,5,6,
				
				8,9,11,
				11,9,10,
				
				12,13,15,
				15,13,14,
				
				16,17,19,
				19,17,18,
				
				20,21,23,
				23,21,22

		};
		
		//float[] normals = generateNormals(vertices, indices);
		float[] normals = {
				0.0000f, -1.0000f, 0.0000f,
				0.0000f, 1.0000f, 0.0000f,
				1.0000f, 0.0000f, 0.0000f,
				-0.0000f, -0.0000f, 1.0000f,
				-1.0000f, -0.0000f, -0.0000f,
				0.0000f, 0.0000f, -1.0000f
		};
		
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
}

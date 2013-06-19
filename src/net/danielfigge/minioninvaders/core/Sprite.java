package net.danielfigge.minioninvaders.core;

import net.danielfigge.minioninvaders.util.ResourceManager;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;


public class Sprite {
	private Texture texture;
	private String filename;
	private int width, height;
	private float rotation;
	private float colorR, colorG, colorB;

	public Sprite(GameWindow window, String ref) {
		texture = ResourceManager.get().getTexture(ref);
		filename = ref.substring(ref.lastIndexOf('/') + 1);
		width = texture.getImageWidth();
		height = texture.getImageHeight();
		rotation = 0;
		colorR = colorG = colorB = 1;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void rotate(float rotation) {
		this.rotation += rotation;
	}

	public void setColor(float r, float g, float b) {
		colorR = r;
		colorG = g;
		colorB = b;
	}

	public String getFilename() {
		return filename;
	}

	public int getWidth() {
		return texture.getImageWidth();
	}

	public int getHeight() {
		return texture.getImageHeight();
	}

	public void draw(int x, int y) {
		draw(x, y, width, height);
	}

	public void draw(int x, int y, int w, int h) {
		GL11.glPushMatrix();

		texture.bind();

		GL11.glTranslatef(x, y, 0);
		if (rotation != 0) {
			GL11.glTranslatef(getWidth() / 2, getHeight() / 2, 0);
			GL11.glRotatef(rotation, 0f, 0f, 1f);
			GL11.glTranslatef(-getWidth() / 2, -getHeight() / 2, 0);
		}
		GL11.glColor3f(colorR, colorG, colorB);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(0, texture.getHeight());
			GL11.glVertex2f(0, h);
			GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
			GL11.glVertex2f(w, h);
			GL11.glTexCoord2f(texture.getWidth(), 0);
			GL11.glVertex2f(w, 0);
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}
}
package net.danielfigge.minioninvaders.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import net.danielfigge.minioninvaders.util.PathTools;
import net.danielfigge.minioninvaders.util.ResourceManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.GradientEffect;
import org.newdawn.slick.font.effects.OutlineEffect;
import org.newdawn.slick.util.ResourceLoader;

import de.matthiasmann.twl.utils.PNGDecoder;

public class GameWindow {

	public static final int ALIGN_LEFT = 1, ALIGN_CENTER = 2, ALIGN_RIGHT = 3;

	private GameWindowCallbackIf callback;

	private boolean gameRunning = true;

	private int width, height;

	public static UnicodeFont FONT_MAIN = null, FONT_TITLE = null, FONT_SMALL = null, FONT_MENU = null;
	public static UnicodeFont[] FONT_MAIN_DIFFICULTY;

	private int fps;

	private String title = null;

	public GameWindow() {
		System.setProperty("org.lwjgl.librarypath", PathTools.getNativeDirectory());
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@SuppressWarnings("unchecked")
	private void initFont() {
		try {
			InputStream inputStream;

			inputStream = ResourceLoader.getResourceAsStream("resources/fonts/JandaManateeSolid.ttf");
			Font mainFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			FONT_MAIN = new UnicodeFont(mainFont, 20, false, false);
			FONT_MAIN.getEffects().add(new ColorEffect(Color.white));
			FONT_MAIN.addAsciiGlyphs();
			FONT_MAIN.loadGlyphs();
			inputStream.close();
			FONT_MAIN_DIFFICULTY = new UnicodeFont[4];
			FONT_MAIN_DIFFICULTY[0] = new UnicodeFont(mainFont, 20, false, false);
			FONT_MAIN_DIFFICULTY[0].getEffects().add(new ColorEffect(new Color(0xff, 0xbf, 0x00)));
			FONT_MAIN_DIFFICULTY[0].addAsciiGlyphs();
			FONT_MAIN_DIFFICULTY[0].loadGlyphs();
			FONT_MAIN_DIFFICULTY[1] = new UnicodeFont(mainFont, 20, false, false);
			FONT_MAIN_DIFFICULTY[1].getEffects().add(new ColorEffect(new Color(0xff, 0x80, 0x00)));
			FONT_MAIN_DIFFICULTY[1].addAsciiGlyphs();
			FONT_MAIN_DIFFICULTY[1].loadGlyphs();
			FONT_MAIN_DIFFICULTY[2] = new UnicodeFont(mainFont, 20, false, false);
			FONT_MAIN_DIFFICULTY[2].getEffects().add(new ColorEffect(new Color(0xff, 0x40, 0x00)));
			FONT_MAIN_DIFFICULTY[2].addAsciiGlyphs();
			FONT_MAIN_DIFFICULTY[2].loadGlyphs();
			FONT_MAIN_DIFFICULTY[3] = new UnicodeFont(mainFont, 20, false, false);
			FONT_MAIN_DIFFICULTY[3].getEffects().add(new ColorEffect(new Color(0xff, 0x00, 0x00)));
			FONT_MAIN_DIFFICULTY[3].addAsciiGlyphs();
			FONT_MAIN_DIFFICULTY[3].loadGlyphs();

			Font smallFont = new Font("Arial", Font.PLAIN, 12);
			FONT_SMALL = new UnicodeFont(smallFont, 12, false, false);
			FONT_SMALL.getEffects().add(new ColorEffect(Color.white));
			FONT_SMALL.addAsciiGlyphs();
			FONT_SMALL.loadGlyphs();

			inputStream = ResourceLoader.getResourceAsStream("resources/fonts/kberry.ttf");
			Font titleFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			FONT_TITLE = new UnicodeFont(titleFont, 72, true, false);
			FONT_TITLE.getEffects().add(new ColorEffect(Color.white));
			FONT_TITLE.getEffects().add(new GradientEffect(Color.white, Color.black, 1f));
			FONT_TITLE.getEffects().add(new OutlineEffect(2, new Color(0x10, 0x10, 0x10)));
			FONT_TITLE.addAsciiGlyphs();
			FONT_TITLE.loadGlyphs();
			inputStream.close();

			inputStream = ResourceLoader.getResourceAsStream("resources/fonts/Australian Sunset.ttf");
			Font menueFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			FONT_MENU = new UnicodeFont(menueFont, 24, false, false);
			FONT_MENU.getEffects().add(new ColorEffect(Color.white));
			FONT_MENU.addAsciiGlyphs();
			FONT_MENU.loadGlyphs();
			inputStream.close();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void drawText(UnicodeFont font, float x, float y, String text) {
		drawText(font, x, y, text, ALIGN_LEFT);
	}

	public void drawText(UnicodeFont font, float x, float y, String text, int align) {
		String[] lines = text.split("\n");
		int heightOffset = 0;
		for (String line : lines) {
			if (line.equals("")) {
				heightOffset += font.getHeight("W");
			}
			switch (align) {
			case ALIGN_LEFT:
				font.drawString(x, y + heightOffset, line);
				break;
			case ALIGN_CENTER:
				font.drawString(x - (font.getWidth(line) / 2), y + heightOffset, line);
				break;
			case ALIGN_RIGHT:
				font.drawString(x - font.getWidth(line), y + heightOffset, line);
				break;
			}
			heightOffset += font.getHeight(line);
		}
	}

	public void setTitle() {
		setTitle(title);
	}

	public void setTitle(String title) {
		if (displayReady()) {
			Display.setTitle(title);
			this.title = null;
		} else {
			this.title = title;
		}
	}

	public boolean displayReady() {
		return Display.isCreated();
	}

	public void setResolution(int x, int y) {
		width = x;
		height = y;
	}

	public void setDisplayMode(int width, int height, boolean fullscreen) {
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height)
				&& (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];
					System.out.println(current.toString());

					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
								&& (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public void setIcons(String[] icons) {
		ByteBuffer[] bb = new ByteBuffer[icons.length];
		try {
			for (int i = 0; i < icons.length; i++) {
				bb[i] = loadIcon(ResourceManager.get().getResource(icons[i]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Display.setIcon(bb);
	}

	private static ByteBuffer loadIcon(URL url) throws IOException {
		InputStream is = url.openStream();
		try {
			PNGDecoder decoder = new PNGDecoder(is);
			ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
			decoder.decode(bb, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			bb.flip();
			return bb;
		} finally {
			is.close();
		}
	}

	public void setVsync(boolean vsync) {
		Display.setVSyncEnabled(vsync);
	}

	public void setFramerate(int fps) {
		this.fps = fps;
	}

	public void startRendering() {
		try {
			setDisplayMode(width, height, false);
			Display.setInitialBackground(0x00, 0x00, 0x00);
			Display.create();
			setTitle();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);

			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			GL11.glClearDepth(1);

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glViewport(0, 0, width, height);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, width, height, 0, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			initFont();

			if (callback != null) {
				callback.initialise();
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
			setGameOver(true);
		}
		gameLoop();
	}

	public void setGameWindowCallback(GameWindowCallbackIf callback) {
		this.callback = callback;
	}

	public boolean isKeyPressed(int keyCode) {
		return Keyboard.isKeyDown(keyCode);
	}

	public void setGameOver(boolean gameOver) {
		gameRunning = !gameOver;
	}

	private void gameLoop() {
		while (gameRunning) {
			// clear screen
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			// let subsystem paint
			if (callback != null) {
				callback.frameRendering();
			}

			// update window contents
			Display.update();
			Display.sync(fps);

			if (Display.isCloseRequested()) {
				setGameOver(true);
			}
		}
		callback.windowClosed();
	}

	public void cleanUp() {
		if (FONT_MAIN != null) {
			FONT_MAIN.destroy();
		}
		if (FONT_TITLE != null) {
			FONT_TITLE.destroy();
		}
		if (FONT_SMALL != null) {
			FONT_SMALL.destroy();
		}
		if (FONT_MENU != null) {
			FONT_MENU.destroy();
		}
		if (FONT_MAIN_DIFFICULTY != null) {
			for (UnicodeFont font : FONT_MAIN_DIFFICULTY) {
				font.destroy();
			}
		}
		Display.destroy();
	}
}
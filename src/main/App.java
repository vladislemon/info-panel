package main;

import com.sun.management.OperatingSystemMXBean;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * slimon
 * 21.06.2014
 */
public class App {

    public static boolean isRunning = true;

    public static TrueTypeFont fontSmall, fontMedium, fontLarge;

    public static OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public static void main(String[] args) throws IOException, FontFormatException {
        //System.setProperty("org.lwjgl.librarypath", "D:/Development/java/libraries/lwjgl-2.9.0/native/windows");
        int desktopWidth = Display.getDesktopDisplayMode().getWidth();
        //int desktopHeight = Display.getDesktopDisplayMode().getHeight();

        int width = 150;
        int height = 125;

        int borderH = 100;
        int borderV = 0;

        Render.setWindowPos(desktopWidth - width - borderH, borderV);
        Render.createDisplay(width, height);
        Render.initOrtho();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/a_AvanteBs.ttf")));
        //char[] additionalSimbols = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЧчШшЩщЬьЫыЪъЭэЮюЯя".toCharArray();
        fontSmall = new TrueTypeFont(new Font("a_AvanteBs", Font.BOLD, 11), true);
        fontMedium = new TrueTypeFont(new Font("a_AvanteBs", Font.PLAIN, 32), true);
        fontLarge = new TrueTypeFont(new Font("a_AvanteBs", Font.PLAIN, 64), true);

        while(isRunning) {
            Render.render();
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && Keyboard.isKeyDown(Keyboard.KEY_E)) {
                isRunning = false;
            }
            if(Mouse.isButtonDown(0) && Render.isButtonCollide()) {
                isRunning = false;
            }
        }
        fontSmall.destroy();
        fontMedium.destroy();
        fontLarge.destroy();
    }
}

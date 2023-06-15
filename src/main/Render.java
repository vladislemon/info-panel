package main;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.lwjgl.opengl.GL11.*;

/**
 * slimon
 * 21.06.2014
 */
public class Render {

    public static int buttonSize = 10;

    public static void createDisplay(int width, int height) {
        DisplayMode displayMode = new DisplayMode(width, height);
        try {
            Display.setDisplayMode(displayMode);
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            Display.setTitle("Info Panel");
            Display.setIcon(getIcon());
            Display.create();
        } catch (LWJGLException e) {
            System.exit(-1);
        }
    }

    public static void setWindowPos(int x, int y) {
        Display.setLocation(x, y);
    }

    /*public static void initPerspective() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(GL_3D, (float) Display.getWidth() / (float) Display.getHeight(), 0.1F, 10000F);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }*/

    public static void initOrtho() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        glClearColor(0.1F, 0.3F, 0.7F, 1.0F);
    }

    /*public static void initRenderForGui() {
        initOrtho();
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
    }*/

    public static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glColor3f(0.8F, 0.8F, 0.8F);
        //Game.fontMedium.drawString(500, 50, "FFF ggg", 1F, 1F, TrueTypeFont.ALIGN_RIGHT);
        glEnable(GL_TEXTURE_2D);
        App.fontMedium.drawString(Display.getWidth()/2, 10, getTime(), 1.0F, 1.0F, TrueTypeFont.ALIGN_CENTER);
        App.fontSmall.drawString(Display.getWidth()/2, 45, getDate(), 1.0F, 1.0F, TrueTypeFont.ALIGN_CENTER);
        App.fontSmall.drawString(Display.getWidth()/2, 65, "CPU load: " + getCPULoad() + "%",
                1.0F, 1.0F, TrueTypeFont.ALIGN_CENTER);
        App.fontSmall.drawString(Display.getWidth()/2, 85, "Memory load:\n" + getMemoryLoad(),
                1.0F, 1.0F, TrueTypeFont.ALIGN_CENTER);

        glDisable(GL_TEXTURE_2D);
        glColor3f(0.3F, 0.3F, 0.3F);
        glRecti(Display.getWidth() - buttonSize, 0, Display.getWidth(), buttonSize);
        glColor3f(0.8F, 0.1F, 0.1F);
        glBegin(GL_LINES);
        glVertex2i(Display.getWidth() - buttonSize, 0);
        glVertex2i(Display.getWidth(), buttonSize);
        glVertex2i(Display.getWidth(), 0);
        glVertex2i(Display.getWidth() - buttonSize, buttonSize);
        glEnd();

        Display.update();
        Display.sync(10);
    }

    public static boolean isButtonCollide() {
        return Mouse.getX() >= Display.getWidth() - buttonSize && Mouse.getX() <= Display.getWidth() &&
                Display.getHeight() - Mouse.getY() >= 0 && Display.getHeight() - Mouse.getY() <= buttonSize;
    }

    private static ByteBuffer[] getIcon() {
        return new ByteBuffer[] {
                //getImageData("res/icon/icon128.png"),
                getImageData("res/icon/icon32.png"),
                getImageData("res/icon/icon16.png")
        };
    }

    public static ByteBuffer getImageData(String path) {
        try {
            File file = new File(path);
            if(!file.exists())
                return BufferUtils.createByteBuffer(1);
            BufferedImage image = ImageIO.read(file);

            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

            ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
                    int pixel = pixels[y * image.getWidth() + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                    buffer.put((byte) (pixel & 0xFF));               // Blue component
                    buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
                }
            }

            buffer.flip();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BufferUtils.createByteBuffer(1);
    }

    public static String getCPULoad() {
        return Integer.toString((int)Math.round(App.osBean.getSystemCpuLoad() * 100));
    }

    public static String getMemoryLoad() {
        long total = App.osBean.getTotalPhysicalMemorySize() / 1024 / 1024;
        long used = total - App.osBean.getFreePhysicalMemorySize() / 1024 / 1024;
        return Long.toString(used) + " of " + Long.toString(total) + " (" +
                Integer.toString(Math.round((float) used / (float) total * 100)) + "%)";
    }

    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YY");
        Date date = new Date();
        return dateFormat.format(date);
    }
}

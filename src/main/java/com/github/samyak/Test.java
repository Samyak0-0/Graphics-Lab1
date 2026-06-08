package com.github.samyak;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Test {

    private long window;
    private static final int WIDTH = 900, HEIGHT = 400;

    public static void main(String[] args) {
        new Test().run();
    }

    public void run() {
        init();
        loop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to init GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "OpenGL – Name with GL_POLYGON", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create window");

        glfwSetKeyCallback(window, (win, key, sc, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(win, true);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pw = stack.mallocInt(1), ph = stack.mallocInt(1);
            glfwGetWindowSize(window, pw, ph);
            GLFWVidMode vm = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vm.width() - pw.get(0)) / 2, (vm.height() - ph.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glColor3f(0.0f, 0.0f, 0.0f);
            drawName();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void drawName() {
        float[] cx = { -0.70f, -0.42f, -0.14f, 0.14f, 0.42f, 0.70f };
        drawLetterS(cx[0]);
        drawLetterA(cx[1]);
        drawLetterM(cx[2]);
        drawLetterY(cx[3]);
        drawLetterA(cx[4]);
        drawLetterK(cx[5]);
    }

    private void polygon(float... pts) {
        glBegin(GL_POLYGON);
        for (int i = 0; i < pts.length; i += 2)
            glVertex2f(pts[i], pts[i + 1]);
        glEnd();
    }

    private void drawLetterS(float cx) {
        float w = 0.09f, th = 0.05f;

        polygon(cx - w, 0.40f - th, cx + w, 0.40f - th, cx + w, 0.40f, cx - w, 0.40f);
        polygon(cx - w, 0.00f, cx - w + th, 0.00f, cx - w + th, 0.40f, cx - w, 0.40f);
        polygon(cx - w, -th / 2f, cx + w, -th / 2f, cx + w, th / 2f, cx - w, th / 2f);
        polygon(cx + w - th, -0.40f, cx + w, -0.40f, cx + w, 0.00f, cx + w - th, 0.00f);
        polygon(cx - w, -0.40f, cx + w, -0.40f, cx + w, -0.40f + th, cx - w, -0.40f + th);
    }

    private void drawLetterA(float cx) {
        float w = 0.09f, th = 0.05f;

        polygon(cx - w, -0.40f, cx - w + th, -0.40f, cx + th * 0.3f, 0.40f, cx - th * 0.3f, 0.40f);
        polygon(cx + w - th, -0.40f, cx + w, -0.40f, cx + th * 0.3f, 0.40f, cx - th * 0.3f, 0.40f);
        polygon(cx - w * 0.6f, -0.05f, cx + w * 0.6f, -0.05f, cx + w * 0.6f, -0.05f + th, cx - w * 0.6f, -0.05f + th);
    }

    private void drawLetterM(float cx) {
        float w = 0.10f, th = 0.045f;
        float diagTh = 0.03f;

        polygon(cx - w, -0.40f, cx - w + th, -0.40f, cx - w + th, 0.40f, cx - w, 0.40f);
        polygon(cx + w - th, -0.40f, cx + w, -0.40f, cx + w, 0.40f, cx + w - th, 0.40f);
        polygon(
                cx - w + th, 0.40f,
                cx - diagTh / 2f, -0.05f,
                cx + diagTh / 2f, -0.05f,
                cx - w + th + diagTh, 0.40f);
        polygon(
                cx + w - th - diagTh, 0.40f,
                cx - diagTh / 2f, -0.05f,
                cx + diagTh / 2f, -0.05f,
                cx + w - th, 0.40f);
    }

    private void drawLetterY(float cx) {
        float w = 0.09f, th = 0.05f;

        polygon(cx - th / 2f, -0.40f, cx + th / 2f, -0.40f, cx + th / 2f, 0.00f, cx - th / 2f, 0.00f);
        polygon(cx - w, 0.40f, cx - w + th, 0.40f, cx + th / 2f, 0.00f, cx - th / 2f, 0.00f);
        polygon(cx + w - th, 0.40f, cx + w, 0.40f, cx + th / 2f, 0.00f, cx - th / 2f, 0.00f);
    }

    private void drawLetterK(float cx) {
        float w = 0.09f, th = 0.05f;

        polygon(cx - w, -0.40f, cx - w + th, -0.40f, cx - w + th, 0.40f, cx - w, 0.40f);
        polygon(cx - w + th, -0.05f, cx - w + th, 0.05f, cx + w, 0.40f, cx + w - th, 0.40f);
        polygon(cx - w + th, 0.05f, cx - w + th, -0.05f, cx + w, -0.40f, cx + w - th, -0.40f);
    }
}

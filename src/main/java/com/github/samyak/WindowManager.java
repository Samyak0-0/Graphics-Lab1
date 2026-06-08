package com.github.samyak;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    private final String title;
    private int width, height;

    private long window;

    private boolean resize, vSync;

    private final Matrix4f projectionMatrix;

    public WindowManager(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        // GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
        // GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        boolean maximized = false;

        if (width == 0 || height == 0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to Create GLFW window");

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });

        if (maximized)
            GLFW.glfwMaximizeWindow(window);
        else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);

        if (isvSync())
            GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        // GL11.glEnable(GL11.GL_DEPTH_TEST);
        // GL11.glEnable(GL11.GL_STENCIL_TEST);
        // GL11.glEnable(GL11.GL_CULL_FACE);
        // GL11.glCullFace(GL11.GL_BACK);

    }

    public void update() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glColor3f(0.0f, 0.0f, 0.0f);
        drawName();

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
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
        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i < pts.length; i += 2)
            GL11.glVertex2f(pts[i], pts[i + 1]);
        GL11.glEnd();
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

    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean isResize() {
        return resize;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int heightj) {
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
}

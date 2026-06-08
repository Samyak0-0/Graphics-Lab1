package com.github.samyak;

public class Main {

    public static void main(String[] args) {
        WindowManager window = new WindowManager("Lab - 1 Name", 900, 500, false);

        window.init();

        while (!window.windowShouldClose()) {
            window.update();
        }

        window.cleanup();
    }
}

// can be run in maven using:
// mvn exec:java -Dexec.mainClass="com.github.samyak.Main"

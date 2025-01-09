package org.personal.jasper;

public class Manager {
    private Window window;

    public void startApplication() {
        window = new Window();
        window.setVisible(true);
    }
}

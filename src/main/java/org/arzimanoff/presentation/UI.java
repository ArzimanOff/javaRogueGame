package org.arzimanoff.presentation;

import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class UI {
    private final Screen screen;

    public UI() throws IOException {
        Presenter presenter = Presenter.getInstance();
        this.screen = presenter.getScreen();

        // Начало работы с экраном
        screen.startScreen();
        Drawer.getInstance(screen);
    }

    public Drawer getDrawer() {
        return Drawer.getInstance(screen);
    }

    public Screen getScreen() {
        return screen;
    }
}

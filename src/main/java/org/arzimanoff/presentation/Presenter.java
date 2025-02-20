package org.arzimanoff.presentation;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import java.io.IOException;

public class Presenter {
    private static Presenter instance; // Единственный экземпляр класса

    private final Terminal terminalFactory;
    private final Screen screen;

    public static synchronized Presenter getInstance() throws IOException {
        if (instance == null) {
            instance = new Presenter(); // Ленивая инициализация
        }
        return instance;
    }

    private Presenter() throws IOException {
        // Приватный конструктор для предотвращения создания новых экземпляров
        this.terminalFactory = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(156, 49))
                .setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode)
                .createTerminal();

        this.screen = new TerminalScreen(this.terminalFactory);
        this.screen.startScreen();
    }

    public Screen getScreen() {
        return this.screen;
    }
}

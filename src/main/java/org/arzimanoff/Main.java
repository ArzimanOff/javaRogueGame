package org.arzimanoff;


import org.arzimanoff.controller.GameController;
import org.arzimanoff.data.InfoProcessor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameController gameController = new GameController();
        gameController.run();

//        InfoProcessor.cleanInfoFile();
    }
}

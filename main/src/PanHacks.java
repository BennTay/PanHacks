import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.Game;

public class PanHacks {

        private Game game;
        private Logger logger;

        public static void main(String[] args) {
            Logger logger = Logger.getLogger(PanHacks.class.getName());
            Game game = new Game(logger);
            game.init();
            game.setVisible(true);
            game.start();
        }


}

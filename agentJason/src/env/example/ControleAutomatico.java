package example;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ControleAutomatico {
    private static final int MOVE_STEP = 10;
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    public void atualizarControle(Rectangle player, Circle item) {
        double dx = item.getTranslateX() - player.getTranslateX();
        double dy = item.getTranslateY() - player.getTranslateY();

        double newX = player.getTranslateX();
        double newY = player.getTranslateY();

        if (Math.abs(dx) > Math.abs(dy)) {
            newX += (dx > 0) ? MOVE_STEP : -MOVE_STEP;
        } else {
            newY += (dy > 0) ? MOVE_STEP : -MOVE_STEP;
        }

        if (!podeMoverPara(newX, newY, player)) {
            if (Math.abs(dx) > Math.abs(dy)) {
                newX = player.getTranslateX();
                newY += (dy > 0) ? MOVE_STEP : -MOVE_STEP;
            } else {
                newX += (dx > 0) ? MOVE_STEP : -MOVE_STEP;
                newY = player.getTranslateY();
            }
        }

        if (podeMoverPara(newX, newY, player)) {
            player.setTranslateX(newX);
            player.setTranslateY(newY);
        }
    }

    private boolean podeMoverPara(double x, double y, Rectangle player) {
        return x >= 0 && x <= SCREEN_WIDTH - player.getWidth() && y >= 0 && y <= SCREEN_HEIGHT - player.getHeight();
    }
}

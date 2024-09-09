package example;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ControleAutomatico {
    private static final int MOVE_STEP = 10;

    public void atualizarControle(Rectangle player, Circle item) {
        double dx = item.getTranslateX() - player.getTranslateX();
        double dy = item.getTranslateY() - player.getTranslateY();

        double newX = player.getTranslateX();
        double newY = player.getTranslateY();

        boolean b = Math.abs(dx) > Math.abs(dy);
        if (b) {
            if (dx > 0) {
                newX += MOVE_STEP;
            } else {
                newX -= MOVE_STEP;
            }
        } else {
            if (dy > 0) {
                newY += MOVE_STEP;
            } else {
                newY -= MOVE_STEP;
            }
        }

        if (podeMoverPara(newX, newY, player)) {
            player.setTranslateX(newX);
            player.setTranslateY(newY);
        } else {
            if (b) {
                newX = player.getTranslateX() - (dx > 0 ? MOVE_STEP : -MOVE_STEP);
            } else {
                newY = player.getTranslateY() - (dy > 0 ? MOVE_STEP : -MOVE_STEP);
            }

            if (podeMoverPara(newX, newY, player)) {
                player.setTranslateX(newX);
                player.setTranslateY(newY);
            }
        }
    }

    private boolean podeMoverPara(double x, double y, Rectangle player) {
        return x >= 0 && x <= 800 - player.getWidth() && y >= 0 && y <= 600 - player.getHeight();
    }
}

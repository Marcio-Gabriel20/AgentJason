package example;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import jason.asSyntax.*;
import jason.environment.*;
import jason.asSyntax.parser.*;
import java.util.Random;
import java.util.logging.Logger;

public class Env extends Environment {
    private Logger logger = Logger.getLogger("gameTesteJason."+Env.class.getName());
    private Stage primaryStage;
    private Pane root;
    private Rectangle player;
    private Circle item;
    private int score = 0;
    private boolean isAutoControl = false;
    private ControleAutomatico controleAutomatico = new ControleAutomatico();

    @Override
    public void init(String[] args) {
        super.init(args);
        try {
            addPercept("bob", ASSyntax.parseLiteral("myName(bob)"));
            addPercept("alice", ASSyntax.parseLiteral("myName(alice)"));

            System.out.println(containsPercept("bob", ASSyntax.parseLiteral("myName(bob)")));
            System.out.println(containsPercept("alice", ASSyntax.parseLiteral("myName(alice)")));

            Platform.startup(() -> startJavaFX());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void startJavaFX() {
        primaryStage = new Stage();
        root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        player = new Rectangle(50, 50, Color.BLUE);
        player.setTranslateX(375);
        player.setTranslateY(540);

        item = new Circle(15, Color.RED);

        posicionarItem();

        root.getChildren().addAll(player, item);

        scene.setOnKeyPressed(event -> {
            if (!isAutoControl) {
                if (event.getCode() == KeyCode.LEFT && player.getTranslateX() > 0) {
                    player.setTranslateX(player.getTranslateX() - 10);
                }
                if (event.getCode() == KeyCode.RIGHT && player.getTranslateX() < 800 - 50) {
                    player.setTranslateX(player.getTranslateX() + 10);
                }
                if (event.getCode() == KeyCode.UP && player.getTranslateY() > 0) {
                    player.setTranslateY(player.getTranslateY() - 10);
                }
                if (event.getCode() == KeyCode.DOWN && player.getTranslateY() < 600 - 50) {
                    player.setTranslateY(player.getTranslateY() + 10);
                }

                verificarColisao();
            }

            if (event.getCode() == KeyCode.A) {
                isAutoControl = !isAutoControl;
                System.out.println("Controle automático: " + isAutoControl);
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.017), event -> {
            if (isAutoControl) {
                controleAutomatico.atualizarControle(player, item);
            }
            verificarColisao();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setTitle("Jogo com Controle Automático");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void verificarColisao() {
        if (player.getBoundsInParent().intersects(item.getBoundsInParent())) {
            score++;
            System.out.println("Score: " + score);
            posicionarItem();
        }
    }

    private void posicionarItem() {
        Random random = new Random();
        double x = random.nextDouble() * (800 - item.getRadius() * 2);
        double y = random.nextDouble() * (600 - item.getRadius() * 2);
        item.setTranslateX(x);
        item.setTranslateY(y);
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("executing: "+action+", for agent: " + agName);
        informAgsEnvironmentChanged();
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        if (primaryStage != null) {
            Platform.runLater(() -> primaryStage.close());
        }
    }
}

class ControleAutomatico {
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

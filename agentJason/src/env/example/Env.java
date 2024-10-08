package example;

import jason.asSemantics.Agent;
import jason.runtime.RuntimeServices;
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
//            addPercept("bob", ASSyntax.parseLiteral("myName(bob)"));
//            addPercept("alice", ASSyntax.parseLiteral("myName(alice)"));

            System.out.println(containsPercept("bob", ASSyntax.parseLiteral("myName(bob)")));
            System.out.println(containsPercept("alice", ASSyntax.parseLiteral("myName(alice)")));

//            addPercept("bob", ASSyntax.parseLiteral("playerPositionX(0)"));
//            addPercept("bob", ASSyntax.parseLiteral("playerPositionY(0)"));

//            addPercept("bob", ASSyntax.parseLiteral("playerPositionX("+ player.getTranslateX() + ")"));
//            addPercept("bob", ASSyntax.parseLiteral("playerPositionY("+ player.getTranslateY() + ")"));

            Platform.startup(this::startJavaFX);
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
                    try {
                        removePercept("bob", ASSyntax.parseLiteral("playerPositionX(" + (player.getTranslateX() + 10) + ")"));
                        addPercept("bob", ASSyntax.parseLiteral("playerPositionX(" + player.getTranslateX() + ")"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (event.getCode() == KeyCode.RIGHT && player.getTranslateX() < 800 - 50) {
                    player.setTranslateX(player.getTranslateX() + 10);
                    try {
                        removePercept("bob", ASSyntax.parseLiteral("playerPositionX(" + (player.getTranslateX() - 10) + ")"));
                        addPercept("bob", ASSyntax.parseLiteral("playerPositionX(" + player.getTranslateX() + ")"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (event.getCode() == KeyCode.UP && player.getTranslateY() > 0) {
                    player.setTranslateY(player.getTranslateY() - 10);
                    try {
                        removePercept("bob", ASSyntax.parseLiteral("playerPositionY(" + (player.getTranslateY() + 10) + ")"));
                        addPercept("bob", ASSyntax.parseLiteral("playerPositionY(" + player.getTranslateY() + ")"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (event.getCode() == KeyCode.DOWN && player.getTranslateY() < 600 - 50) {
                    player.setTranslateY(player.getTranslateY() + 10);
                    try {
                        removePercept("bob", ASSyntax.parseLiteral("playerPositionY(" + (player.getTranslateY() - 10) + ")"));
                        addPercept("bob", ASSyntax.parseLiteral("playerPositionY(" + player.getTranslateY() + ")"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    verificarColisao();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            if (event.getCode() == KeyCode.A) {
                isAutoControl = !isAutoControl;
                if(isAutoControl) {
                    System.out.println("Controle automático: Ativado");
                } else {
                    System.out.println("Controle automático: Desativado");
                }
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.020), event -> {
            if (isAutoControl) {
                controleAutomatico.atualizarControle(player, item);
            }
            try {
                verificarColisao();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setTitle("ChonGame");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void verificarColisao() throws ParseException {
        if (player.getBoundsInParent().intersects(item.getBoundsInParent())) {
            score++;
            removePercept("bob", ASSyntax.parseLiteral("myScore("+ (score-1) +")"));
            addPercept("bob", ASSyntax.parseLiteral("myScore("+ score +")"));

//            System.out.println("Score: " + consultPercepts("bob").getFirst());
//            System.out.println(containsPercept("bob", ASSyntax.parseLiteral("myScore(1)").addSource(ASSyntax.parseTerm("percept"))));
//            System.out.println(containsPercept("bob", ASSyntax.parseLiteral("myName(bob)").addSource(ASSyntax.parseTerm("self"))));
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
        if (agName.equals("bob") && action.getFunctor().equals("moveRight")) {
            logger.info("Mexi no ambiente!");
            logger.info(String.valueOf(action.getFunctor().equals("hi")));
            while (agName.equals("bob") && action.getFunctor().equals("moveRight")) {
                if(player.getTranslateX() != 0.0) {
                    player.setTranslateX(player.getTranslateX() + 10);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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

package com.example.asteroidsshootergame;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AsteroidsApplicationFX extends Application {
    public static int WIDTH = 300;
    public static int HEIGHT = 200;

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        Text text = new Text(10, 20, "Points: 0");

        pane.setPrefSize(WIDTH, HEIGHT);

        Ship ship = new Ship(WIDTH/2 ,HEIGHT/2);
        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i <5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH/3), rnd.nextInt(HEIGHT/3));
            asteroids.add(asteroid);
        }
        List<Projectile> projectiles = new ArrayList<>();

        pane.getChildren().add(ship.getCharacter());
        pane.getChildren().add(text);

        AtomicInteger points = new AtomicInteger();

        // loop through
        for (int i = 0; i < asteroids.size(); i++) {
            pane.getChildren().add(asteroids.get(i).getCharacter());
            asteroids.get(i).turnRight();
            asteroids.get(i).turnRight();
            asteroids.get(i).accelerate();
            asteroids.get(i).accelerate();
        }

        Scene scene = new Scene(pane);

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();


        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);

        });

        Point2D movement = new Point2D(1, 0);


        new AnimationTimer() {
            @Override
            public void handle(long now) {


                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 3) {
                    // we shoot
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                    pane.getChildren().add(projectile.getCharacter());
                }



                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());


                asteroids.forEach( asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });


                List<Projectile> projectilesToRemove = projectiles.stream().filter(projectile -> {
                    List<Asteroid> collisions = asteroids.stream()
                            .filter(asteroid -> asteroid.collide(projectile))
                            .collect(Collectors.toList());

                    if(collisions.isEmpty()) {
                        return false;
                    }

                    collisions.stream().forEach(collided -> {
                        text.setText("Points: " + points.addAndGet(1000));
                        asteroids.remove(collided);
                        pane.getChildren().remove(collided.getCharacter());
                    });

                    return true;
                }).collect(Collectors.toList());

                projectilesToRemove.forEach(projectile -> {
                    pane.getChildren().remove(projectile.getCharacter());
                    projectiles.remove(projectile);
                });

                if(Math.random() < 0.005) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }



            }
        }.start();


        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch(AsteroidsApplicationFX.class);
    }
}

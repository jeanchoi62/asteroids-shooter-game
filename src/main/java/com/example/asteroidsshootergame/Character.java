package com.example.asteroidsshootergame;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {
    private Polygon character;
    private Point2D movement;

    private Boolean b = true;

    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.movement = new Point2D(0, 0);
    }

    public Polygon getCharacter() {
        return character;
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }

    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());

        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + AsteroidsApplicationFX.WIDTH);
        }

        if (this.character.getTranslateX() > AsteroidsApplicationFX.WIDTH) {
            this.character.setTranslateX(this.character.getTranslateX() % AsteroidsApplicationFX.WIDTH);
        }

        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + AsteroidsApplicationFX.HEIGHT);
        }

        if (this.character.getTranslateY() > AsteroidsApplicationFX.HEIGHT) {
            this.character.setTranslateY(this.character.getTranslateY() % AsteroidsApplicationFX.HEIGHT);
        }
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.05;
        changeY *= 0.05;

        this.movement = this.movement.add(changeX, changeY);
    }

    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public Point2D getMovement() {
        return this.movement;
    }

    public void setMovement(Point2D p) {
        this.movement = p;
    }

    public void setAlive(Boolean input) {
        this.b = input;
    }

    public boolean isAlive() {
        return this.b;
    }


}
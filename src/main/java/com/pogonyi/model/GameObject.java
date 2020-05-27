package com.pogonyi.model;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class GameObject implements Renderable {

    protected Point2D position;
    protected  double width;
    protected double height;
    protected boolean alive;

    public GameObject() {
        this.position = new Point2D(0,0);
        alive = true;
    }

    public GameObject(Point2D position) {
        this.position = position;
        alive = true;
    }

    public GameObject(Point2D position, double size) {
        this.position = position;
        this.width = size;
        this.height = size;
        alive = true;
    }

    public GameObject(double width, double height) {
        this.position = new Point2D(0,0);
        this.width = width;
        this.height = height;
        alive = true;
    }

    public GameObject(Point2D position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    @Override
    public void render(GraphicsContext graphicsContext) {
        if(alive) {
            graphicsContext.fillRect(position.getX() + 1, position.getY() + 1, width -2, height - 2);
        }
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(position.getX(), position.getY(), width, height);
    }

    public boolean intersect(GameObject other) {
        return this.getBoundary().intersects(other.getBoundary());
    }

    public void setRandomPosition(int width, int height) {
        Random random = new Random();
        setPosition(new Point2D(random.nextInt((width) / 25) * 25,
                random.nextInt((height) / 25) * 25));
    }
}

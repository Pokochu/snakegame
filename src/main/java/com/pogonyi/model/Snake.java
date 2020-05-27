package com.pogonyi.model;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class Snake implements Renderable {

    private LinkedList<MovingGameObject> body = new LinkedList<>();
    private int bodySize;
    private MovingGameObject tail;
    private Direction direction = Direction.RIGHT;

    public Snake(Point2D head, Point2D tail, int bodySize) {
        this.bodySize = bodySize;
        body.add(new MovingGameObject(head, bodySize));
        body.add(new MovingGameObject(tail, bodySize));
    }

    public MovingGameObject getHead() {
        return body.getFirst();
    }

    public MovingGameObject getNeck() {
        return body.get(1);
    }

    public MovingGameObject getBody(int index) {
        return body.get(index);
    }

    public MovingGameObject getTail() {
        return body.getLast();
    }

    public int getLength() {
        return body.size();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void move() {
        tail = body.pollLast();

        switch (direction) {
            case UP: {
                Point2D newPos = getHead().getPosition().subtract(0, bodySize);
                body.addFirst(new MovingGameObject(newPos, bodySize, direction));
                break;
            }
            case DOWN: {
                Point2D newPos = getHead().getPosition().add(0, bodySize);
                body.addFirst(new MovingGameObject(newPos, bodySize, direction));
                break;
            }
            case LEFT: {
                Point2D newPos = getHead().getPosition().subtract(bodySize, 0);
                body.addFirst(new MovingGameObject(newPos, bodySize, direction));
                break;
            }
            case RIGHT: {
                Point2D newPos = getHead().getPosition().add(bodySize, 0);
                body.addFirst(new MovingGameObject(newPos, bodySize, direction));
                break;
            }
        }
    }

    public void grow() {
        body.add(new MovingGameObject());
    }

    public boolean intersect(GameObject other) {
        for (int i = 0; i < getLength(); i++) {
            if (other.intersect(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean collide() {
        for (int i = 1; i < getLength(); i++) {
            if (getHead().getPosition().equals(getBody(i).getPosition())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(GraphicsContext graphicsContext) {
        ImageView head = new ImageView(getClass().getResource("/modelImages/head.png").toString());
        switch (direction) {
            case RIGHT: {
                head.setRotate(-90);
                break;
            }
            case DOWN: {
                head.setRotate(0);
                break;
            }
            case UP: {
                head.setRotate(180);
                break;
            }
            case LEFT: {
                head.setRotate(90);
                break;
            }
        }
        graphicsContext.drawImage(head.snapshot(new SnapshotParameters(), null),
                getHead().getPosition().getX() + 1,
                getHead().getPosition().getY() + 1,
                23,
                23);
        graphicsContext.drawImage(new ImageView("modelImages/body.png").getImage(),
                getNeck().getPosition().getX() + 1,
                getNeck().getPosition().getY() + 1,
                23,
                23);

        if (tail != null) {
            graphicsContext.setFill(Color.BLACK);
            tail.render(graphicsContext);
        }
    }
}

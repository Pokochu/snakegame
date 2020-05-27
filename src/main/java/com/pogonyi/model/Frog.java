package com.pogonyi.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Frog extends GameObject {

    public Frog(double width, double height) {
        super(width, height);
    }

    public Frog(Point2D position, double width, double height) {
        super(position, width, height);
    }

    @Override
    public void render(GraphicsContext context) {
        Image image = new ImageView("modelImages/brekk.png").getImage();
        context.drawImage(image, position.getX(), position.getY());
    }
}

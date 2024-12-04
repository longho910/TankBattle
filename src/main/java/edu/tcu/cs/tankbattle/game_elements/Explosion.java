package edu.tcu.cs.tankbattle.game_elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Explosion {
    private double x, y;
    private boolean live = true;
    private int step = 0;

    // Load explosion images
    private static final Image[] imgs = {
            new Image(Explosion.class.getResource("/images/1.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/2.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/3.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/4.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/5.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/6.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/7.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/8.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/9.gif").toExternalForm()),
            new Image(Explosion.class.getResource("/images/10.gif").toExternalForm())
    };

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isLive() {
        return live;
    }

    public void draw(GraphicsContext gc) {
        if (!live) {
            return;
        }

        if (step == imgs.length) {
            live = false;
            return;
        }

        gc.drawImage(imgs[step], x, y);
        step++;
    }
}

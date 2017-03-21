package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    // Asteroides
    int numAsteroids;
    private ArrayList<Asteroid> asteroids;

    // Objecte Random
    Random r;

    public ScrollHandler() {

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();

        // Comencem amb 3 asteroids
        numAsteroids = 5;

        // Creem l'ArrayList
        asteroids = new ArrayList<Asteroid>();

        // Definim una mida aleatòria entre el mínim i el màxim
        float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;

        // Afegim el primer Asteroid a l'Array i al grup
        Asteroid asteroid = new Asteroid(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED, -50 + r.nextInt(100), true);
        asteroids.add(asteroid);
        addActor(asteroid);

        // Des del segon fins l'últim asteroide
        for (int i = 1; i < numAsteroids; i++) {
            // Creem la mida al·leatòria
            newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
            // Afegim l'asteroid.
            asteroid = new Asteroid(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED, -50 + r.nextInt(100), true);
            // Afegim l'asteroide a l'ArrayList
            asteroids.add(asteroid);
            // Afegim l'asteroide al grup d'actors
            addActor(asteroid);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());

        }

        for (int i = 0; i < asteroids.size(); i++) {

            Asteroid asteroid = asteroids.get(i);
            if (asteroid.isLeftOfScreen()) {
                if (i == 0) {
                    asteroid.reset(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP);
                } else {
                    asteroid.reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
                }
            }
        }
    }

    public boolean collides(Spacecraft nau) {

        // Comprovem les col·lisions entre cada asteroid i la nau
        for (Asteroid asteroid : asteroids) {
            if (asteroid.collides(nau) && asteroid.isContact()) {
                return true;
            }
        }
        return false;
    }


    public void reset() {

        // Posem el primer asteroid fora de la pantalla per la dreta
        asteroids.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'asteroids.
        for (int i = 1; i < asteroids.size(); i++) {
            asteroids.get(i).reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);

        }
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public int getNumAsteroids() {
        return numAsteroids;
    }

    public void setNumAsteroids(int numAsteroids) {
        this.numAsteroids = numAsteroids;
    }

    public static class Spacecraft extends Actor {

        // Distintes posicions de la spacecraft, recta, pujant i baixant
        public static final int SPACECRAFT_STRAIGHT = 0;
        public static final int SPACECRAFT_UP = 1;
        public static final int SPACECRAFT_DOWN = 2;

        // Paràmetres de la spacecraft
        private Vector2 position;
        private float velocityX, velocityY;
        private int width, height;
        private int direction;
        private Stage stage;
        private ScrollHandler scrollHandler;

        private Rectangle collisionRect;

        public Spacecraft(float x, float y, int width, int height, Stage stage) {

            // Inicialitzem els arguments segons la crida del constructor
            this.width = width;
            this.height = height;
            position = new Vector2(x, y);
            this.stage = stage;

            // Inicialitzem la spacecraft a l'estat normal
            direction = SPACECRAFT_STRAIGHT;

            // Creem el rectangle de col·lisions
            collisionRect = new Rectangle();

            // Per a la gestio de hit
            setBounds(position.x, position.y, width, height);
            setTouchable(Touchable.enabled);
        }

        public void act(float delta) {

            // Movem la spacecraft depenent de la direcció controlant que no surti de la pantalla
            switch (direction) {
                case SPACECRAFT_UP:
                    if (this.position.y - Settings.SPACECRAFT_VELOCITY * delta >= 0) {
                        this.position.y -= Settings.SPACECRAFT_VELOCITY * delta;
                    }
                    break;
                case SPACECRAFT_DOWN:
                    if (this.position.y + height + Settings.SPACECRAFT_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                        this.position.y += Settings.SPACECRAFT_VELOCITY * delta;
                    }
                    break;
                case SPACECRAFT_STRAIGHT:
                    break;
            }

            collisionRect.set(position.x, position.y + 3, width, 10);
            setBounds(position.x, position.y, width, height);

        }

        // Getters dels atributs principals
        public float getX() {
            return position.x;
        }

        public float getY() {
            return position.y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        // Canviem la direcció de la spacecraft: Puja
        public void goUp() {
            direction = SPACECRAFT_UP;
        }

        // Canviem la direcció de la spacecraft: Baixa
        public void goDown() {
            direction = SPACECRAFT_DOWN;
        }

        // Posem la spacecraft al seu estat original
        public void goStraight() {

            direction = SPACECRAFT_STRAIGHT;
        }

        public void pause()

        {
            velocityX = 0;
            velocityY = 0;
            direction = SPACECRAFT_STRAIGHT;
        }
        // Obtenim el TextureRegion depenent de la posició de la spacecraft
        public TextureRegion getSpacecraftTexture() {

            switch (direction) {

                case SPACECRAFT_STRAIGHT:
                    return AssetManager.spacecraft;
                case SPACECRAFT_UP:
                    return AssetManager.spacecraftUp;
                case SPACECRAFT_DOWN:
                    return AssetManager.spacecraftDown;
                default:
                    return AssetManager.spacecraft;
            }
        }

        public void reset() {

            // La posem a la posició inicial i a l'estat normal
            position.x = Settings.SPACECRAFT_STARTX;
            position.y = Settings.SPACECRAFT_STARTY;
            direction = SPACECRAFT_STRAIGHT;
            collisionRect = new Rectangle();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.draw(getSpacecraftTexture(), position.x, position.y, width, height);
        }

        public Rectangle getCollisionRect() {
            return collisionRect;
        }

        public void shoot(ScrollHandler scrollHandler) {
            for (Actor actor : stage.getActors()) {
                if(actor.getName() != null && actor.getName().equalsIgnoreCase("spacecraft")){
                    stage.addActor(new Disparo(actor.getX()+actor.getWidth(), actor.getY()+actor.getHeight()/2, 22, 20, scrollHandler));
//                    AssetManager.disparo.play();
                    break;
                }
            }
        }
    }
}
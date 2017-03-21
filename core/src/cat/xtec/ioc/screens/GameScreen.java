package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Set;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Asteroid;
import cat.xtec.ioc.objects.Marcador;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.objects.Scrollable;
import cat.xtec.ioc.utils.Settings;

public class GameScreen implements Screen {

    // Per controlar el gameover
    Boolean gameOver = false;

    //Marcador
    private int puntuacion = 0;

    // Objectes necessaris
    private Stage stage = new Stage();
    private ScrollHandler.Spacecraft spacecraft;
   // private Background bg;
    private ScrollHandler scrollHandler;

    public GameScreen() {

    }

    public enum GameState {

        READY, RUNNING, GAMEOVER

    }

    private GameState estado;

    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch = stage.getBatch();

    private GlyphLayout marcador;
    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    private GlyphLayout textLayout;

    public GameScreen(Batch batch, Viewport viewport, String dificil) {
        batch = stage.getBatch();
        // Iniciem la música
        AssetManager.music.play();
        if (dificil.equals("facil")) {
            Settings.ASTEROID_GAP += 10;
            Settings.SPACECRAFT_VELOCITY = 100;
            Settings.ASTEROID_SPEED += 90;
        } else if (dificil.equals("medio")) {
            Settings.ASTEROID_SPEED -= 50;
            Settings.SPACECRAFT_VELOCITY += 10;
            Settings.ASTEROID_GAP -= 20;
        } else if (dificil.equals("dificil")) {
            Settings.ASTEROID_GAP -= 50;
            Settings.ASTEROID_SPEED -= 40;
        }
            scrollHandler = new ScrollHandler();
            shapeRenderer = new ShapeRenderer();

            OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
            camera.setToOrtho(true);

            // Creem el viewport amb les mateixes dimensions que la càmera
            StretchViewport viewp = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

            // Creem l'stage i assginem el viewp
            stage = new Stage(viewp);

            batch = stage.getBatch();

            // Creem la nau i la resta d'objectes
            spacecraft = new ScrollHandler.Spacecraft(Settings.SPACECRAFT_STARTX, Settings.SPACECRAFT_STARTY, Settings.SPACECRAFT_WIDTH, Settings.SPACECRAFT_HEIGHT, stage);
            // bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, 0);

            // Afegim els actors a l'stage
            stage.addActor(scrollHandler);

            // stage.addActor(bg);
            stage.addActor(spacecraft);

            // Donem nom a l'Actor
            spacecraft.setName("spacecraft");


            marcador = new GlyphLayout();
            puntuacion = 0;
            estado = GameState.READY;


            // Assignem com a gestor d'entrada la classe InputHandler
            Gdx.input.setInputProcessor(new InputHandler(this));


            // Iniciem el GlyphLayout
            textLayout = new GlyphLayout();
            textLayout.setText(AssetManager.font, "GameOver");


            // Assignem com a gestor d'entrada la classe InputHandler
            Gdx.input.setInputProcessor(new InputHandler(this));



    }
    private void drawElements() {

        //Recollim les propietats del Batch de l'Stage
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Pintem el fons de negre per evitar el "flickering"
        Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Inicialitzem el shaperenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Definim el color (verd)
        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        // Pintem la nau
        shapeRenderer.rect(spacecraft.getX(), spacecraft.getY(), spacecraft.getWidth(), spacecraft.getHeight());


        // Recollim tots els Asteroid
        ArrayList<Asteroid> asteroids = scrollHandler.getAsteroids();
        Asteroid asteroid;

        for (int i = 0; i < asteroids.size(); i++) {

            asteroid = asteroids.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getWidth() / 2, asteroid.getWidth() / 2);
        }
        shapeRenderer.end();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // Dibuixem i actualizem tots els actors de l'stage
        stage.draw();

        switch (estado) {

            case GAMEOVER:
//                updateGameOver(delta);
//                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case READY:
//                updateReady();
//                break;

        }

        stage.act(delta);

        if (!gameOver) {
            if (scrollHandler.collides(spacecraft)) {
                // Si hi ha hagut col·lisió: Reproduïm l'explosió
                AssetManager.explosionSound.play();
                stage.getRoot().findActor("spacecraft").remove();
                gameOver = true;
            }

        } else {
            batch.begin();
            // Si hi ha hagut col·lisió: Reproduïm l'explosió
            batch.draw(AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);
            AssetManager.font.draw(batch, textLayout, Settings.GAME_WIDTH / 2 - textLayout.width / 2, Settings.GAME_HEIGHT / 2 - textLayout.height / 2);
            // Convert integer into String

        }
        updateRunning(delta);
    }

        private void updateRunning(float delta) {
            stage.act(delta);
            batch.begin();
            marcador.setText(AssetManager.font, "Puntuacion : " + puntuacion++);
            AssetManager.fontPuntuacio.draw(batch, marcador, 1, 2);

            if (scrollHandler.collides(spacecraft)) {
                // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
                String mostraPuntuacio = Integer.toString(puntuacion);
                puntuacion = 0;
                AssetManager.explosionSound.play();
                stage.getRoot().findActor("spacecraft").remove();
                textLayout.setText(AssetManager.font, "Game Over\n" +
                        "Puntuacion final : " + mostraPuntuacio);
                estado = GameState.GAMEOVER;
            }
            batch.end();
        }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public ScrollHandler.Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public Stage getStage() {
        return stage;
    }

 /*   public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }
    */
}
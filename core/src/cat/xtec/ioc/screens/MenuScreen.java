package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import cat.xtec.ioc.SpaceRace;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;


public class MenuScreen implements Screen {

    private Stage stage;
    private SpaceRace game;
    private boolean touch;
    private Label.LabelStyle textStyle;
    private TextButton facil, medio, dificil;
    private TextButton.TextButtonStyle textButtonStyle;

    public MenuScreen(SpaceRace game) {



        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = AssetManager.font;
        this.game = game;

        touch = false;

        OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        camera.setToOrtho(true);

        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        stage = new Stage(viewport);

        stage.addActor(new Image(AssetManager.background));

        Image spacecraft = new Image(AssetManager.spacecraft);
        float y = Settings.GAME_HEIGHT / 3;
        spacecraft.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.moveTo(0 - spacecraft.getWidth(), y), Actions.moveTo(Settings.GAME_WIDTH, y, 5))));

        stage.addActor(spacecraft);

        facil = new TextButton("Facil", textButtonStyle);
        medio = new TextButton("Medio", textButtonStyle);
        dificil = new TextButton("Dificil", textButtonStyle);

        Container containerFacil = new Container(facil);
        containerFacil.setTransform(true);
        containerFacil.center();
        containerFacil.setPosition(Settings.GAME_WIDTH/2+10, 30);
        Container containerMedio = new Container(medio);
        containerMedio.setTransform(true);
        containerMedio.center();
        containerMedio.setPosition(Settings.GAME_WIDTH/2+10, 60);
        Container containerDificil = new Container(dificil);
        containerDificil.setTransform(true);
        containerDificil.center();
        containerDificil.setPosition(Settings.GAME_WIDTH/2+10, 90);
        containerFacil.addAction(Actions.touchable(Touchable.enabled));
        stage.addActor(containerFacil);
        stage.addActor(containerMedio);
        stage.addActor(containerDificil);

        facil.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.game.setScreen(new GameScreen(MenuScreen.this.stage.getBatch(), MenuScreen.this.stage.getViewport(), "facil"));
                dispose();
            }
        });

        medio.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.game.setScreen(new GameScreen(MenuScreen.this.stage.getBatch(), MenuScreen.this.stage.getViewport(), "medio"));
                dispose();
            }
        });

        dificil.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.game.setScreen(new GameScreen(MenuScreen.this.stage.getBatch(), MenuScreen.this.stage.getViewport(), "dificil"));
                dispose();
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    public MenuScreen() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        stage.draw();
        stage.act(delta);

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
}

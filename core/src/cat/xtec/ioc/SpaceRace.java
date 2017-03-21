package cat.xtec.ioc;

import com.badlogic.gdx.Game;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.screens.GameScreen;

public class SpaceRace extends Game {

    @Override
    public void create() {

        // A l'iniciar el joc carreguem els recursos
        AssetManager.load();
        // I definim la pantalla principal com a la pantalla
        setScreen(new GameScreen());

    }

    // Cridem per descartar els recursos carregats.
    @Override
    public void dispose() {
        super.dispose();
        AssetManager.dispose();
    }
}
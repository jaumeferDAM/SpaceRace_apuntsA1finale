package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by ALUMNEDAM on 07/03/2017.
 */

public class Marcador {
    private int score;
    private String Puntos;
    FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
    BitmapFont font = new BitmapFont(fontFile,true);
    public void create() {
        score = 0;
        Puntos = "score: 0";
        font = new BitmapFont();
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public FileHandle getFontFile() {
        return fontFile;
    }

    public void setFontFile(FileHandle fontFile) {
        this.fontFile = fontFile;
    }

    public String getPuntos() {
        return Puntos;
    }

    public void setPuntos(String puntos) {
        Puntos = puntos;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

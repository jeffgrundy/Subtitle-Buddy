package io.github.vincemann.subtitleBuddy.gui;

import io.github.vincemann.subtitleBuddy.gui.pages.OptionsPage;
import io.github.vincemann.subtitleBuddy.gui.pages.SettingsPage;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.MovieSrtDisplayer;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.SettingsSrtDisplayer;
import io.github.vincemann.subtitleBuddy.gui.stages.OptionsStageController;
import io.github.vincemann.subtitleBuddy.gui.stages.SettingsStageController;
import io.github.vincemann.subtitleBuddy.gui.stages.stageController.AbstractStageController;
import io.github.vincemann.subtitleBuddy.srt.SrtFont;
import io.github.vincemann.subtitleBuddy.srt.font.fontManager.SrtFontManager;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeoutException;


public class OptionsTest extends GuiTest {



    private SettingsPage settingsPage;
    private SrtFontManager srtFontManager;

    @Override
    public void setUpClass() throws Exception {
        super.setUpClass();
        this.settingsPage = new SettingsPage(this);
        this.srtFontManager= getApplicationInjector().getInstance(SrtFontManager.class);
    }


    @Test
    public void testNoColorChangeOnSettingsText() throws TimeoutException {
        focusStage(SettingsStageController.class);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        refreshGui();
        focusStage(OptionsStageController.class);
        Color selectedColor = optionsPage.selectRandomColor(SettingsSrtDisplayer.DEFAULT_FONT_COLOR);
        Assert.assertNotEquals(SettingsSrtDisplayer.DEFAULT_FONT_COLOR, selectedColor);
        refreshGui();
        //es darf keinen change gegeben haben
        Assert.assertEquals(SettingsSrtDisplayer.DEFAULT_FONT_COLOR, findSrtDisplayer(SettingsSrtDisplayer.class).getFontColor());
    }


    @Test
    public void testShowOptions() throws TimeoutException {
        settingsPage.openOptionsWindow();
        refreshGui();
        Assert.assertTrue(isStageShowing(OptionsStageController.class));
        Assert.assertTrue(isStageShowing(SettingsStageController.class));
        AbstractStageController optionsStageController = findStageController(OptionsStageController.class);
        Assert.assertTrue(optionsStageController.getStage().isFocused());
    }

    @Test
    public void testChangeColorOnMovieMode() throws TimeoutException {
        focusStage(SettingsStageController.class);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(SettingsStageController.class);
        settingsPage.switchToMovieMode();
        findSrtDisplayer(MovieSrtDisplayer.class).setFontColor(Color.RED);
        refreshGui();
        focusStage(OptionsStageController.class);
        Color selectedColor = optionsPage.selectRandomColor(Color.RED);
        Assert.assertNotEquals(Color.RED, selectedColor);
        refreshGui();
        Assert.assertEquals(selectedColor, findSrtDisplayer(MovieSrtDisplayer.class).getFontColor());
    }

    @Test
    public void testChangeFontInSettingsMode() throws TimeoutException {
        focusStage(SettingsStageController.class);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(OptionsStageController.class);
        SrtFont firstFont = srtFontManager.loadDefaultFont();
        findSrtDisplayer(SettingsSrtDisplayer.class).setCurrentFont(firstFont);
        SrtFont currFont = findSrtDisplayer(SettingsSrtDisplayer.class).getCurrentFont();
        Assert.assertEquals(currFont, firstFont);
        SrtFont newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        refreshGui();
        Assert.assertEquals(findSrtDisplayer(SettingsSrtDisplayer.class).getCurrentFont(), newFont);
    }

    @Test
    public void testChangeFontInMovieMode() throws TimeoutException {
        focusStage(SettingsStageController.class);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(OptionsStageController.class);
        SrtFont firstFont = srtFontManager.loadDefaultFont();
        focusStage(SettingsStageController.class);
        settingsPage.switchToMovieMode();
        findSrtDisplayer(MovieSrtDisplayer.class).setCurrentFont(firstFont);
        SrtFont currFont = findSrtDisplayer(MovieSrtDisplayer.class).getCurrentFont();
        Assert.assertEquals(currFont, firstFont);
        focusStage(OptionsStageController.class);
        SrtFont newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        refreshGui();
        Assert.assertEquals(findSrtDisplayer(MovieSrtDisplayer.class).getCurrentFont(), newFont);
    }
}

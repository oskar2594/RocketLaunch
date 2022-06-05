/**
 * 
 * MIT LICENSE
 * 
 * Copyright 2022 Philip Gilde & Oskar Stanschus
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Philip Gilde & Oskar Stanschus
 * 
 */
package de.pogs.rl.utils;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class AssetHelper {
    private AssetManager assetManager;

    private HashMap<String, AssetDescriptor> images = new HashMap<String, AssetDescriptor>();
    private HashMap<String, AssetDescriptor> sounds = new HashMap<String, AssetDescriptor>();
    private HashMap<String, FreeTypeFontGenerator> fonts =
            new HashMap<String, FreeTypeFontGenerator>();

    private FileHandle baseDir;

    public AssetHelper() {
        assetManager = new AssetManager();
        baseDir = Gdx.files.getFileHandle("assets/", FileType.Local);
    }

    public void loadAll() {
        System.out.println(baseDir.list().length);
        for (FileHandle entry : baseDir.list()) {
            System.out.println(entry.name());
            if (entry.isDirectory()) {
                switch (entry.name()) {
                    case "images":
                        loadImages(entry.list());
                        break;
                    case "sounds":
                        loadSounds(entry.list());
                        break;
                    case "fonts":
                        loadFonts(entry.list());
                        break;
                    default:
                        break;
                }
            }
        }
    }


    private void loadFonts(FileHandle[] list) {
        for (FileHandle file : list) {
            System.out.println(file.name());
            if (file.extension().equalsIgnoreCase("ttf")) {
                FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
                fonts.put(file.nameWithoutExtension().toLowerCase(), generator);
            }
        }
    }

    public BitmapFont getFont(String name, int size) {
        try {
            FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                    new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            BitmapFont font = fonts.get(name).generateFont(parameter);
            return font;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private void loadSounds(FileHandle[] list) {
        for (FileHandle file : list) {
            System.out.println(file.path());
            AssetDescriptor asset = new AssetDescriptor<>(file.path(), Sound.class);
            sounds.put(file.nameWithoutExtension(), asset);
            assetManager.load(asset);
        }
    }

    public Sound getSound(String name) {
        try {
            return (Sound) this.assetManager.get(sounds.get(name));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private void loadImages(FileHandle[] list) {
        for (FileHandle file : list) {
            System.out.println(file.path());
            AssetDescriptor asset = new AssetDescriptor(file.path(), Texture.class);
            images.put(file.nameWithoutExtension(), asset);
            assetManager.load(asset);
        }
    }

    public Texture getImage(String name) {
        try {
            return (Texture) this.assetManager.get(images.get(name));
        } catch (Exception e) {
            return null;
        }
    }


    public AssetManager getAssetManager() {
        return assetManager;
    }

}
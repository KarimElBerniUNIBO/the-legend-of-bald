package com.thelegendofbald.item;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameItem {

    protected int x;
    protected int y;
    protected String name;
    protected BufferedImage sprite;

    public GameItem(int x,int y,String name,String imagePath){
        
        this.x = x;
        this.y = y;
        this.name = name;
        this.ImageUpload(imagePath);

    }

    private void ImageUpload(String path){
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream(path));
        } catch(IOException e ){
            e.printStackTrace();
        }
    }

}

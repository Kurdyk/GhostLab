package utils;

import javafx.scene.image.Image;

/**
 * The type Image crop.
 */
public class ImageCrop {
    private final Image image;
    private final int width;
    private final int height;
    private final int cropStartX;
    private final int cropStartY;
    private final int cropWidth;
    private final int cropHeight;

    /**
     * Instantiates a new Image crop.
     *
     * @param image      the image
     * @param width      the width
     * @param height     the height
     * @param cropStartX the crop start x
     * @param cropStartY the crop start y
     * @param cropWidth  the crop width
     * @param cropHeight the crop height
     */
    public ImageCrop(Image image, int width, int height, int cropStartX, int cropStartY, int cropWidth, int cropHeight) {
        System.out.println("ImageCrop() called with: image = [" + image + "], width = [" + width + "], height = [" + height + "], cropStartX = [" + cropStartX + "], cropStartY = [" + cropStartY + "], cropWidth = [" + cropWidth + "], cropHeight = [" + cropHeight + "]");
        this.image = image;
        this.width = width;
        this.height = height;
        this.cropStartX = cropStartX;
        this.cropStartY = cropStartY;
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets crop start x.
     *
     * @return the crop start x
     */
    public int getCropStartX() {
        return cropStartX;
    }

    /**
     * Gets crop start y.
     *
     * @return the crop start y
     */
    public int getCropStartY() {
        return cropStartY;
    }

    /**
     * Gets crop width.
     *
     * @return the crop width
     */
    public int getCropWidth() {
        return cropWidth;
    }

    /**
     * Gets crop height.
     *
     * @return the crop height
     */
    public int getCropHeight() {
        return cropHeight;
    }
}

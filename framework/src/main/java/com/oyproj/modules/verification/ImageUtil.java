package com.oyproj.modules.verification;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author oywq3000
 * @since 2026-01-08
 */
public class ImageUtil {
    /**
     * @param oriImage 原图
     * @param templateImage 模板图
     * @param newImage 新抠出来的小图
     * @param x 随机扣取坐标X
     * @param y 随机扣取坐标Y
     */
    public static void cutByTemplate(BufferedImage oriImage,
                                     BufferedImage templateImage,
                                     BufferedImage newImage,
                                     int x,int y){
        //临时数组遍历用于高斯模糊周边的像素值
        int[][] matrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();

        //模板图像宽度
        for(int i = 0;i<xLength;i++){
            for (int j = 0; j < yLength; j++) {
                //如果模板图像当前像素点不是透明色，copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i,j);
                if(rgb<0){
                    newImage.setRGB(i,j,oriImage.getRGB(x+i,y+j));
                    //被扣图区域使用高斯模糊
                    readPixel(oriImage,x+i,y+j,values);
                    fillMatrix(matrix,values);
                    oriImage.setRGB(x+i,y+j,avgMatrix(matrix));
                }
                //防止数组越界判断
                if (i == (xLength - 1) || j == (yLength - 1)) {
                    continue;
                }
                int rightRgb = templateImage.getRGB(i+1,j);
                int downRgb = templateImage.getRGB(i,j+1);
                //描边处理，取带像素和无像素的界点，判断该点是不是临界轮廓点，如果是设置该坐标像素是白色
                boolean isBorder = (
                        (rgb>=0&&rightRgb<0)
                        || (rgb<0&&rightRgb>=0)
                        || (rgb>=0&&downRgb<0)
                        || (rgb<0&&downRgb>=0)
                        );
                if(isBorder){
                    newImage.setRGB(i,j,Color.GRAY.getRGB());
                }
            }
        }
    }

    //读取像素点x,y为中心的3*3的每个像素点
    public static void readPixel(BufferedImage img,int x,int y,int[] pixels){
        int xStart = x-1;
        int yStart = y-1;
        int current = 0;
        for(int i = xStart;i<3+xStart;i++){
            for(int j = yStart;j<3+yStart;j++){
                int tx = i;
                if(tx<0){
                    tx = -tx;
                }else if(tx>=img.getWidth()){
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);
            }
        }
    }
    public static void fillMatrix(int[][] matrix,int[] values){
        int filled = 0;
        for(int[] x:matrix){
            for (int i = 0; i < x.length; i++) {
                x[i] = values[filled++];
            }
        }
    }
    public static int avgMatrix(int[][] matrix){
        int r = 0,g = 0,b = 0;
        for(int[] x:matrix){
            for (int j = 0; j < x.length; j++) {
                if(j==1){
                    continue;
                }
                Color color = new Color(x[j]);
                r+=color.getRed();
                g+=color.getGreen();
                b+=color.getBlue();
            }
        }
        return new Color(r/8,g/8,b/8).getRGB();
    }

    public static void interfereTemplate(BufferedImage oriImage, BufferedImage templateImage, int x, int y) {
        //临时数组遍历用于高斯模糊存周边像素值
        int[][] matrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        //模板图像宽度
        for (int i = 0; i < xLength; i++) {
            //模板图片高度
            for (int j = 0; j < yLength; j++) {
                //如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    //抠图区域高斯模糊
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(matrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(matrix));
                }
            }
        }
    }
}

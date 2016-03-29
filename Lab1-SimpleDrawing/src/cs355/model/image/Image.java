package cs355.model.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class Image extends CS355Image{
	
	public BufferedImage bi = null;

	public Image()
	{
		super();
	}

	@Override
	public BufferedImage getImage()
	{
		if(bi != null)
			return bi;
		
		int w = super.getWidth();
		int h = super.getHeight();
		bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		WritableRaster wr = bi.getRaster();
        
		int[] rgb = new int[3];
		
		for (int y = 0; y < h; ++y) 
		{
			for (int x = 0; x < w; ++x) 
			{
				wr.setPixel(x, y, super.getPixel(x, y, rgb));
			}
		}
		
		bi.setData(wr);
		
        return bi;
	}

	@Override
	public void edgeDetection()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sharpen()
	{
		int[] rgb = new int[3];
		
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; ++y) 
		{
			for (int x = 0; x < width; ++x) 
			{
				int px = x > 0 ? x-1 : 0; //previous x, or 0
				int py = y > 0 ? y-1 : 0; //previous y, or 0
				int nx = x < width-1 ? x+1 : x; //next x, or x if edge
				int ny = y < height-1 ? y+1 : y; //next y, or y if edge
				
				rgb[0] =	(-super.getRed(x, py)+
							-super.getRed(px, y)+
							(6*super.getRed(x, y))+
							-super.getRed(nx, y)+
							-super.getRed(x, ny))/2;
				
				rgb[1] =	(-super.getGreen(x, py)+
							-super.getGreen(px, y)+
							(6*super.getGreen(x, y))+
							-super.getGreen(nx, y)+
							-super.getGreen(x, ny))/2;
				
				rgb[2] =	(-super.getBlue(x, py)+
							-super.getBlue(px, y)+
							(6*super.getBlue(x, y))+
							-super.getBlue(nx, y)+
							-super.getBlue(x, ny))/2; 
				
				//ensure values are in bounds (0 <= x <= 255
				rgb[0] = Math.max(Math.min(rgb[0], 255),0);
				rgb[1] = Math.max(Math.min(rgb[1], 255),0);
				rgb[2] = Math.max(Math.min(rgb[2], 255),0);
				
				setPixel(x, y, rgb); // Set the pixel
			}
		}
		
		bi=null; //reset buffered image so it will redraw
	}

	@Override
	public void medianBlur()
	{
		int[] rgb = new int[3];
		
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; ++y) 
		{
			for (int x = 0; x < width; ++x) 
			{
				int px = x > 0 ? x-1 : 0; //previous x, or 0
				int py = y > 0 ? y-1 : 0; //previous y, or 0
				int nx = x < width-1 ? x+1 : x; //next x, or x if edge
				int ny = y < height-1 ? y+1 : y; //next y, or y if edge
				
				int[] red =	{super.getRed(px, py),
							super.getRed(x, py),
							super.getRed(nx, py),
							super.getRed(px, y),
							super.getRed(x, y),
							super.getRed(nx, y),
							super.getRed(px, ny),
							super.getRed(x, ny),
							super.getRed(nx, ny)};
				
				int[] green={super.getBlue(px, py),
							super.getBlue(x, py),
							super.getBlue(nx, py),
							super.getBlue(px, y),
							super.getBlue(x, y),
							super.getBlue(nx, y),
							super.getBlue(px, ny),
							super.getBlue(x, ny),
							super.getBlue(nx, ny)};
				
				int[] blue ={super.getGreen(px, py),
							super.getGreen(x, py),
							super.getGreen(nx, py),
							super.getGreen(px, y),
							super.getGreen(x, y),
							super.getGreen(nx, y),
							super.getGreen(px, ny),
							super.getGreen(x, ny),
							super.getGreen(nx, ny)};
				
				//order colors
				Arrays.sort(red);
				Arrays.sort(green);
				Arrays.sort(blue);
				
				//grab median color
				rgb[0] = red[4];
				rgb[1] = green[4];
				rgb[2] = blue[4];
				
				setPixel(x, y, rgb); // Set the pixel
			}
		}
		
		bi=null; //reset buffered image so it will redraw
	}

	@Override
	public void uniformBlur()
	{
		int[] rgb = new int[3];
		
		int height = super.getHeight();
		int width = super.getWidth();
		
		for (int y = 0; y < height; ++y) 
		{
			for (int x = 0; x < width; ++x) 
			{
				int px = x > 0 ? x-1 : 0; //previous x, or 0
				int py = y > 0 ? y-1 : 0; //previous y, or 0
				int nx = x < width-1 ? x+1 : x; //next x, or x if edge
				int ny = y < height-1 ? y+1 : y; //next y, or y if edge
				
				//average red
				rgb[0] =	(super.getRed(px, py)+
							super.getRed(x, py)+
							super.getRed(nx, py)+
							super.getRed(px, y)+
							super.getRed(x, y)+
							super.getRed(nx, y)+
							super.getRed(px, ny)+
							super.getRed(x, ny)+
							super.getRed(nx, ny))/9;
				
				//average green
				rgb[1] =	(super.getBlue(px, py)+
							super.getBlue(x, py)+
							super.getBlue(nx, py)+
							super.getBlue(px, y)+
							super.getBlue(x, y)+
							super.getBlue(nx, y)+
							super.getBlue(px, ny)+
							super.getBlue(x, ny)+
							super.getBlue(nx, ny))/9;
				
				//average blue
				rgb[2] =	(super.getGreen(px, py)+
							super.getGreen(x, py)+
							super.getGreen(nx, py)+
							super.getGreen(px, y)+
							super.getGreen(x, y)+
							super.getGreen(nx, y)+
							super.getGreen(px, ny)+
							super.getGreen(x, ny)+
							super.getGreen(nx, ny))/9;
				
				setPixel(x, y, rgb); // Set the pixel
			}
		}
		
		bi=null; //reset buffered image so it will redraw
	}

	@Override
	public void grayscale()
	{
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		
		for (int y = 0; y < super.getHeight(); ++y) 
		{
			for (int x = 0; x < super.getWidth(); ++x) 
			{
				rgb = super.getPixel(x, y, rgb);
				
				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[1] = 0; //set saturation to 0
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();

				setPixel(x, y, rgb); // Set the pixel.
			}
		}
		
		bi=null; //reset buffered image so it will redraw
	}

	@Override
	public void contrast(int amount)
	{
		float scalar = (float) Math.pow(((amount+100)/100), 4);
        
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		
		for (int y = 0; y < super.getHeight(); ++y) 
		{
			for (int x = 0; x < super.getWidth(); ++x) 
			{
				super.getPixel(x, y, rgb);
				
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[2] = scalar*(hsb[2]-128)+128; //contrast the brightness or somethin
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();

				setPixel(x, y, rgb); // Set the pixel.
			}
		}
		
		bi=null; //reset buffered image so it will redraw
	}

	@Override
	public void brightness(int amount)
	{
		float adjustedAmount = amount/100;
        
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		
		for (int y = 0; y < super.getHeight(); ++y) 
		{
			for (int x = 0; x < super.getWidth(); ++x) 
			{
				super.getPixel(x, y, rgb);
				
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[2] += adjustedAmount; //adjust brightness
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();

				setPixel(x, y, rgb); // Set the pixel
			}
		}
		
		bi=null; //reset buffered image so it will redraw
	}

}

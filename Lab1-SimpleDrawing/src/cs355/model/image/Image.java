package cs355.model.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Image extends CS355Image{
	
	public BufferedImage bi = null;

	public Image()
	{
		super();
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void medianBlur()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uniformBlur()
	{
		// TODO Auto-generated method stub
		
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
				
				hsb[1] = 0;
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();
				// Set the pixel.
				setPixel(x, y, rgb);
			}
		}
		
		bi=null;
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
				
				hsb[2] = scalar*(hsb[2]-128)+128;
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();
				// Set the pixel.
				setPixel(x, y, rgb);
			}
		}
		
		bi=null;
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
				
				hsb[2] += adjustedAmount;
				
				Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = c.getRed();
				rgb[1] = c.getGreen();
				rgb[2] = c.getBlue();
				// Set the pixel.
				setPixel(x, y, rgb);
			}
		}
		
		bi=null;
	}

}

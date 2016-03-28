package cs355.model.image;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contrast(int amount)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void brightness(int amount)
	{
		// TODO Auto-generated method stub
		
	}

}

package net.parvate.iReader.imageproc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import net.parvate.iReader.IReaderActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageProcessing {

	public static ArrayList<Line> paragraphs = new ArrayList<Line>();
	

	public static ArrayList<Line> find(Bitmap image) {
		// Image to Bitmap
		paragraphs.clear();
		SmartScanner.resetnum();
		System.out.println("Width: " + image.getWidth());
		System.out.println("Height: " + image.getHeight());

		System.out.println("Finding");
		new SmartScanner().separateColumns(image);
		System.out.println("Number of Paragraphs found: " + paragraphs.size());

		for (int p = 0; p < paragraphs.size(); p++) {
			// Prints coordinates of all paragraphs

			System.out.println("height: " + image.getHeight() + ", width: "
					+ image.getWidth());
			
			System.out.println(paragraphs.get(p).toString());

		}
		return paragraphs;
		
		
		

	}

}

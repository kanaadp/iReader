package net.parvate.iReader.imageproc;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.graphics.Bitmap;

public class ImagetoText {
	public static String ocrText(Bitmap bmp, TessBaseAPI baseApi) {
		bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
		baseApi.setDebug(true);
		baseApi.setImage(bmp);
		System.out.println("here");
		String recognizedText = baseApi.getUTF8Text();
		System.out.println("there");
		System.out.println("---------------------output-------------------");
		System.out.println("recognizedText<" + recognizedText + ">");
		return recognizedText;
	}
	

}

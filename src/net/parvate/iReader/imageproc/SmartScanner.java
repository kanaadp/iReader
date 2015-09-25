package net.parvate.iReader.imageproc;

import android.graphics.Bitmap;

public class SmartScanner {
	private static int columnSpace = 20;
	private static int paragraphSpace = 70;
	private static int blackThreshold = 3881787;
	private static int num = 1;
	
	public static void resetnum(){
		num=1;
	}
	
	public static void setColumnSpace(int newcolumnSpace){
		columnSpace = newcolumnSpace;
		
	}
	public static void setParagraphSpace(int newParagraphSpace){
		paragraphSpace = newParagraphSpace;
		
	}
	public static void setBlackThreshold(int newBlackThreshold){
		blackThreshold = newBlackThreshold;
		
	}
	public void separateColumns(Bitmap image) {
		System.out.println("Column Separation");
		boolean inTextColumn = false;
		boolean lineisWhite;
		int firstBlackLine = 0;
		int lastBlackLine = 0;
		int consecutiveWhiteLines = 0;
		
		//scans horizontally across
		for (int x = 0; x < image.getWidth(); x++) {
			lineisWhite = true;
			//System.out.println("Scanning Column" + x);
			
			//scans vertically down
			for (int y = 0; y < image.getHeight(); y++) {
				
				//IF black pixel is found, line is no longer white.
				if (isPixelBlack(image, x, y)) {
					
					//System.out.println("Pixel at : " + x +", " + y+"is: " +image.getPixel(x, y));
					lineisWhite = false;
					
				}
			}
			if (lineisWhite) {
				if (!inTextColumn) {
					//Keep searching for black lines
					consecutiveWhiteLines++;
				} else {
					
					consecutiveWhiteLines++;
					//Could be the end of a column.
					//If consecutive white lines is greater than or equal to column space column is found.
					if (consecutiveWhiteLines >= columnSpace) {
						System.out.println("Column Found");
						lastBlackLine = (x-columnSpace);
						separateParagraphs(image, firstBlackLine, lastBlackLine);
						inTextColumn = false;
						
					}
					//Otherwise, keep searching
					
					
					//System.out.println("Consecutive White Lns?  "+ consecutiveWhiteLines);
				}
			} else {
										
				if (!inTextColumn) {
					//Found first line that is not white. 
					System.out.println("First Black Line at :" + x);
					firstBlackLine = x;
					inTextColumn = true;
				}

				consecutiveWhiteLines = 0;

			}

		}
	}

	
	//Uses very similar logic as separateColumns()
	public void separateParagraphs(Bitmap image, int firstLine, int secondLine) {
		System.out.println("Separate Paragraphs");
		boolean inTextParagraph = false;
		boolean lineisWhite;
		int consecutiveWhiteLines = 0;
		int w = 0;
		int h1 = 0;
		for (int h = 0; h < image.getHeight(); h++) {
			lineisWhite = true;
			for (w = firstLine; w < secondLine; w++) {

				if (isPixelBlack(image, w, h)) {
					lineisWhite = false;
				}
			}
			if (lineisWhite) {
				if (!inTextParagraph) {
					consecutiveWhiteLines++;
				} else {
					consecutiveWhiteLines++;
					if (consecutiveWhiteLines >= paragraphSpace) {
						Line paragraph = null;
						paragraph = new Line(firstLine, secondLine, h1, (h-consecutiveWhiteLines));
						ImageProcessing.paragraphs.add(paragraph);
						System.out.println("Paragraph Found! " + num);
						num++;
						inTextParagraph = false;
					}
				}
			} else {
				//System.out.println("Line: "+ h + "is black");
				//line is not white
				if (!inTextParagraph) {
					//set current line to first line of paragraph
					h1 = h;
					inTextParagraph = true;
				}

				consecutiveWhiteLines = 0;

			}

		}
		if (inTextParagraph){
			Line fullColumn = new Line(firstLine, secondLine, h1, image.getHeight());
			ImageProcessing.paragraphs.add(fullColumn);
		}
	}
	private boolean isPixelBlack(Bitmap bmp, int x, int y){
		//Conversion of ARGB to RGB
		int pixelColorARGB = bmp.getPixel(x,y);
		
		//& operator blanks out first two digits (alpha component)
		int pixelColorRGB = (pixelColorARGB & 0x00FFFFFF);
		//System.out.println("Pixel at : " + x +", " + y+"is: " +pixelColorRGB);
		if(pixelColorRGB < blackThreshold){
			
			return true;
		}
		else{
			return false;
		}
	}
	
}

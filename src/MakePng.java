import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.SourceStringReader;

public class MakePng {
	
	private String source = "";
	
	public MakePng() {
		source +=  "@startuml\n";
		source +=  "start\n";
	}

	public void addElement(String element)
	{
		source += element + ";\n";
	}
	
	public void generateImageTo(String output)
	{
		ByteArrayOutputStream bous = new ByteArrayOutputStream();
		source +=  "end\n";
		source += "@enduml\n";
		SourceStringReader reader = new SourceStringReader(source);
		// Write the first image to "png"
		String desc;
		try {
			desc = reader.generateImage(bous);
			// Return a null string if no generation
		byte [] data = bous.toByteArray();
	
		InputStream in = new ByteArrayInputStream(data);
		BufferedImage convImg = ImageIO.read(in);
		
		//Replace path with output param
		ImageIO.write(convImg, "png", new File(output));
		//System.out.print(desc);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "MakePng [source=" + source + "]";
	}
	
	
	
}


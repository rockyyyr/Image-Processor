# Image-Processor
###### Display images by replacing pixels with UTF-8 Characters

![alt text](https://user-images.githubusercontent.com/16550024/27202155-4bd18958-51d5-11e7-9146-b134b9371f50.png)

Image pixels are divided into groups which represent a color range. The amount of groups is detemerined by the colorDepth parameter.
The value for each color can range from 0 - 255. To generate the range for each group, 255 or MAX_RGB is divided by colorDepth. Multiples 
of the range value are then assigned to the colorBounds array of length equal to the colorDepth.
```java
private void setRange() {
	RANGE = MAX_RGB / COLOR_DEPTH;
}

private void buildColorRange() {
	colorBounds = new int[COLOR_DEPTH + EXPOSURE];

	for (int i = 0; i < colorBounds.length; i++) {
		colorBounds[i] = RANGE * i;
	}
}
  ``` 
The EXPOSURE parameter, if set to low, adds another color range group which shortens the range where a pixel will be considered white.
If set to high, the range a pixel can be white is equal to the RANGE value plus the remainder of MAX_RGB / colorDepth.

An int array representing pixel color values is generated from the original image:
```java
int width = img.getWidth();
int height = img.getHeight();

int[] pixelMap = img.getRGB(0, 0, width, height, null, 0, width);
```    
    
Since the rendered image wont be using color, an average of red, blue and green is used to determine which grouping each pixel is assigned:
```java 
int count = 0;
for (int pixel : pixelMap) {
  Color color = new Color(pixel);
            
	int val = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
    ....
```    
Then iterate through the colorBounds and check if each pixel is in that particular range. If it is, add the corresponding character
from the pixels char array and break out of the loop.
```java
for (int i = 0; i < colorBounds.length - 1; i++) {
  if (val >= colorBounds[i] && val < colorBounds[i + 1]) {
	  sb.append(pixels[i]);
		break;
	}
}
```
Check for any white pixels:
```java
if (val > colorBounds[colorBounds.length - 1])
  sb.append(WHITE_PIXEL);
```                       
Check to see if iteration of the row is complete. If it is, add a new line character and reset the counter:
```java
if (++count == img.getWidth()) {
  sb.append(NEW_LINE);
	count = 0;
}
```         
            
The rendered image is then returned as a string.


            


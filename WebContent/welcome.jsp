<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link href="style.css" type="text/css" rel="stylesheet">
    <title>UTF-8 Image Processor</title>
</head>
<body>
<div id="header">
    <h1>UTF-8 Image Processor</h1>
</div>
<div id="content">

    <div class="module">
        <h2>Upload</h2>

        <div id="file-selection">
            <p>Choose image file or take a photo. <b>*Take photos in landscape mode*</b></p>
            <form action='upload' method='POST' enctype='multipart/form-data'>
                <input type='file' name='file' id="file-input">
                <div class="hidden">
                    <input name="colorDepth">
                    <input name="exposure">
                    <input name="charset">
                    <input name="pixelSize">
                </div>
                <input type='submit' id="file-submit" value="Submit File ">
            </form>
        </div>
        <br>
        <div class="item" id="link-selection">
            <p>Submit a link</p>
            <form action='upload' method='GET'>
                <input type='url' name='image-url' id="url-input" placeholder="URL" style="margin-right: 92px">
                <div class="hidden">
                    <input name="colorDepth">
                    <input name="exposure">
                    <input name="charset">
                    <input name="pixelSize">
                </div>
                <input type='submit' id="url-submit" value="Submit Link">
            </form>
        </div>
    </div>

    <hr>

    <div class="module" id="settings">
        <h2>Settings</h2>

        <div class="module">
            <div class="setting">
                <label for="pixelSize"><B>Pixel size: </B> </label>
                <input id="pixelSize"
                       type="number"
                       min="1" max="30"
                       name="pixelSize-setting"
                       onchange="applyPixelSize()"
                       placeholder="1 - 30">
            </div>
        </div>

        <br>

        <div class="module">
            <div class="setting">
                <label for="colorDepth"><B>Color depth:</B> </label>
                <input id="colorDepth"
                       type="number"
                       min="2" max="50"
                       name="colorDepth-setting"
                       onchange="applyColorDepth()"
                       placeholder="2 - 50">
            </div>
        </div>

        <br>

        <div class="module">
            <div class="setting">
                <label><B>Exposure:</B> </label><br>
                <input type="radio"
                       name="exposure-setting"
                       class="exposure-setting"
                       value="High Exposure"
                       onchange="applyExposure()"
                       checked>
                <label>High Exposure</label><br>
                <input type="radio"
                       name="exposure-setting"
                       class="exposure-setting"
                       value="Low Exposure"
                       onchange="applyExposure()">
                <label>Low Exposure</label><br>
            </div>
        </div>

        <br>

        <div class="module">
            <div class="setting">
                <label><B>Pixels:</B> </label>
                <input type="text"
                       name="charset-setting"
                       onchange="applyCharset()"
                       placeholder="0123456789abcdefgh">
            </div>
        </div>

    </div>

    <hr>

    <div class="module">
        <h2>Help</h2>

        <h3>Pixel size:</h3>
        <p>Sets the font size for each pixel. 1 - 30. Default value is 1</p>

        <h3>Color depth:</h3>
        <p>The number of colors to process. This can range from 2 - 50 but usually looks best
            from 3 - 10. Default value is 4</p>

        <h3>Exposure:</h3>
        <p>When exposure is set to low, very little white space will show.
            High exposure better simulates a silhouette in most situations.</p>

        <h3>Pixels:</h3>
        <p>A custom charset can be set. The characters in the charset are used to replace pixels
            depending on their brightness. The character at the beginning will replace black pixels,
            each subsequent character will replace pixels of increasing brightness. The last character will
            replace white pixels. Charset should be equal to the color depth value.
            If no charset is specified, a default charset will be used</p>

        <hr>

        <p>Rendering of the image depends on device/browser, and entirely on font, letter spacing and level of zoom.
            On desktop, browser zoom should be set to 100%.
            On mobile devices, images render better in landscape orientation</p>
        <p>Tested in Chrome for desktop/mobile</p>
        <hr>
    </div>
</div>

<script src="jquery-3.2.1.slim.min.js"></script>
<script src="script.js"></script>

</body>
</html>
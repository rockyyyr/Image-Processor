
function applyColorDepth(){
    var colorDepth = $('[name="colorDepth-setting"]').val();
    $('[name="colorDepth"]').val(colorDepth);
}

function applyExposure(){
    var exposure = $('.exposure-setting:checked').val();
    $('[name="exposure"]').val(exposure);
}

function applyCharset(){
    var charset = $('[name="charset-setting"]').val();
    $('[name="charset"]').val(charset);
}

function applyPixelSize(){
    var pixelSize = $('[name="pixelSize-setting"]').val();
    $('[name="pixelSize"]').val(pixelSize);
}
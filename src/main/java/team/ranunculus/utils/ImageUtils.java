package team.ranunculus.utils;

import java.util.Base64;
public class ImageUtils {
    public String getImgData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
}

package ticket_online.ticket_online.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ticket_online.ticket_online.dto.ApiResponse;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateUtil {

    public static String generateSignatureKeySHA256(String dataBeforeSignature){

        try {
            // Menggunakan MessageDigest untuk menghasilkan hash SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataBeforeSignature.getBytes());
            // Mengonversi byte[] hash menjadi string hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            // Tampilkan signature
            return hexString.toString();

        } catch (Exception e) {
            return null;
        }
    }

    public static String generateSignatureKeyMD5(String dataBeforeSignature){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(dataBeforeSignature.getBytes());

            // Konversi byte array ke string hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b & 0xff));
            }

            return hexString.toString();
//            return hexString.toString().toUpperCase(); // Sama seperti md5() di PHP (menghasilkan huruf kapital)
        }catch (Exception e){
            return  null;
        }
    }

    public static String generateImgUrl(String image){

        if(image == null){
            return null;
        }else{
            return  ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploaded-images/" + image)  // Menambahkan path gambar
                    .toUriString();
        }
    }

    public static String generateMerchantOrderId(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String formattedDate = now.format(formatter);
        return formattedDate;
    }

    public static String transactionCode(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String formattedDate = "TR"+ now.format(formatter);
        return formattedDate;
    }

    public static String ticketCode(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String formattedDate = "QR"+ now.format(formatter) + "TK";
        return formattedDate;
    }

}

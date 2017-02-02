package com.erith.api.imageprocessing.com.erith.api.imageprocessing.exercise;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetector {
    // https://blog.openshift.com/day-12-opencv-face-detection-for-java-developers/
    // make user library and add it to project

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File(FaceDetector.class.getResource("photoCheck/PhotoA.png").getPath()));
        detectFace(image);
    }

    private static Mat convertBufImg2Mat(BufferedImage image) {
        DataBufferByte s;
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }

    private static int detectFace(BufferedImage image) {

        System.out.println("step0: Running FaceDetector");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        CascadeClassifier faceDetector = new CascadeClassifier(
                FaceDetector.class.getResource("D:\\workspace\\api-imageprocessing\\src\\main\\resources\\haarcascades\\haarcascade_frontalface_alt.xml").getPath());
        if (!faceDetector.load("D:\\workspace\\api-imageprocessing\\src\\main\\resources\\haarcascades\\haarcascade_frontalface_alt.xml")) {
            return -1;
        }

        System.out.println("step1: convert bufferedimage to mat type");
        Mat matImage = convertBufImg2Mat(image);


        System.out.print("step2: detect face- ");
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(matImage, faceDetections);
        System.out.println(String.format(" %s faces", faceDetections.toArray().length));

        System.out.println("step3: write faces");
        String filename = "/0_research/" + "ouputA.png";
        for (Rect rect : faceDetections.toArray()) {
            writeFrame(filename, matImage, rect);
        }
        return faceDetections.toArray().length;
    }

    private static BufferedImage cropImage(BufferedImage src, Rect rect) {
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }

    public static void writeFrame(String filename, Mat mat, Rect rect) {

        byte[] data = new byte[mat.rows() * mat.cols() * (int) (mat.elemSize())];
        mat.get(0, 0, data);
        if (mat.channels() == 3) {
            for (int i = 0; i < data.length; i += 3) {
                byte temp = data[i];
                data[i] = data[i + 2];
                data[i + 2] = temp;
            }
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        BufferedImage frame = cropImage(image, rect);
        try {
            ImageIO.write(frame, "png", new File(filename + ".png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
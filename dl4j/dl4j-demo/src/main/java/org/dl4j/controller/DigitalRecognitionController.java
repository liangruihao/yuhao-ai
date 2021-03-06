package org.dl4j.controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.dl4j.constant.WebConstant;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sun.misc.BASE64Decoder;

@RequestMapping("/digitalRecognition")
@Controller
public class DigitalRecognitionController implements InitializingBean {
	private MultiLayerNetwork net;

	@RequestMapping("/index")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/predict")
	public int predict(@RequestParam(value = "img") String img) throws Exception {
		String imagePath= generateImage(img);//
		imagePath= zoomImage(imagePath);//
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		ImageRecordReader testRR = new ImageRecordReader(28, 28, 1);
		File testData = new File(imagePath);
		FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS);
		testRR.initialize(testSplit);
		DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, 1);
		testIter.setPreProcessor(scaler);
		INDArray array = testIter.next().getFeatures(); // .getFeaturesMaskArray(); // testIter.next().getFeatureMatrix();
		return net.predict(array)[0];
		
	}

	private String generateImage(String img) {
		BASE64Decoder decoder = new BASE64Decoder();
		String filePath = WebConstant.WEB_ROOT + "upload/"+UUID.randomUUID().toString()+".png";
		try {
			byte[] b = decoder.decodeBuffer(img);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(filePath);
			out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}
	
	private String zoomImage(String filePath){
		String imagePath=  WebConstant.WEB_ROOT + "upload/"+UUID.randomUUID().toString()+".png";
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(filePath));
			Image image = bufferedImage.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); //
			g.dispose();
			ImageIO.write(tag, "png",new File(imagePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagePath;
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Loading model ...... ");
		//net = ModelSerializer.restoreMultiLayerNetwork(new File(WebConstant.WEB_ROOT + "model/minist-model.zip"));
		//String modelWeights = new ClassPathResource("model_weights.h5").getFile().getPath();
		net = KerasModelImport.importKerasSequentialModelAndWeights(
				"model/mnist_cnn_model_json", 
				"model/mnist_cnn_model_save");
		
	}

}

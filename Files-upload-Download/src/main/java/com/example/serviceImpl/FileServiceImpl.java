package com.example.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.Repository.ProductRepo;
import com.example.model.Product;
import com.example.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Value("${file.upload.path}")
	private String uploadPath;

	@Autowired
	private ProductRepo productRepo;

	@Override
	public Boolean uploadFile(MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();

		File saveFile = new File(uploadPath);

		if (!saveFile.exists()) {
			saveFile.mkdir();
		}
		String storePath = uploadPath.concat(filename);
		long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
		if (upload != 0) {
			return true;
		}

		return false;
	}

	@Override
	public byte[] downloadFile(String file) throws IOException {
		String fullPath = uploadPath.concat(file); // files/Screenshot.png
		try {
			InputStream ios = new FileInputStream(fullPath);

			return StreamUtils.copyToByteArray(ios);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public Boolean saveProduct(Product product) {
		Product save = productRepo.save(product);
		if (!ObjectUtils.isEmpty(save)) {
			return true;
		}
		return false;

	}

	@Override
	public String uploadFileWithData(MultipartFile file) throws IOException {
		
		String fileName = file.getOriginalFilename();

		File saveFile = new File(uploadPath);
		
		String rndString = UUID.randomUUID().toString();
		String removeExtension = FilenameUtils.removeExtension(fileName);
		String extension = FilenameUtils.getExtension(fileName);
		fileName=removeExtension+"_"+rndString+"."+extension;
		
		
		if (!saveFile.exists()) {
			saveFile.mkdir();
		}
		String storePath = uploadPath.concat(fileName);
		long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
		if (upload != 0) {
			return fileName;
		}

		return null;
	}
	

}

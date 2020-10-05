package com.javatpoint.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.javatpoint.model.Books;
import com.javatpoint.repository.BooksRepository;

//defining the business logic
@Service
public class BooksService {
	@Autowired
	BooksRepository booksRepository;

	public List<Books> getAllBooks() {
		List<Books> books = new ArrayList<Books>();
		booksRepository.findAll().forEach(books1 -> books.add(books1));
		return books;
	}

	public Books getBooksById(int id) {
		return booksRepository.findById(id).get();
	}

	public void saveOrUpdate(Books books) {
		booksRepository.save(books);
	}

	public void delete(int id) {
		booksRepository.deleteById(id);
	}

	public void update(Books books, int bookid) {
		booksRepository.save(books);
	}

	public void save(MultipartFile file) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			for (CSVRecord csvRecord : csvRecords) {
				Books book = new Books(Integer.parseInt(csvRecord.get("bookid")), csvRecord.get("bookname"),
						csvRecord.get("author"), Integer.parseInt(csvRecord.get("price")));
				booksRepository.save(book);
			}
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}

	}
}
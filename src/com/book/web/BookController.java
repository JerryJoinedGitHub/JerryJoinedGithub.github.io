package com.book.web;

import com.book.domain.Book;
import com.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class BookController {
	private BookService bookService;

	@Autowired
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	@RequestMapping("/querybook.html")
	public ModelAndView queryBookDo(HttpServletRequest request, String searchWord) {
		boolean exist = bookService.matchBook(searchWord);
		if (exist) {
			ArrayList<Book> books = bookService.queryBook(searchWord);
			ModelAndView modelAndView = new ModelAndView("admin_books");
			modelAndView.addObject("books", books);
			return modelAndView;
		} else {
			return new ModelAndView("admin_books", "error", "No search result found.");
		}
	}

	@RequestMapping("/reader_querybook.html")
	public ModelAndView readerQueryBook() {
		ArrayList<Book> books = bookService.getAllBooks();
		ModelAndView modelAndView = new ModelAndView("reader_book_query");
		modelAndView.addObject("books", books);
		return modelAndView;

	}

	@RequestMapping("/reader_querybook_do.html")
	public ModelAndView readerQueryBookDo(HttpServletRequest request, String searchWord,
			RedirectAttributes redirectAttributes) {
		boolean exist = bookService.matchBook(searchWord);
		if (exist) {
			ArrayList<Book> books = bookService.queryBook(searchWord);
			ModelAndView modelAndView = new ModelAndView("reader_book_query");
			modelAndView.addObject("books", books);
			return modelAndView;
		} else {
			return new ModelAndView("reader_book_query", "error", "No matching result found.");
		}

	}

	@RequestMapping("/allbooks.html")
	public ModelAndView allBook() {
		ArrayList<Book> books = bookService.getAllBooks();
		ModelAndView modelAndView = new ModelAndView("admin_books");
		modelAndView.addObject("books", books);
		return modelAndView;
	}

	@RequestMapping("/deletebook.html")
	public String deleteBook(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		long bookId = Integer.parseInt(request.getParameter("bookId"));
		int res = bookService.deleteBook(bookId);

		if (res == 1) {
			redirectAttributes.addFlashAttribute("succ", "Book successfully deleted!");
			return "redirect:/allbooks.html";
		} else {
			redirectAttributes.addFlashAttribute("error", "Failed to delete.");
			return "redirect:/allbooks.html";
		}
	}

	@RequestMapping("/book_add.html")
	public ModelAndView addBook(HttpServletRequest request) {

		return new ModelAndView("admin_book_add");

	}

	@RequestMapping("/book_add_do.html")
	public String addBookDo(BookAddCommand bookAddCommand, RedirectAttributes redirectAttributes) {
		Book book = new Book();
		book.setBookId(0);
		book.setPrice(bookAddCommand.getPrice());
		book.setState(bookAddCommand.getState());
		book.setPublish(bookAddCommand.getPublish());
		book.setPubdate(bookAddCommand.getPubdate());
		book.setName(bookAddCommand.getName());
		book.setIsbn(bookAddCommand.getIsbn());
		book.setClassId(bookAddCommand.getClassId());
		book.setAuthor(bookAddCommand.getAuthor());
		book.setIntroduction(bookAddCommand.getIntroduction());
		book.setPressmark(bookAddCommand.getPressmark());
		book.setLanguage(bookAddCommand.getLanguage());

		boolean succ = bookService.addBook(book);
		// ArrayList<Book> books=bookService.getAllBooks();
		if (succ) {
			redirectAttributes.addFlashAttribute("succ", "Book successfuly added.");
			return "redirect:/allbooks.html";
		} else {
			redirectAttributes.addFlashAttribute("succ", "Failed!");
			return "redirect:/allbooks.html";
		}
	}

	@RequestMapping("/updatebook.html")
	public ModelAndView bookEdit(HttpServletRequest request) {
		long bookId = Integer.parseInt(request.getParameter("bookId"));
		Book book = bookService.getBook(bookId);
		ModelAndView modelAndView = new ModelAndView("admin_book_edit");
		modelAndView.addObject("detail", book);
		return modelAndView;
	}

	@RequestMapping("/book_edit_do.html")
	public String bookEditDo(HttpServletRequest request, BookAddCommand bookAddCommand,
			RedirectAttributes redirectAttributes) {
		long bookId = Integer.parseInt(request.getParameter("id"));
		Book book = new Book();
		book.setBookId(bookId);
		book.setPrice(bookAddCommand.getPrice());
		book.setState(bookAddCommand.getState());
		book.setPublish(bookAddCommand.getPublish());
		book.setPubdate(bookAddCommand.getPubdate());
		book.setName(bookAddCommand.getName());
		book.setIsbn(bookAddCommand.getIsbn());
		book.setClassId(bookAddCommand.getClassId());
		book.setAuthor(bookAddCommand.getAuthor());
		book.setIntroduction(bookAddCommand.getIntroduction());
		book.setPressmark(bookAddCommand.getPressmark());
		book.setLanguage(bookAddCommand.getLanguage());

		boolean succ = bookService.editBook(book);
		if (succ) {
			redirectAttributes.addFlashAttribute("succ", "Book successfuly updated.");
			return "redirect:/allbooks.html";
		} else {
			redirectAttributes.addFlashAttribute("error", "Failed!");
			return "redirect:/allbooks.html";
		}
	}

	@RequestMapping("/bookdetail.html")
	public ModelAndView bookDetail(HttpServletRequest request) {
		long bookId = Integer.parseInt(request.getParameter("bookId"));
		Book book = bookService.getBook(bookId);
		ModelAndView modelAndView = new ModelAndView("admin_book_detail");
		modelAndView.addObject("detail", book);
		return modelAndView;
	}

	@RequestMapping("/readerbookdetail.html")
	public ModelAndView readerBookDetail(HttpServletRequest request) {
		long bookId = Integer.parseInt(request.getParameter("bookId"));
		Book book = bookService.getBook(bookId);
		ModelAndView modelAndView = new ModelAndView("reader_book_detail");
		modelAndView.addObject("detail", book);
		return modelAndView;
	}

}

package in.ongrid.kshitijroy.service;

import in.ongrid.kshitijroy.dao.*;
import in.ongrid.kshitijroy.model.dto.*;
import in.ongrid.kshitijroy.model.entity.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepo userRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    BookTitleRepo bookTitleRepo;

    @Autowired
    OrdersRepo ordersRepo;

    @Autowired
    BooksOrderedRepo booksOrderedRepo;

    @Autowired
    BookRepo bookRepo;

    @Autowired
    AddressRepo addressRepo;

    @Override
    public UserSignUpResponse userSignUpService(UserSignUpRequestDTO userSignUpRequestDTO) {
        UserSignUpResponse userSignUpResponse = new UserSignUpResponse();

        String email = userSignUpRequestDTO.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        User temp = userRepo.findByEmail(email);

        if (temp != null) {
            throw new IllegalArgumentException("Email registered before");
        }

        String pincode= userSignUpRequestDTO.getPinCode();
        if(pincode.length()!=6){
            throw new IllegalArgumentException("Pincode not correct");
        }

        userSignUpRequestDTO.getName();
        Address add = new Address(userSignUpRequestDTO.getHouseDetail(),userSignUpRequestDTO.getCity(),
                userSignUpRequestDTO.getPinCode());

        User user = new User(userSignUpRequestDTO.getName(),userSignUpRequestDTO.getEmail(),
                userSignUpRequestDTO.getPassword(),add);
        userRepo.save(user);
        userSignUpResponse.setId(user.getId());
        return userSignUpResponse;
    }


    @Override
    public UserSignInResponseDTO UserSignIn(UserSignInRequestDTO userSignInRequestDTO) {
        String email = userSignInRequestDTO.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email is not valid");
        }

        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email/password incorrect");
        }
        Long id = user.getId();
        String password = user.getPassword();
        if (!userSignInRequestDTO.getPassword().equals(password)) {
            throw new IllegalArgumentException("Email/password incorrect");
        }
        UserSignInResponseDTO userSignInResponseDTO = new UserSignInResponseDTO();
        userSignInResponseDTO.setName(user.getName());
        userSignInResponseDTO.setId(user.getId());

        return userSignInResponseDTO;

    }

    @Override
    public UserProfileResponseDTO viewProfile(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        User user = userRepo.getById(id); //constructor
        if(user==null){
            throw new IllegalArgumentException("user not found");
        }

       UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO(user.getId(),user.getName(),
               user.getEmail(),user.getAddress().getHouseDetail(),user.getAddress().getCity());


       return userProfileResponseDTO;


    }

    @Override
    public String updateProfile(UserProfileUpdateRequestDTO userProfileUpdateRequestDTO, Long id) {
        User user = userRepo.getById(id);
        if(user==null){
            throw new IllegalArgumentException("user not found");
        }

        if(userProfileUpdateRequestDTO.getPinCode().length()!=6){
            throw new IllegalArgumentException("pincode not correct");
        }

        Address add= userRepo.getById(id).getAddress();
        add.setCity(userProfileUpdateRequestDTO.getCity());
        add.setHouseDetail(userProfileUpdateRequestDTO.getHouseDetails());
        add.setCity(userProfileUpdateRequestDTO.getPinCode());
        addressRepo.save(add);
        return "updated";

    }

    @Override
    public Integer cartStart(Long id){
        if(id==null){
            throw new IllegalArgumentException("invalid user id");
        }
        Cart cart= cartRepo.findByUserCartIdAndPurchased(id,false);
        if(cart!=null){
            return cart.getBookTitleList().size();
        }
        User user= userRepo.getById(id);

        List<Cart> carts= cartRepo.findByUserCartId(id);
        cart= new Cart();
        cart.setUserCart(user);
        cartRepo.save(cart);
        carts.add(cart);
        user.setuCart(carts);
        return cart.getBookTitleList().size();
    }

    @Override
    public CartAddResponseDTO addCart(Long id, CartAddRequestDTO cartAddRequestDTO) {

        if(id==null) {
            throw new IllegalArgumentException("invalid user Id");
        }
        Cart cart = cartRepo.findByUserCartIdAndPurchased(id, false);
        if(cart == null ){
            throw new IllegalArgumentException("user doesn't have cart");
        }
        Long bookTitleId = cartAddRequestDTO.getBookTitleId();
        List<BookTitle> bookList = cart.getBookTitleList();
        BookTitle bookTitle = bookTitleRepo.getById(bookTitleId);
        bookList.add(bookTitle);  // add book in booklist array of cart
        cart.setBookTitleList(bookList);
        cartRepo.save(cart);  // save info in repo
        CartAddResponseDTO cartAddResponseDTO = new CartAddResponseDTO();
        List<CartInfo> details = new ArrayList<>();
        for(BookTitle bookTitle1 : bookList){
                CartInfo cartInfo = new CartInfo(bookTitle1.getId(), bookTitle1.getBookName());
                details.add(cartInfo);
        }
        cartAddResponseDTO.setCartCount(bookList.size());
        cartAddResponseDTO.setBookTitleList(details);
        return cartAddResponseDTO;
    }

    @Override
    public String issue(Long id) {
        if(id==null){
            throw new IllegalArgumentException("user id can not be null");
        }
        Cart cart= cartRepo.findByUserCartIdAndPurchased(id,false);
        if(cart == null){
            throw new IllegalArgumentException("user doesn't have any cart");
        }
        List<BookTitle> bookTitleList = cart.getBookTitleList();
        List<BooksOrdered> booksOrdered= new ArrayList<>();
        List<Long> bookTitlesId = new ArrayList<>();

        for (BookTitle bookTitle: bookTitleList){
            bookTitlesId.add(bookTitle.getId());
        }
        List<BookTitle> bookTitlesList= bookTitleRepo.findBooks(bookTitlesId);
        for(BookTitle bookTitle : bookTitlesList){
            BookTitle bookTitle1 = bookTitleRepo.getById(bookTitle.getId());
            bookTitle1.setAvailable(bookTitle1.getAvailable()-1);
            Book book = bookRepo.getById(bookTitle.getBooks().get(0).getId());
            book.setBooked(true);
            BooksOrdered booksOrder = new BooksOrdered();
            booksOrder.setBookId(book.getId());
            booksOrderedRepo.save(booksOrder);
        }
        Orders orders= new Orders(new Date(), userRepo.getById(id), booksOrdered );
        cart.setPurchased(true);
        ordersRepo.save(orders);
        cartRepo.save(cart);
        return "success";
    }

    @Override
    public CartAddResponseDTO getCart(Long id){
        if(id==null){
            throw new IllegalArgumentException("user id not true");
        }

        Cart cart= cartRepo.findByUserCartIdAndPurchased(id,false);
        List<BookTitle> bookTitleList= cart.getBookTitleList();
        CartAddResponseDTO cartAddResponseDTO = new CartAddResponseDTO();
        List<CartInfo> details = new ArrayList<>();

        for(BookTitle bookTitle1 : bookTitleList){
            CartInfo cartInfo = new CartInfo(bookTitle1.getId(), bookTitle1.getBookName());
            details.add(cartInfo);
        }

        cartAddResponseDTO.setCartCount(bookTitleList.size());
        cartAddResponseDTO.setBookTitleList(details);
        return cartAddResponseDTO;
    }


    @Override
    public List<ViewOrder> seeOrder(Long id){
        if(id==null){
            throw new IllegalArgumentException("invalid user id");
        }
        List<Orders> history= ordersRepo.findByUserOrderId(id);
        List<ViewOrder> result= new ArrayList<>();
        for(Orders oldOrders: history){
            Date issueDate= oldOrders.getIssueDate();
            Date returnDate=null;
            String bookName="";
            Long bookId=null ;
            List<BooksOrdered> booksOrderedList=oldOrders.getBooksOrderedList();
            for(BooksOrdered booksOrdered:booksOrderedList){
                bookId=booksOrdered.getBookId();
                returnDate=booksOrdered.getReturnDate();
                Book bookDetail= bookRepo.getById(bookId);
                bookName=bookDetail.getName();
                ViewOrder oneOrder=new ViewOrder(bookId,bookName,issueDate,returnDate);
                result.add(oneOrder);
            }
        }
        return result;
    }


    @Override
     public  ReturnBookResponseDTO returnBook(Long userId,Long bookId){
            if (userId == null) {
                throw new IllegalArgumentException("user id not valid");
            }
            if (bookId == null) {
                throw new IllegalArgumentException("bookId not valid");
            }

            BooksOrdered booksOrdered = booksOrderedRepo.findByUserBookIdAndBookId(userId, bookId);
            Date returnDate = new Date();
            booksOrdered.setReturnDate(returnDate);
            booksOrderedRepo.save(booksOrdered);

            Date issueDate = booksOrdered.getOrders().getIssueDate();
            Long timeDifference = returnDate.getTime() - issueDate.getTime();

            Long days_difference = (timeDifference / (1000 * 60 * 60 * 24));
            Long cost = 0L;
            if (days_difference <= 15) {
                cost = days_difference * 2;
            } else {
                cost = (days_difference - 15) * 5 + 30;
            }
            ReturnBookResponseDTO returnBookResponseDTO = new ReturnBookResponseDTO(bookId, returnDate, cost);

            Book book = bookRepo.getById(bookId);
            book.setBooked(false);
            bookRepo.save(book);

            BookTitle bookTitle = book.getBookTitle();
            bookTitle.setAvailable(bookTitle.getAvailable() + 1);
            bookTitleRepo.save(bookTitle);

            return returnBookResponseDTO;
        }
    }

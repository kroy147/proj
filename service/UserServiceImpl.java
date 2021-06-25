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

        Long id=user.getId();

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
        /*
        List<Cart> carts= cartRepo.findByUserCartId(id);
        for(int i=0;i<carts.size();i++){
            if(carts.get(i).isPurchased()==false){
                int count= carts.get(i).getBook().size();
                return count;
            }
        }*/
        Cart cart= cartRepo.findByUserCartIdAndPurchased(id,false);
        if(cart!=null){
            return cart.getBook().size();
        }
        User user= userRepo.getById(id);

        List<Cart> carts= cartRepo.findByUserCartId(id);
         cart= new Cart();
        cart.setUserCart(user);
        cartRepo.save(cart);
        Long cartId= cart.getId();
        carts.add(cart);
        user.setuCart(carts);
        System.out.println(cartId);
        return cart.getBook().size();
    }

    @Override
    public CartAddResponseDTO addCart(Long id, CartAddRequestDTO cartAddRequestDTO) {

        if(id==null){
            throw new IllegalArgumentException("invalid user Id");
        }

        Cart cart = cartRepo.findByUserCartIdAndPurchased(id, false);
        //find an empty cart
        Long bookTitleId = cartAddRequestDTO.getBookTitleId();
        List<BookTitle> bookList;
        bookList = cart.getBook();
        BookTitle bookTitle = bookTitleRepo.getById(bookTitleId);
        bookList.add(bookTitle);  // add book in booklist array of cart
        cart.setBook(bookList);
        cartRepo.save(cart);  // save info in repo
        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        bookTitle.setCarts(carts);
        bookTitleRepo.save(bookTitle); // add same detail in cart
        CartAddResponseDTO cartAddResponseDTO = new CartAddResponseDTO();
        List<CartInfo> details = new ArrayList<>();
        for (int i = 0; i < bookList.size(); i++) {
            CartInfo cartInfo = new CartInfo();
            cartInfo.setBookTitleId(bookList.get(i).getId());
            cartInfo.setBookName(bookList.get(i).getBookName());
            details.add(cartInfo);
        }
        cartAddResponseDTO.setCartCount(bookList.size());
        cartAddResponseDTO.setBookTitleList(details);
        return cartAddResponseDTO;

    }

    @Override
    public String issue(Long id) {
        if(id==null){
            throw new IllegalArgumentException("user id not true");
        }

        Cart cart= cartRepo.findByUserCartIdAndPurchased(id,false);
        System.out.println(cart.getId());
        System.out.println(cart.getUserCart().getId());//userID

        Orders orders= new Orders();  // new order create
        orders.setIssueDate(new Date());
        orders.setUserOrder(userRepo.getById(id));

        List<BookTitle> bookTitleList = new ArrayList<>();
        bookTitleList= cart.getBook();
        List<BooksOrdered> booksOrdered= new ArrayList<>();
        List<Book> books=new ArrayList<>();

        for(BookTitle bookTitle:bookTitleList){
            List<BooksCheck> booksChecks= bookTitleRepo.findBooks(bookTitle.getId());
            if(booksChecks.size()<1) {continue;}
            Long titleId = booksChecks.get(0).getBookTitleId();
            int copiesLeft = booksChecks.get(0).getCopiesLeft();
            Long bookId=  booksChecks.get(0).getBookId();
            boolean pur=booksChecks.get(0).isBooked();

            copiesLeft= copiesLeft-1;
            pur=true;

            bookTitleRepo.getById(titleId).setAvailable(copiesLeft);
            bookRepo.getById(bookId).setBooked(pur);

            BooksOrdered ob3=new BooksOrdered();
            ob3.setBookId(bookId);
            booksOrderedRepo.save(ob3);

        }

        orders.setBooksOrderedList(booksOrdered);
        ordersRepo.save(orders);


        /*

        for( BookTitle ob:bookTitleList){
            List<Book>book1=new ArrayList<>();
            if(ob.getAvailable()>=1){
                ob.setAvailable(ob.getAvailable()-1);
                bookTitleRepo.save(ob);
                book1=ob.getBook();
                for(Book ob2:book1) {
                    if (!ob2.isBooked()) {
                        BooksOrdered ob3=new BooksOrdered();
                        ob3.setBookId(ob2.getId());
                        booksOrderedRepo.save(ob3);

                        booksOrdered.add(ob3);
                        ob2.setBooked(true);
                        bookRepo.save(ob2);
                        break;
                    }
                }
            }
        }
        orders.setBooksOrderedList(booksOrdered);
        ordersRepo.save(orders);
        for(BooksOrdered booksOrdered1:booksOrdered){
            booksOrdered1.setOrders(orders);
            booksOrderedRepo.save(booksOrdered1);
        }

         */

        cart.setPurchased(true);
        cartRepo.save(cart);


        return "success";
    }

    @Override
    public CartAddResponseDTO getCart(Long id){
        if(id==null){
            throw new IllegalArgumentException("user id not true");
        }

        Cart cart= cartRepo.findByUserCartIdAndPurchased(id,false);
        List<BookTitle> bookTitleList= cart.getBook();

        CartAddResponseDTO cartAddResponseDTO=new CartAddResponseDTO();
        List<CartInfo> details = new ArrayList<>();

        for (int i = 0; i < bookTitleList.size(); i++) {
            CartInfo cartInfo = new CartInfo();
            cartInfo.setBookTitleId(bookTitleList.get(i).getId());
            cartInfo.setBookName(bookTitleList.get(i).getBookName());
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
            Long orderId= oldOrders.getId();
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
        if (userId==null){
            throw new IllegalArgumentException("user id not valid");
        }
        if (bookId==null){
            throw new IllegalArgumentException("bookId not valid");
        }

        BooksOrdered booksOrdered= booksOrderedRepo.findByUserBookIdAndBookId(userId,bookId);
        Date returnDate= new Date();
        booksOrdered.setReturnDate(returnDate);
        booksOrderedRepo.save(booksOrdered);

        Date issueDate=booksOrdered.getOrders().getIssueDate();
        Long  timeDifference= returnDate.getTime()-issueDate.getTime();

        Long days_difference = (timeDifference / (1000*60*60*24));
        Long cost=0L;
        if (days_difference<=15){
            cost=days_difference*2;
        }
        else{
            cost=(days_difference-15)*5 + 30;
        }
        ReturnBookResponseDTO returnBookResponseDTO=new ReturnBookResponseDTO(bookId,returnDate,cost);

        Book book= bookRepo.getById(bookId);
        book.setBooked(false);
        bookRepo.save(book);

        BookTitle bookTitle=book.getBookTitle();
        bookTitle.setAvailable(bookTitle.getAvailable()+1);
        bookTitleRepo.save(bookTitle);


        return returnBookResponseDTO;


    }



}

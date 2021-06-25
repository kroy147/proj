package in.ongrid.kshitijroy.service;

import in.ongrid.kshitijroy.dao.BookTitleRepo;
import in.ongrid.kshitijroy.model.dto.BookFilterResponseDTO;
import in.ongrid.kshitijroy.model.dto.BookTitleResponseDTO;
import in.ongrid.kshitijroy.model.entity.BookTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class BookTitleServiceImpl implements BookTitleService {

  @Autowired
  BookTitleRepo bookTitleRepo;

  @Override
  public BookTitleResponseDTO getBookInfo(Long id){

     BookTitle bookTitle=bookTitleRepo.getById(id);
     if(bookTitle==null){
         throw new IllegalArgumentException("id is invalid");
     }
     BookTitleResponseDTO bookTitleResponseDTO=new BookTitleResponseDTO(bookTitle.getId(),bookTitle.getBookName(),
             bookTitle.getAuthorName(),bookTitle.getBookCover(),bookTitle.getAvailable(),
             bookTitle.getCategoryOb().getName());

     return bookTitleResponseDTO;

  }

  @Override
    public BookFilterResponseDTO searchBook(String id,String key){
      if(id.isEmpty() && key.isEmpty()){
          throw new IllegalArgumentException("invalid argument");
      }
      List<BookTitle> bookTitleList;
       if(id.isEmpty()){
           System.out.println(key);
           bookTitleList= bookTitleRepo.findByName(key);
           System.out.println(Arrays.toString(bookTitleList.toArray()));
       }
       else{
           System.out.println("id not null");
            bookTitleList=bookTitleRepo.findByCategoryObId(Long.parseLong(id));
       }
       BookFilterResponseDTO bookFilterResponseDTO= new BookFilterResponseDTO(bookTitleList);
       return  bookFilterResponseDTO;
  }


}

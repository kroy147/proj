package in.ongrid.kshitijroy.service;

import in.ongrid.kshitijroy.model.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UserService {
    UserSignUpResponse userSignUpService(UserSignUpRequestDTO userSignUpRequestDTO);

    UserProfileResponseDTO viewProfile(Long id);

    String updateProfile(UserProfileUpdateRequestDTO userProfileUpdateRequestDTO, Long id);

    CartAddResponseDTO addCart(Long id, CartAddRequestDTO cartAddRequestDTO);

    UserSignInResponseDTO UserSignIn(UserSignInRequestDTO userSignInRequestDTO);

    String issue(Long id);

    CartAddResponseDTO getCart(Long id);


    List<ViewOrder> seeOrder(Long id);

    ReturnBookResponseDTO returnBook(Long userId,Long BookId);

    Integer cartStart(Long userId);


}

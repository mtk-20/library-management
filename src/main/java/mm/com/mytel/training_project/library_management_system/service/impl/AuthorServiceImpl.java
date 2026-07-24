package mm.com.mytel.training_project.library_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.com.mytel.training_project.library_management_system.common.constant.ErrorCode;
import mm.com.mytel.training_project.library_management_system.common.exception.CommonException;
import mm.com.mytel.training_project.library_management_system.common.response.Basic;
import mm.com.mytel.training_project.library_management_system.common.response.ResponseFactory;
import mm.com.mytel.training_project.library_management_system.dto.request.AuthorRequest;
import mm.com.mytel.training_project.library_management_system.dto.request.AuthorUpdateRequest;
import mm.com.mytel.training_project.library_management_system.dto.response.AuthorResponse;
import mm.com.mytel.training_project.library_management_system.entity.Author;
import mm.com.mytel.training_project.library_management_system.repo.AuthorRepo;
import mm.com.mytel.training_project.library_management_system.repo.BookRepo;
import mm.com.mytel.training_project.library_management_system.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<Basic> addAuthor(AuthorRequest authorRequest) {
        Author entity = new Author();
        if (authorRepo.existsByAuthorName(authorRequest.getAuthorName())) {
            throw new CommonException(ErrorCode.DUPLICATE, "Author name already exists.");
        }
        entity.setAuthorName(authorRequest.getAuthorName());
        entity.setBiography(authorRequest.getBiography());
        entity.setNationality(authorRequest.getNationality());
        Author savedAuthor = authorRepo.save(entity);

        AuthorResponse response = new AuthorResponse();
        response.setAuthorName(savedAuthor.getAuthorName());
        response.setBiography(savedAuthor.getBiography());
        response.setNationality(savedAuthor.getNationality());

        return responseFactory.buildSuccess(
                HttpStatus.CREATED,
                response,
                ErrorCode.CREATED,
                "Author create success."
        );
    }

    @Override
    public ResponseEntity<Basic> updateAuthor(Long id, AuthorUpdateRequest authorUpdateRequest) {
        Author entity = authorRepo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Author not found."));
        entity.setBiography(authorUpdateRequest.getBiography());
        authorRepo.save(entity);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                entity,
                ErrorCode.OK,
                "Author update success."
        );
    }

    @Override
    public ResponseEntity<Basic> deleteAuthor(Long id) {
        boolean exists = bookRepo.existsByAuthorId(id);

        if (exists) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "Cannot delete author because it is being used by one or more books."
            );
        }
        Author entity = authorRepo.findById(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND, "Author not found."));
        authorRepo.delete(entity);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                null,
                ErrorCode.OK,
                "Author delete success."
        );
    }

    @Override
    public ResponseEntity<Basic> listAllAuthors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Author> entities = authorRepo.findAllByOrderByAuthorNameAsc(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("authors", entities);

        return responseFactory.buildSuccess(
                HttpStatus.OK,
                response,
                ErrorCode.OK,
                "Author list request success."
        );
    }
}

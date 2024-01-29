package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.Image;
import com.objects.marketbridge.product.service.port.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public void save(Image image) {
        imageJpaRepository.save(image);
    }

    @Override
    public Image findById(Long id) {
        return imageJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void delete(Image image) {
        imageJpaRepository.delete(image);
    }

    public void deleteById(Long id) {
        imageJpaRepository.delete(findById(id));
    }
}
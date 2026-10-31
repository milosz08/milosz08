function initLightbox() {
  const galleryImages = document.querySelectorAll('.gallery__image');
  const lightbox = document.getElementById('lightbox');

  if (!galleryImages.length || !lightbox) {
    return;
  }

  const lightboxImg = document.getElementById('lightboxImg');
  const closeBtn = document.getElementById('closeBtn');
  const prevBtn = document.getElementById('prevBtn');
  const nextBtn = document.getElementById('nextBtn');

  let currentIndex = 0;
  const imagesSrc = Array.from(galleryImages).map(function (img) {
    return img.src;
  });

  galleryImages.forEach(function (img, index) {
    img.addEventListener('click', function () {
      currentIndex = index;
      updateLightboxImage();
      lightbox.classList.add('lightbox--active');
    });
  });

  function updateLightboxImage() {
    lightboxImg.src = imagesSrc[currentIndex];
  }

  nextBtn.addEventListener('click', function (e) {
    e.stopPropagation();
    currentIndex = (currentIndex + 1) % imagesSrc.length;
    updateLightboxImage();
  });

  prevBtn.addEventListener('click', function (e) {
    e.stopPropagation();
    currentIndex = (currentIndex - 1 + imagesSrc.length) % imagesSrc.length;
    updateLightboxImage();
  });

  closeBtn.addEventListener('click', function () {
    lightbox.classList.remove('lightbox--active');
  });

  lightbox.addEventListener('click', function (e) {
    if (e.target === lightbox) {
      lightbox.classList.remove('lightbox--active');
    }
  });

  document.addEventListener('keydown', function (e) {
    if (!lightbox.classList.contains('lightbox--active')) {
      return;
    }
    if (e.key === 'ArrowRight') {
      nextBtn.click();
    }
    if (e.key === 'ArrowLeft') {
      prevBtn.click();
    }
    if (e.key === 'Escape') {
      closeBtn.click();
    }
  });
}

document.addEventListener('DOMContentLoaded', initLightbox);

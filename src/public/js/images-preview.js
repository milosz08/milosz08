'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: images-preview.js
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:50:47
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const imagesInput = document.getElementById('imagesInput');
const imagesPreviewContainer = document.getElementById(
  'imagesPreviewContainer'
);

function createImagePreview(imageSrc, index) {
  imagesPreviewContainer.innerHTML += `
        <div id="imagePreview${index}" class="d-flex flex-column image-preview">
            <div class="flex-fill image-preview-content" style="background-image: url('${imageSrc}')"></div>
        </div>
    `;
}

function onAddImages(e) {
  imagesPreviewContainer.innerHTML = '';
  for (let i = 0, f; i < e.target.files.length, (f = e.target.files[i]); i++) {
    const reader = new FileReader();
    reader.onload = (function (_) {
      return function (e) {
        createImagePreview(e.target.result, i);
      };
    })(f);
    reader.readAsDataURL(f);
  }
}

imagesInput.addEventListener('change', onAddImages);

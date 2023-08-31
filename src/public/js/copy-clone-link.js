'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: copy-clone-link.js
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:50:43
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const copyBtn = document.getElementById('copyCloneButton');
const copyUrl = copyBtn.dataset.cloneUrl ?? '';

async function onCopyContent() {
  await navigator.clipboard.writeText(copyUrl);
  const prevTempInnerContent = copyBtn.innerHTML;
  copyBtn.innerText = 'Copied!';
  setTimeout(() => {
    copyBtn.innerHTML = prevTempInnerContent;
  }, 2000);
}

copyBtn.addEventListener('click', onCopyContent);

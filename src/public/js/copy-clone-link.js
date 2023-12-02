'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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

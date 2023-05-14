/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: copy-clone-link.js
 * Last modified: 12/05/2023, 08:13
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const copyBtn = document.getElementById("copyCloneButton");
const copyUrl = copyBtn.dataset.cloneUrl ?? "";

async function onCopyContent() {
    await navigator.clipboard.writeText(copyUrl);
    const prevTempInnerContent = copyBtn.innerHTML;
    copyBtn.innerText = "Copied!";
    setTimeout(() => {
        copyBtn.innerHTML = prevTempInnerContent;
    }, 2000);
}

copyBtn.addEventListener("click", onCopyContent);

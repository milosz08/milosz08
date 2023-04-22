/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: toggle-password.js
 * Last modified: 22/04/2023, 01:10
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const passwordContainers = document.querySelectorAll("[data-pwd-container]");

const getChildClasses = el => el.firstElementChild.classList.value;
const setChildClass = (el, cls) => el.firstElementChild.classList = cls;

const SHOW_PASSWORD_ICON = "bi bi-eye-fill";
const HIDE_PASSWORD_ICON = "bi bi-eye-slash-fill";

function togglePasswordVisible(input, toggle) {
    if (getChildClasses(toggle) === SHOW_PASSWORD_ICON) {
        setChildClass(toggle, HIDE_PASSWORD_ICON);
        input.setAttribute("type", "text");
    } else {
        setChildClass(toggle, SHOW_PASSWORD_ICON);
        input.setAttribute("type", "password");
    }
}

function onPasswordFocus(input, toggle) {
    if (getChildClasses(toggle) === HIDE_PASSWORD_ICON && input.value.length < 1) {
        togglePasswordVisible(input, toggle);
    }
}

function onPasswordClick(input, toggle) {
    togglePasswordVisible(input, toggle);
    input.focus();
}

passwordContainers.forEach(e => {
    const input = document.getElementById(e.dataset.pwdInputId);
    const toggle = document.getElementById(e.dataset.pwdToggleId);
    input.addEventListener("focus", () => onPasswordFocus(input, toggle));
    toggle.addEventListener("click", () => onPasswordClick(input, toggle));
});

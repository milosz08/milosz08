/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: common-public.js
 * Last modified: 15/04/2023, 18:35
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const preferColorScheme = window.matchMedia("(prefers-color-scheme: dark)");
const changeThemeButton = document.querySelector(".change-theme-btn");

const DISABLE_ANIMATIONS_CLASS = "disable-animations";
const DARK_THEME_CLASS = "dark-theme";
const BUTTON_DARK_TEXT = "Set theme to LIGHT";
const BUTTON_LIGHT_TEXT = "Set theme to DARK";

const addClass = className => document.body.classList.add(className);
const removeClass = className => document.body.classList.remove(className);
const removeClassOnTimeout = (className, timeout = 100) => setTimeout(() => removeClass(className), timeout);
const toggleClass = className => document.body.classList.toggle(className);
const containsClass = className => document.body.classList.contains(className);

function changeThemeFallback() {
    if (containsClass(DARK_THEME_CLASS)) {
        changeThemeButton.innerText = BUTTON_DARK_TEXT;
    } else {
        changeThemeButton.innerText = BUTTON_LIGHT_TEXT;
    }
}

function onChangeColorScheme() {
    addClass(DISABLE_ANIMATIONS_CLASS);
    toggleClass(DARK_THEME_CLASS);
    if (containsClass(DARK_THEME_CLASS)) {
        Cookies.set("THEME", "DARK", { expires: 365 });
    } else {
        Cookies.set("THEME", "LIGHT", { expires: 365 });
    }
    changeThemeFallback();
    removeClassOnTimeout(DISABLE_ANIMATIONS_CLASS);
}

function changeColorTheme(isDark) {
    if (Cookies.get("THEME") === 'DARK') {
        isDark = true;
    } else if (Cookies.get("THEME") === 'LIGHT') {
        isDark = false;
    }
    addClass(DISABLE_ANIMATIONS_CLASS);
    if (isDark) {
        addClass(DARK_THEME_CLASS);
    } else {
        removeClass(DARK_THEME_CLASS);
    }
    changeThemeFallback();
    removeClassOnTimeout(DISABLE_ANIMATIONS_CLASS);
}

preferColorScheme.addEventListener("change", e => changeColorTheme(e.matches));
changeThemeButton.addEventListener("click", onChangeColorScheme);

changeColorTheme(preferColorScheme.matches);

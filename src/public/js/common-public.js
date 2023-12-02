'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */

const preferColorScheme = window.matchMedia('(prefers-color-scheme: dark)');
const changeThemeButton = document.querySelector('.change-theme-btn');

const DISABLE_ANIMATIONS_CLASS = 'disable-animations';
const BUTTON_DARK_TEXT = 'Set theme to LIGHT';
const BUTTON_LIGHT_TEXT = 'Set theme to DARK';

const addClass = className => document.body.classList.add(className);
const removeClass = className => document.body.classList.remove(className);
const removeClassOnTimeout = (className, timeout = 100) =>
  setTimeout(() => removeClass(className), timeout);

function changeThemeFallback() {
  if (document.documentElement.dataset.theme === 'DARK') {
    changeThemeButton.innerText = BUTTON_DARK_TEXT;
  } else {
    changeThemeButton.innerText = BUTTON_LIGHT_TEXT;
  }
}

function onChangeColorScheme() {
  addClass(DISABLE_ANIMATIONS_CLASS);
  if (document.documentElement.dataset.theme === 'LIGHT') {
    localStorage.setItem('THEME', 'DARK');
    document.documentElement.dataset.theme = 'DARK';
  } else {
    localStorage.setItem('THEME', 'LIGHT');
    document.documentElement.dataset.theme = 'LIGHT';
  }
  changeThemeFallback();
  removeClassOnTimeout(DISABLE_ANIMATIONS_CLASS);
}

function changeColorTheme(isDark) {
  addClass(DISABLE_ANIMATIONS_CLASS);
  if (isDark || document.documentElement.dataset.theme === 'DARK') {
    document.documentElement.dataset.theme = 'DARK';
  } else {
    document.documentElement.dataset.theme = 'LIGHT';
  }
  changeThemeFallback();
  removeClassOnTimeout(DISABLE_ANIMATIONS_CLASS);
}

preferColorScheme.addEventListener('change', e => changeColorTheme(e.matches));
changeThemeButton.addEventListener('click', onChangeColorScheme);

changeColorTheme(preferColorScheme.matches);

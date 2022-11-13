function changeColorTheme(isDark) {
    if (isDark) {
        document.body.classList.add('dark-theme');
    } else {
        document.body.classList.remove('dark-theme');
    }
}

const preferColorScheme = window.matchMedia('(prefers-color-scheme: dark)');

changeColorTheme(preferColorScheme.matches);
preferColorScheme.addEventListener('change', e => changeColorTheme(e.matches));

function applyTheme(theme) {
  document.documentElement.setAttribute('data-theme', theme);
}

function updateThemeButtonIcon(themeBtn, themeState) {
  themeBtn.classList.remove('is-light', 'is-dark', 'is-system');
  themeBtn.classList.add(`is-${themeState}`);
}

function getCurrentThemeState() {
  const saved = localStorage.getItem('theme');
  if (saved) {
    return saved;
  }
  return 'system';
}

function initThemeSwitcher() {
  const themeToggleButton = document.getElementById('theme-toggle');
  const darkModeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)');

  if (!themeToggleButton) {
    return;
  }

  updateThemeButtonIcon(themeToggleButton, getCurrentThemeState());

  themeToggleButton.addEventListener('click', () => {
    const currentSaved = localStorage.getItem('theme');
    let nextTheme;
    let nextState;
    if (currentSaved === 'light') {
      nextTheme = 'dark';
      nextState = 'dark';
      localStorage.setItem('theme', 'dark');
    } else if (currentSaved === 'dark') {
      localStorage.removeItem('theme');
      nextTheme = darkModeMediaQuery.matches ? 'dark' : 'light';
      nextState = 'system';
    } else {
      nextTheme = 'light';
      nextState = 'light';
      localStorage.setItem('theme', 'light');
    }
    applyTheme(nextTheme);
    updateThemeButtonIcon(themeToggleButton, nextState);
  });

  darkModeMediaQuery.addEventListener('change', (e) => {
    if (localStorage.getItem('theme')) {
      return;
    }
    const systemTheme = e.matches ? 'dark' : 'light';
    applyTheme(systemTheme);
    updateThemeButtonIcon(themeToggleButton, 'system');
  });
}

document.addEventListener('DOMContentLoaded', initThemeSwitcher);

const savedTheme = localStorage.getItem('theme');
const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

let theme;
let iconState;

if (savedTheme) {
  theme = savedTheme;
  iconState = savedTheme;
} else {
  theme = prefersDark ? 'dark' : 'light';
  iconState = 'system';
}

document.documentElement.setAttribute('data-theme', theme);

document.querySelectorAll('link[data-theme-type]').forEach(link => {
  link.disabled = (link.getAttribute('data-theme-type') !== theme);
});

document.addEventListener('DOMContentLoaded', () => {
  const btn = document.getElementById('theme-toggle');
  if (btn) {
    btn.classList.add('is-' + iconState);
  }
});

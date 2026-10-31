function handleEmailReveal(event) {
  const link = event.currentTarget;
  const encoded = link.getAttribute('data-obfuscated-email');
  if (!encoded) {
    return;
  }
  event.preventDefault();

  const decodedEmail = window.atob(encoded);

  link.textContent = decodedEmail;
  link.setAttribute('href', `mailto:${decodedEmail}`);
  link.removeAttribute('data-obfuscated-email');
  link.removeAttribute('rel');
  link.removeEventListener('click', handleEmailReveal);
}

function attachEmailListener(link) {
  link.addEventListener('click', handleEmailReveal);
}

function initEmailMangler() {
  const emailLinks = document.querySelectorAll('a[data-obfuscated-email]');
  emailLinks.forEach(attachEmailListener);
}

document.addEventListener('DOMContentLoaded', initEmailMangler);

/* global hljs */

(function (code, language) {
  if (language && hljs.getLanguage(language)) {
    return hljs.highlight(code, {language: language}).value;
  }
  return hljs.highlightAuto(code).value;
})

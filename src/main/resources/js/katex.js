/* global katex */

(function (texString, isDisplayMode) {
  try {
    return katex.renderToString(texString, {
      displayMode: isDisplayMode,
      throwOnError: false,
      errorColor: "#cc0000"
    });
  } catch (err) {
    return texString;
  }
})

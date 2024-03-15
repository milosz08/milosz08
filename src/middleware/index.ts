import { AVAILABLE_LANGS, DEFAULT_LANG, type TLanguage } from '@/i18n';
import { defineMiddleware } from 'astro/middleware';

export const onRequest = defineMiddleware(async ({ url }, next) => {
  const [, lang] = url.pathname.split('/');
  if (AVAILABLE_LANGS.includes(lang as TLanguage)) {
    return next();
  }
  return Response.redirect(`${url.origin}/${DEFAULT_LANG}${url.pathname}`);
});

import { ui } from '@/i18n/ui-translations';

export const AVAILABLE_LANGS: TLanguage[] = ['pl', 'en'];
export const DEFAULT_LANG = 'en';

export type TLanguage = 'pl' | 'en';

export function getLangFromUrl(url: URL): TLanguage {
  const [, lang] = url.pathname.split('/');
  if (AVAILABLE_LANGS.includes(lang as TLanguage)) {
    return lang as TLanguage;
  }
  return DEFAULT_LANG;
}

export function toggleLangHref(url: string, lang: TLanguage): string {
  const urlWithoutLang = url.replace(/\/(en|pl)/i, '');
  return `/${lang}${urlWithoutLang}`;
}

export function isLang(url: string, lang: TLanguage): boolean {
  return lang === url.substring(1, 3);
}

export function useTranslations(lang: TLanguage) {
  return function t(key: keyof (typeof ui)[typeof DEFAULT_LANG]) {
    return ui[lang][key] || ui[DEFAULT_LANG][key];
  };
}

export function i18nHref(originalUrl: string, lang: TLanguage): string {
  return `/${lang}${originalUrl}`;
}

import { AVAILABLE_LANGS } from '@/i18n';
import { getEntry } from 'astro:content';

export type TEntry = 'about' | 'projects';

export async function fabricateMdxI18nStaticPaths(entry: TEntry) {
  return await Promise.all(
    AVAILABLE_LANGS.map(async (lang) => {
      const pageMdx = await getEntry('misc', `${lang}/${entry}`);
      return {
        params: { lang },
        props: pageMdx,
      };
    }),
  );
}

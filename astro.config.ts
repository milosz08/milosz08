/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import mdx from '@astrojs/mdx';
import { defineConfig } from 'astro/config';

let BASE_URL = 'https://miloszgilga.pl';
if (import.meta.env.DEV) {
  BASE_URL = 'http://localhost:3254';
}

export default defineConfig({
  integrations: [mdx()],
  site: BASE_URL,
});

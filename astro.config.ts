/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import mdx from "@astrojs/mdx";
import { defineConfig } from "astro/config";

export default defineConfig({
  integrations: [mdx()],
});

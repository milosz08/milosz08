import { defineCollection, z } from 'astro:content';

const projectCollection = defineCollection({
  type: 'content',
  schema: z.object({
    title: z.string(),
    index: z.number(),
    type: z.enum(['impl', 'rd', 'part']),
  }),
});

const miscCollection = defineCollection({});

export const collections = {
  projects: projectCollection,
  misc: miscCollection,
};

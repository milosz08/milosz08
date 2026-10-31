<#-- @ftlvariable name="seo" type="pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo" -->
<#-- @ftlvariable name="langPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="groupedTags" type="java.util.List<pl.miloszgilga.jargonaut.core.dto.AlphabeticalGroup>" -->
<#-- @ftlvariable name="tagCloud" type="java.util.List<pl.miloszgilga.jargonaut.page.tag.dto.TagCloudView>" -->

<#import "../layout.ftl" as layout>
<#import "../macros/pillar-header.ftl" as PillarHeader>
<#import "../macros/word-cloud.ftl" as WordCloud>
<#import "../macros/alphabet-grid.ftl" as AlphabetGrid>

<@layout.page seo=seo>
  <@PillarHeader.render seo=seo />
  <main class="pillar">
    <#include "../fragments/md-content.ftl">
    <@WordCloud.render wordCloud=tagCloud page="tag"/>
    <@AlphabetGrid.render groups=groupedTags ; item>
      <a href="${langPrefix}/tag/${item.slug}" class="alphabet-group__link">
        <span class="alphabet-group__name">${item.name}</span>
        <span class="alphabet-group__count">${item.projectsCount}</span>
      </a>
    </@AlphabetGrid.render>
  </main>
</@layout.page>

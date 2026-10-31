<#-- @ftlvariable name="seo" type="pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo" -->
<#-- @ftlvariable name="projectCategories" type="java.util.List<pl.miloszgilga.jargonaut.page.project.dto.ProjectCategoriesTableView>" -->
<#-- @ftlvariable name="aggregatedCalendar" type="pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendar" -->

<#import "../layout.ftl" as layout>
<#import "../macros/pillar-header.ftl" as PillarHeader>
<#import "../macros/contribution-calendar.ftl" as ContributionCalendar>
<#import "../macros/language-bar.ftl" as LanguageBar>
<#import "../macros/projects-table.ftl" as ProjectsTable>

<@layout.page seo=seo>
  <@PillarHeader.render seo=seo>
    <@ContributionCalendar.render calendar=aggregatedCalendar />
  </@PillarHeader.render>
  <main class="pillar">
    <#include "../fragments/md-content.ftl">
    <#list projectCategories as projectCategory>
      <h2>${projectCategory.name}</h2>
      <@LanguageBar.render stats=projectCategory.categoryLanguages />
      <@ProjectsTable.render projects=projectCategory.projects />
    </#list>
  </main>
</@layout.page>

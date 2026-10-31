<#-- @ftlvariable name="seo" type="pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo" -->
<#-- @ftlvariable name="i18n" type="java.util.Map" -->
<#-- @ftlvariable name="pinnedProjects" type="java.util.List<pl.miloszgilga.jargonaut.page.project.dto.ProjectCategoriesTableView>" -->
<#-- @ftlvariable name="aggregatedLanguages" type="java.util.List<pl.miloszgilga.jargonaut.core.extractor.stats.LanguageStat>" -->
<#-- @ftlvariable name="globals" type="pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata" -->
<#-- @ftlvariable name="contributionCalendar" type="pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendar" -->
<#-- @ftlvariable name="researchList" type="java.util.List<pl.miloszgilga.jargonaut.page.education.dto.ResearchView>" -->

<#import "../layout.ftl" as layout>
<#import "../macros/contribution-calendar.ftl" as ContributionCalendar>
<#import "../macros/language-bar.ftl" as LanguageBar>
<#import "../macros/projects-table.ftl" as ProjectsTable>
<#import "../macros/research-table.ftl" as ResearchTable>

<@layout.page seo=seo>
  <header class="pillar-header">
    <div class="pillar-header__wrapper">
      <div class="pillar-caption pillar-caption--space-up">
        <#if globals.honorificPrefixes?? && globals.honorificPrefixes?has_content>
          <p class="pillar-caption__honorific-prefixes">
            ${globals.honorificPrefixes?join(" ")}
          </p>
        </#if>
        <#assign nameClasses = "pillar-caption__name">
        <#if !(globals.honorificPrefixes?? && globals.honorificPrefixes?has_content)>
          <#assign nameClasses += " pillar-caption__name--space-up">
        </#if>
        <#if !(globals.honorificSuffixes?? && globals.honorificSuffixes?has_content)>
          <#assign nameClasses += " pillar-caption__name--space-down">
        </#if>
        <h1 class="${nameClasses}">
          ${globals.name}
        </h1>
        <#if globals.honorificSuffixes?? && globals.honorificSuffixes?has_content>
          <p class="pillar-caption__honorific-suffixes">
            ${globals.honorificSuffixes?join(", ")}
          </p>
        </#if>
      </div>
      <@ContributionCalendar.render calendar=contributionCalendar />
    </div>
  </header>
  <main class="pillar">
    <#include "../fragments/md-content.ftl">
    <h2>${i18n.project.selectedProjects}</h2>
    <@LanguageBar.render stats=aggregatedLanguages />
    <@ProjectsTable.render projects=pinnedProjects />
    <@ResearchTable.render researchList=researchList />
  </main>
</@layout.page>

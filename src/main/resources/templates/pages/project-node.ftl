<#-- @ftlvariable name="seo" type="pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo" -->
<#-- @ftlvariable name="tocPlaceholderId" type="java.lang.String" -->
<#-- @ftlvariable name="calendar" type="pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendarView" -->
<#-- @ftlvariable name="languages" type="java.util.List<pl.miloszgilga.jargonaut.core.extractor.stats.LanguageStat>" -->
<#-- @ftlvariable name="project" type="pl.miloszgilga.jargonaut.page.project.dto.ProjectDetailsView" -->
<#-- @ftlvariable name="i18n" type="java.util.Map" -->
<#-- @ftlvariable name="galleryImages" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="technologies" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="readingTime" type="java.lang.Integer" -->
<#-- @ftlvariable name="mdContent" type="java.lang.String" -->

<#import "../layout.ftl" as layout>
<#import "../macros/contribution-calendar.ftl" as ContributionCalendar>
<#import "../macros/resource-section.ftl" as ResourceSection>
<#import "../macros/language-bar.ftl" as LanguageBar>
<#import "../macros/gallery.ftl" as Gallery>
<#import "../macros/technologies.ftl" as Technologies>

<@layout.page seo=seo>
  <header class="pillar-header">
    <div class="pillar-header__wrapper pillar-header__wrapper--${project.status}">
      <div class="pillar-caption">
        <h1 class="pillar-caption__head">${project.name}</h1>
        <p class="pillar-caption__description">${project.description}</p>
      </div>
      <div class="status-badge__container">
        <#if project.recentlyUpdated>
          <span class="status-badge status-badge--header status-badge--updated">
            ${i18n.status.updated}
          </span>
        </#if>
        <div class="status-badge status-badge--header status-badge--${project.status}">
          ${i18n.status[project.status]}
        </div>
      </div>
      <@ContributionCalendar.render calendar=calendar />
      <div class="project-resources">
        <#assign sections = [
        {"label": i18n.project.repositories, "icon": "repo", "items": (project.repoUrls)![]},
        {"label": i18n.project.documentation, "icon": "book", "items": (project.docsUrls)![]},
        {"label": i18n.project.download, "icon": "download", "items": (project.downloadUrls)![]},
        {"label": i18n.project.demo, "icon": "demo", "items": (project.demoUrls)![]}
        ] />
        <#list sections as section>
          <@ResourceSection.render label=section.label icon=section.icon items=section.items projectName=project.name />
        </#list>
      </div>
    </div>
  </header>
  <div id="${tocPlaceholderId}"></div>
  <main class="pillar">
    <@LanguageBar.render stats=languages />
    <#if mdContent?has_content>
      <div class="pillar-reading-time">
        <span class="pillar-reading-time__label">${i18n.project.readingTime}:</span>
        <span class="pillar-reading-time__value">${readingTime}min</span>
      </div>
    </#if>
    <div class="pillar-wrapper">
      <#include "../fragments/md-content.ftl">
      <@Gallery.render images=galleryImages urlPrefix="/${project.id}/"/>
      <@Technologies.render technologies=technologies![] />
    </div>
  </main>
</@layout.page>

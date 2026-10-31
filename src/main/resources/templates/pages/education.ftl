<#-- @ftlvariable name="seo" type="pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo" -->
<#-- @ftlvariable name="educationList" type="java.util.List<pl.miloszgilga.jargonaut.page.education.dto.EducationView>" -->
<#-- @ftlvariable name="researchList" type="java.util.List<pl.miloszgilga.jargonaut.page.education.dto.ResearchView>" -->
<#-- @ftlvariable name="resume" type="pl.miloszgilga.jargonaut.page.education.model.Resume" -->

<#import "../layout.ftl" as layout>
<#import "../macros/education-table.ftl" as EducationTable>
<#import "../macros/research-table.ftl" as ResearchTable>
<#import "../macros/pillar-header.ftl" as PillarHeader>

<@layout.page seo=seo>
  <header class="pillar-header">
    <div class="pillar-header__wrapper">
      <div class="pillar-caption pillar-caption--space-up">
        <h1 class="pillar-caption__name">${seo.pageName}</h1>
      </div>
      <#if resume??>
        <div class="pillar-download">
          <a href="${resume.url}" class="pillar-download__btn">
            <span class="pillar-download__icon"></span>
            <span>${resume.label}</span>
          </a>
        </div>
      </#if>
    </div>
  </header>
  <main class="pillar">
    <#include "../fragments/md-content.ftl">
    <@EducationTable.render educationList=educationList />
    <@ResearchTable.render researchList=researchList />
  </main>
</@layout.page>

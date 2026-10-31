<#-- @ftlvariable name="seo" type="pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo" -->

<#import "../layout.ftl" as layout>
<#import "../macros/pillar-header.ftl" as PillarHeader>

<@layout.page seo=seo>
  <@PillarHeader.render seo=seo />
  <main class="pillar">
    <#include "../fragments/md-content.ftl">
  </main>
</@layout.page>

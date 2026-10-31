<#-- @ftlvariable name="seo" type="pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo" -->

<#macro render seo>
  <header class="pillar-header">
    <div class="pillar-header__wrapper">
      <div class="pillar-caption pillar-caption--space-between">
        <h1 class="pillar-caption__name">${seo.pageName}</h1>
      </div>
      <#nested />
    </div>
  </header>
</#macro>

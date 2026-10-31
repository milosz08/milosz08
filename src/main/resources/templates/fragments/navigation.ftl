<#-- @ftlvariable name="i18n" type="java.util.Map" -->
<#-- @ftlvariable name="languageLinks" type="java.util.List<pl.miloszgilga.jargonaut.core.template.generator.dto.LanguageView>" -->
<#-- @ftlvariable name="navLinks" type="java.util.List<pl.miloszgilga.jargonaut.core.template.generator.dto.NavLinkView>" -->
<#-- @ftlvariable name="langPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#-- @ftlvariable name="breadcrumbs" type="java.util.List<pl.miloszgilga.jargonaut.core.template.generator.dto.BreadcrumbView>" -->
<#-- @ftlvariable name="sitemapTree" type="java.util.List<pl.miloszgilga.jargonaut.core.template.generator.dto.SitemapNodeView>" -->

<#import "../macros/nav-dropdown.ftl" as NavDropdown>

<nav class="navbar">
  <div class="navbar__inner">
    <div class="navbar__left">
      <div class="navbar__dropdown dropdown dropdown--sitemap">
        <input id="sitemap-btn" class="dropdown__checkbox" type="checkbox">
        <label for="sitemap-btn" class="dropdown__trigger">${i18n.nav.caption}&nbsp;&#x25BE;</label>
        <ul class="dropdown__menu">
          <#list sitemapTree![] as rootNode>
            <@NavDropdown.render node=rootNode />
          </#list>
        </ul>
      </div>
      <#if breadcrumbs?? && breadcrumbs?has_content>
        <div class="navbar__breadcrumb">
          <#list breadcrumbs as crumb>
            <#if crumb.current>
              <span class="navbar__link navbar__link--current">${crumb.label}</span>
            <#else>
              <a href="${crumb.url}" class="navbar__link">${crumb.label}</a>
              &rarr;
            </#if>
          </#list>
        </div>
      </#if>
    </div>
    <div class="navbar__right">
      <#list navLinks![] as navLink>
        <#if navLink.current>
          <a class="navbar__text navbar__link navbar__link--active">
            ${navLink.label}
          </a>
        <#else>
          <a class="navbar__text navbar__link" href="${navLink.url}">
            ${navLink.label}
          </a>
        </#if>
      </#list>
      <div class="navbar__dropdown dropdown">
        <input id="lang-links-btn" class="dropdown__checkbox" type="checkbox">
        <label for="lang-links-btn" class="dropdown__trigger" title="Language">
          <span class="dropdown__icon dropdown__icon--lang"></span>&#x25BE;
        </label>
        <ul class="dropdown__menu">
          <#list languageLinks as lang>
            <li class="dropdown__item">
              <#if lang.active>
                <span class="dropdown__current">${lang.displayName}</span>
              <#else>
                <a href="${lang.url}" class="dropdown__link">${lang.displayName}</a>
              </#if>
            </li>
          </#list>
        </ul>
      </div>
    </div>
  </div>
</nav>
<div class="navbar-spacer"></div>

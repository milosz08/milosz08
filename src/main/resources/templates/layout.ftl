<#-- @ftlvariable name="syntaxParserAssets" type="java.util.List" -->
<#-- @ftlvariable name="mathParserAssets" type="java.util.List" -->

<#macro page seo>
  <!DOCTYPE html>
  <html lang="${seo.htmlLanguage}">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="generator" content="JArgonaut">
    <meta name="robots" content="index, follow">
    <#if seo.authorName??>
      <meta name="author" content="${seo.authorName}">
    </#if>
    <title>${seo.pageTitle}</title>
    <#if seo.pageDescription??>
      <meta name="description" content="${seo.pageDescription}">
    </#if>
    <#if seo.faviconUrl??>
      <link rel="icon" type="image/png" href="${seo.faviconUrl}">
      <link rel="apple-touch-icon" href="${seo.faviconUrl}">
    </#if>
    <link rel="canonical" href="${seo.canonicalUrl}">
    <#list seo.alternateUrls as hreflang, url>
      <link rel="alternate" hreflang="${hreflang}" href="${url}">
    </#list>
    <#if seo.rssFeedUrl??>
      <link rel="alternate" type="application/rss+xml" title="${seo.authorName} - RSS"
            href="${seo.canonicalUrl}/${seo.rssFeedUrl}">
    </#if>
    <#if seo.authorName??>
      <meta property="og:site_name" content="${seo.authorName}">
    </#if>
    <meta property="og:title" content="${seo.pageTitle}">
    <meta property="og:type" content="website">
    <meta property="og:url" content="${seo.canonicalUrl}">
    <meta property="og:locale" content="${seo.ogLocale}">
    <meta property="og:image" content="${seo.ogImageUrl}">
    <#if seo.pageDescription??>
      <meta property="og:description" content="${seo.pageDescription}">
    </#if>
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${seo.pageTitle}">
    <meta name="twitter:image" content="${seo.ogImageUrl}">
    <#if seo.pageDescription??>
      <meta name="twitter:description" content="${seo.pageDescription}">
    </#if>
    <#if seo.gtmId?has_content>
      <link rel="preconnect" href="https://www.googletagmanager.com">
    </#if>
    <script type="application/ld+json">${seo.jsonLdScript?no_esc}</script>
    <#if seo.gtmId?has_content>
      <script type="text/javascript">
        const dataLayerName = "dataLayer";
        window[dataLayerName] = window[dataLayerName] || [];
        window[dataLayerName].push({
          'gtm.start': new Date().getTime(),
          event: 'gtm.js'
        });
        const dataLayerParam = dataLayerName !== "dataLayer" ? "&l=" + dataLayerName : "";
        const gtmScript = document.createElement("script");
        gtmScript.async = true;
        gtmScript.src = "https://www.googletagmanager.com/gtm.js?id=${seo.gtmId}" + dataLayerParam;
        const firstScript = document.getElementsByTagName("script")[0];
        firstScript.parentNode.insertBefore(gtmScript, firstScript);
      </script>
    </#if>
    <#if syntaxParserAssets?has_content>
      <#list syntaxParserAssets as assetUrl>
        <link rel="stylesheet" href="${assetUrl}">
      </#list>
    </#if>
    <#if mathParserAssets?has_content>
      <#list mathParserAssets as assetUrl>
        <link rel="stylesheet" href="${assetUrl}">
      </#list>
    </#if>
    <#if seo.styleFileNames?has_content>
      <#list seo.styleFileNames as styleFileName>
        <link rel="stylesheet" href="/${styleFileName}">
      </#list>
    </#if>
    <#if seo.scriptFileNames?has_content>
      <#list seo.scriptFileNames as scriptFileName>
        <script type="text/javascript" src="/${scriptFileName}" defer></script>
      </#list>
    </#if>
  </head>
  <body>
  <#if seo.gtmId?has_content>
    <noscript>
      <iframe src="https://www.googletagmanager.com/ns.html?id=${seo.gtmId}" height="0" width="0"
              style="display:none;visibility:hidden"></iframe>
    </noscript>
  </#if>
  <#include "fragments/navigation.ftl">
  <#nested>
  <#include "fragments/footer.ftl">
  </body>
  </html>
</#macro>

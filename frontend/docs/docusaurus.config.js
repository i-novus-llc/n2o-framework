const CONFIG = require('./src/ci-config.json')

/** @type {import('@docusaurus/types').DocusaurusConfig} */
module.exports = {
    title: CONFIG.title || 'N2O',
    //tagline: 'The tagline of my site',
    url: CONFIG.url,
    baseUrl: CONFIG.baseUrl || '/',
    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',
    favicon: 'img/favicon.ico',
    organizationName: CONFIG.organizationName || 'Ай-Новус',
    projectName: 'Документация N2O',
    themeConfig: {
        navbar: {
            title: CONFIG.navbarTitle || '',
            logo: {
                alt: CONFIG.navbarIconAlt || 'Документация N2O',
                src: CONFIG.navbarIconSrc || 'img/logo_dark.png',
                srcDark: CONFIG.navbarIconSrcLight || 'img/logo_light.png',
            },
            items: [
                {
                    to: 'docs/',
                    activeBasePath: 'docs',
                    label: 'Документация',
                    position: 'left',
                },
                { to: 'blog', label: 'Блог', position: 'left' },
                {
                    href: 'https://github.com/i-novus-llc/n2o-framework',
                    label: 'GitHub',
                    position: 'right',
                },
            ],
        },
        footer: {
            style: 'dark',
            copyright: `Copyright © ${new Date().getFullYear()} N2O, Inc. Built with I-Novus.`,
        },
    },
    presets: [
        [
            '@docusaurus/preset-classic',
            {
                docs: {
                    sidebarPath: require.resolve('./sidebars.js'),
                },
                blog: {
                    showReadingTime: true,
                },
                theme: {
                    customCss: require.resolve('./src/css/custom.css'),
                },
            },
        ],
    ],
    plugins: ['docusaurus-plugin-sass'],
}

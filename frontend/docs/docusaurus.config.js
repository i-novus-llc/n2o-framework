const CONFIG = require('./src/ci-config.json')

/** @type {import('@docusaurus/types').DocusaurusConfig} */
module.exports = {

    /* Обязательные поля */

    // Текст вкладки браузера и h1 на дефолтной главной странице (useDocusaurusContext().siteConfig.title)
    title: CONFIG.title || 'N2O',
    // Хост вашего сайта без пути и слеша в конце. Ваще ХЗ на кой оно нужно и на что влияет. Скорее что-то для SEO.
    url: CONFIG.url || 'https://n2o.i-novus.ru',
    // Путь, по которому нужно открывать документацию (аналог contextPath), н.р. /docusaurus/
    // Требуется для корректных ссылок на статику
    baseUrl: CONFIG.baseUrl || '/',

    /* Опциональные поля */

    //tagline: 'The tagline of my site',
    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',
    // Ссылка относительно папки static. Можно указать http адрес
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

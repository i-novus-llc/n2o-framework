import CONFIG from './src/ci-config.json'

let contextPath = CONFIG.docusaurusUrl || '/'

if (contextPath[contextPath.length - 1] !== '/') {
    contextPath += '/'
}

const description = 'N2O Framework - библиотека с открытым исходным кодом, написанная на Java и ReactJS. Позволяет создавать веб приложения со сложными пользовательскими интерфейсами без глубоких знаний веб технологий и фронтенд фреймворков.'

const WORDS_PER_MINUTE = 200
const readingTimeWithoutIntlSegmenter = ({ content }) => {
    const words = content.split(/\s+/).filter(Boolean).length
    return Math.round((words / WORDS_PER_MINUTE) * 100) / 100
}

/** @type {import('@docusaurus/types').DocusaurusConfig} */
export default {
    /* Обязательные поля */

    // Текст вкладки браузера и h1 на дефолтной главной странице (useDocusaurusContext().siteConfig.title)
    title: CONFIG.title || 'N2O Framework',
    // Хост вашего сайта без пути и слеша в конце. Ваще ХЗ на кой оно нужно и на что влияет. Скорее что-то для SEO.
    tagline: CONFIG.title || 'Java и ReactJS библиотека, позволяющая создавать web приложения со сложными пользовательскими интерфейсами без глубоких знаний web технологий и frontend фреймворков',
    url: CONFIG.url || 'https://n2o.i-novus.ru',
    // Путь, по которому нужно открывать документацию (аналог contextPath), н.р. /docusaurus/
    // Требуется для корректных ссылок на статику
    baseUrl: contextPath,

    /* Опциональные поля */

    //tagline: 'The tagline of my site',
    markdown: {
        hooks: {
            onBrokenMarkdownLinks: 'warn',
        },
    },
    onBrokenLinks: 'throw',
    onBrokenAnchors: 'ignore',
    // Ссылка относительно папки static. Можно указать http адрес
    favicon: 'img/favicon.ico',
    organizationName: CONFIG.organizationName || 'Ай-Новус',
    projectName: 'N2O Framework',
    themeConfig: {
        prism: {
            theme: require('prism-react-renderer').themes.oceanicNext,
        },
        navbar: {
            title: CONFIG.navbarTitle || '',
            logo: {
                alt: CONFIG.navbarIconAlt || 'N2O Framework',
                src: CONFIG.navbarIconSrc || 'img/logo_dark.png',
                srcDark: CONFIG.navbarIconSrcLight || 'img/logo_light.png',
            },
            items: [
                {
                    type: 'doc',
                    docId: 'introduction',
                    label: 'Документация',
                    position: 'left',
                },
                {
                    type: 'doc',
                    docId: 'buttons',
                    label: 'Примеры',
                    position: 'left',
                },
                {
                    href: 'https://sandbox.i-novus.ru/',
                    label: 'Песочница',
                    position: 'left',
                },
                {
                    to: 'blog',
                    label: 'Блог',
                    position: 'left',
                },
                {
                    type: 'custom-versions',
                    position: 'right',
                },
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
    metadata: [{
        name: 'description',
        content: description
    }, {
        property: 'og:description',
        content: description
    }],
},
    presets: [
        [
            '@docusaurus/preset-classic',
            {
                docs: {
                    sidebarPath: require.resolve('./sidebars.js'),
                    disableVersioning: false,
                    lastVersion: 'current',
                    onlyIncludeVersions: ['current'],
                    versions: {
                        current: {
                            label: `${CONFIG.n2oVersion} 🚧`,
                        },
                    },
                },
                blog: {
                    readingTime: readingTimeWithoutIntlSegmenter,
                },
                theme: {
                    customCss: require.resolve('./src/css/custom.css'),
                },

            },
        ],
    ],

    plugins: [
        [
            '@easyops-cn/docusaurus-search-local',
            {
                hashed: true,
                docsRouteBasePath: 'docs',
                docsDir: 'docs',
                indexDocs: true,
                indexBlog: false,
                language: ['en', 'ru'],
            },
        ],
    ],
}

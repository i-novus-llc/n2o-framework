// Префикс для manual chunks в статике
export const CHUNK_PREFIX = 'vendor-'

// Список групп пакетов для manualChunks
export const VENDORS = {
    'react': [
        'react',
        'react-dom',
        'react-router-dom',
        'history',
        'react-helmet',
        'i18next',
        'react-i18next',
        'prop-types',
    ],
    'redux': [
        'redux',
        'redux-thunk',
        'redux-saga',
        'connected-react-router',
        'react-redux',
        '@reduxjs/toolkit',
        'redux-actions',
    ],
    'core-utils': [
        'dayjs',
        'classnames',
        'numeral',
        'tslib',
        'url-parse',
        'query-string',
        'deepmerge',
        'lodash',
        /* 
         * Костыли:
         * Не прямая зависимость,но если не указать явно в отдельный модуль,
         * то rollup подмешивает их в бандлы, которые мы хотим тянуть лениво, а получаем на старте приложения
         */ 
        'lodash.get',
        'lodash.isequal',
        '@babel/runtime',
    ],

    // Зависимости, от которых нужно со временем избавиться
    'deprecated': [
        'moment',
        'axios',
        'flat',
    ],

    // UI компоненты
    'ui-libs': [
        'react-copy-to-clipboard',
        'react-textarea-autosize',
        'scroll-into-view-if-needed',
        'react-onclickoutside',
        'react-resize-detector',
        'react-hotkeys',
        'react-popper',
        'rc-slider',
        'rc-tree-select',
        'rc-tree',
        'rc-collapse',
        'rc-drawer',
        'rc-switch',
        'reactstrap',
        'react-virtualized',
        '@maskito/core',
        '@maskito/kit',
        '@maskito/react',
    ],
    'font-awesome': [
        'font-awesome',
        '@fortawesome/fontawesome-svg-core',
        '@fortawesome/free-solid-svg-icons',
        '@fortawesome/react-fontawesome',
        '@fortawesome/fontawesome-free'
    ],

    // lazy load
    'code-editor': [
        'react-ace',
        'brace',
    ],
    'text-editor': [
        'react-draft-wysiwyg',
        'draft-js',
        'draftjs-to-html',
        'html-to-draftjs'
    ],
    'markdown': [
        'remark-gfm',
        'rehype-raw',
        'react-markdown',
    ],
    'highlighter': [
        'react-syntax-highlighter',
        'refractor',
    ],
    'recharts': ['recharts'],
    // 'calendar': ['react-big-calendar'],
    'stompjs': [
        'stompjs',
        'sockjs-client',
    ],
};

// List of modules that rollup sometimes bundles with manual chunks, causing those chunks to be eager-loaded
export const ROLLUP_COMMON_MODULES = [
    'vite/preload-helper',
    'vite/modulepreload-polyfill',
    'vite/dynamic-import-helper',
    'commonjsHelpers',
    'commonjs-dynamic-modules',
    '__vite-browser-external'
];

export function getModuleName(id: string) {
    return id.split('\/node_modules\/')[1] || ''
}

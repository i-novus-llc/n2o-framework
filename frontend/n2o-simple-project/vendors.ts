// Префикс для manual chunks в статике
export const CHUNK_PREFIX = 'vendor-'

// Список групп пакетов для manualChunks
export const VENDORS = {
    // React и связанные библиотеки
    'react': ['react-is'],
    'react-router': ['react-router-dom'],
    'react-i18n': ['i18next', 'react-i18next'],
    'react-redux': ['@reduxjs/toolkit', 'redux-actions'],
    'react-utils': [
        'react-copy-to-clipboard',
        'react-textarea-autosize',
        'scroll-into-view-if-needed',
        'react-onclickoutside',
        'react-resize-detector',
        'react-hotkeys'
    ],

    // UI компоненты
    'reactstrap': ['reactstrap'],
    'recharts': ['recharts'],
    'rc-components': [
        'rc-slider',
        'rc-tree-select',
        'rc-tree',
        'rc-collapse',
        'rc-drawer',
        'rc-switch',
    ],
    'react-big-calendar': ['react-big-calendar'],
    'react-virtualized': ['react-virtualized'],

    // Редакторы
    'code-editor': ['react-ace', 'brace'],
    'rich-text-editor': [
        'react-draft-wysiwyg',
        'draft-js',
        'draftjs-to-html',
        'html-to-draftjs'
    ],

    // Тяжелые утилиты, сборка отдельными chunks для уменьшения размера misc
    'date': ['dayjs', 'moment'],
    'lodash': ['lodash', 'lodash-es', 'lodash.isequal', 'lodash.get', 'lodash.isobject', 'lodash.isboolean', 'lodash.isequalwith'],
    'axios': ['axios'],
    'syntax': ['react-syntax-highlighter'],
    'dropzone': ['react-dropzone'],
    'font-awesome': [
        'font-awesome',
        '@fortawesome/fontawesome-svg-core',
        '@fortawesome/free-solid-svg-icons',
        '@fortawesome/react-fontawesome',
        '@fortawesome/fontawesome-free'
    ],
    'refractor': ['refractor'],
    'ace-builds': ['ace-builds'],
    'parse5': ['parse5'],
    'stompjs': ['stompjs'],
    'sockjs': ['sockjs'],
    'babel': ['babel'],
    'd3': ['d3'],
};

export function getPackageName(id: string) {
    const match = id.match(/[\\/]node_modules[\\/](@[^\\/]+\/[^\\/]+|[^\\/]+)/)
    return match ? match[1] : ''
}

module.exports = {
    customSyntax: 'postcss-scss',
    extends: 'stylelint-config-standard',
    plugins: [
        'stylelint-scss',
        'stylelint-order',
        '@stylistic/stylelint-plugin'
    ],
    rules: {
        // Отступ в 4 пробела
        '@stylistic/indentation': 4,
        '@stylistic/block-opening-brace-newline-after': 'always',
        '@stylistic/block-closing-brace-newline-before': 'always',

        // Отключаем проверку аннотаций (так как прим. !default - это SCSS)
        'annotation-no-unknown': null,

        // Отключаем проверку цветовых функций (чтобы не переписывать rgba → rgb и т.д.)
        'color-function-alias-notation': null,
        'color-function-notation': null,
        'alpha-value-notation': null,

        // Правила для кебаб-кейс селекторов (отключены)
        'selector-class-pattern': null,
        'keyframes-name-pattern': null,

        // Отключена проверка значений свойств, так как они могут содержать SCSS-переменные,
        // 'declaration-property-value-no-unknown': [true, { severity: 'warning' }],
        'declaration-property-value-no-unknown': null,

        // Запрет на использование & при формировании имени класса: &-focused {}
        'scss/selector-no-union-class-name': true,
        // Не использовать & в селекторе, если он не нужен: & a {}
        'scss/selector-no-redundant-nesting-selector': true,
        'at-rule-no-unknown': null,
        'scss/at-rule-no-unknown': true,
        // Отключено, нужно менять импорты и настраивать vite
        'import-notation': null,
    },
};

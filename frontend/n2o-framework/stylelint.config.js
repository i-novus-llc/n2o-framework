module.exports = {
    extends: 'stylelint-config-standard',
    plugins: [
        'stylelint-scss',
        'stylelint-order',
    ],
    rules: {
        // Отступ в 4 пробела
        indentation: 4,
        // Следующие 2 правила запрещают однострочные CSS-классы
        'block-opening-brace-newline-after': 'always',
        'block-closing-brace-newline-before': 'always',
        // Запрет на использование & при формировании имени класса: &-focused {}
        'scss/selector-no-union-class-name': true,
        // Не использовать & в селекторе, если он не нужен: & a {}
        'scss/selector-no-redundant-nesting-selector': true,
        // Fix: https://github.com/stylelint/stylelint/issues/3190
        'at-rule-no-unknown': null,
        'scss/at-rule-no-unknown': true,
    },
}

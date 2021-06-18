/**
 * Возвращает тип createElement() на основе props компонента.
 * Полезно для расчета того, какой тип компонента должен отображаться.
 *
 * @param {function} Component компонент React.
 * @param {object} props атрибуты компонента
 * @param {function} [getDefault] функция для определения дефолтного значения.
 * @returns {string|function} ReactElement
 */
function getElementType(Component, props, getDefault) {
    const { defaultProps = {} } = Component

    if (props.as && props.as !== defaultProps.as) { return props.as }

    if (getDefault) {
        const computedDefault = getDefault()

        if (computedDefault) { return computedDefault }
    }

    return defaultProps.as || 'div'
}

export default getElementType
